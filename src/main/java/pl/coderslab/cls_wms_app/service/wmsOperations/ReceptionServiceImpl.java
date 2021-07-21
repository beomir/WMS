package pl.coderslab.cls_wms_app.service.wmsOperations;


import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import lombok.extern.slf4j.Slf4j;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import javax.servlet.http.HttpSession;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class ReceptionServiceImpl implements ReceptionService {
    private final ReceptionRepository receptionRepository;
    private final EmailRecipientsRepository emailRecipientsRepository;
    private final SendEmailService sendEmailService;
    private final SchedulerRepository schedulerRepository;
    private final ArticleRepository articleRepository;
    private final VendorRepository vendorRepository;
    private final UnitRepository unitRepository;
    private final CompanyRepository companyRepository;
    private final WarehouseRepository warehouseRepository;
    private final WorkDetailsRepository workDetailsRepository;
    private final LocationRepository locationRepository;
    private final StatusRepository statusRepository;
    private final StockRepository stockRepository;
    private final TransactionService transactionService;
    private final IssueLogService issueLogService;
    public String insertReceptionFileResult;
    private final CustomerUserDetailsService customerUserDetailsService;
    private final IssueLogRepository issueLogRepository;
    private final ExtremelyRepository extremelyRepository;
    private final LocationService locationService;

    @Autowired
    public ReceptionServiceImpl(ReceptionRepository receptionRepository, EmailRecipientsRepository emailRecipientsRepository, SendEmailService sendEmailService, SchedulerRepository schedulerRepository, ArticleRepository articleRepository, VendorRepository vendorRepository, UnitRepository unitRepository, CompanyRepository companyRepository, WarehouseRepository warehouseRepository, WorkDetailsRepository workDetailsRepository, LocationRepository locationRepository, StatusRepository statusRepository, StockRepository stockRepository, TransactionService transactionService, IssueLogService issueLogService, CustomerUserDetailsService customerUserDetailsService, IssueLogRepository issueLogRepository, ExtremelyRepository extremelyRepository, LocationService locationService) {
        this.receptionRepository = receptionRepository;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.sendEmailService = sendEmailService;
        this.schedulerRepository = schedulerRepository;
        this.articleRepository = articleRepository;
        this.vendorRepository = vendorRepository;
        this.unitRepository = unitRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.workDetailsRepository = workDetailsRepository;
        this.locationRepository = locationRepository;
        this.statusRepository = statusRepository;
        this.stockRepository = stockRepository;
        this.transactionService = transactionService;
        this.issueLogService = issueLogService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.issueLogRepository = issueLogRepository;
        this.extremelyRepository = extremelyRepository;
        this.locationService = locationService;
    }


    @Override
    public void add(Reception reception) {
        receptionRepository.save(reception);
    }

    @Override
    public void addNew(Reception reception,HttpSession session) {
        reception.setStatus(statusRepository.getStatusByStatusName("creation_pending","Reception"));
        reception.setPieces_qty(0L);
        receptionRepository.save(reception);
        Transaction transactionAdded = new Transaction();
        transactionAdded.setTransactionDescription("Reception created manually");
        transactionAdded.setAdditionalInformation("Reception Number: " + reception.getReceptionNumber() + " created");
        transactionAdded.setTransactionGroup("Reception");
        transactionAdded.setTransactionType("111");
        transactionAdded.setCreated(reception.getCreated());
        transactionAdded.setCreatedBy(SecurityUtils.usernameForActivations());
        transactionAdded.setCompany(reception.getCompany());
        transactionAdded.setWarehouse(reception.getWarehouse());
        transactionAdded.setReceptionNumber(reception.getReceptionNumber());
        transactionAdded.setArticle(0L);
        transactionAdded.setQuality("");
        transactionAdded.setUnit("");
        transactionAdded.setVendor("");
        transactionAdded.setQuantity(0L);
        transactionAdded.setHdNumber(0L);
        transactionAdded.setReceptionStatus("Created");

        transactionService.add(transactionAdded);

        session.setAttribute("receptionMessage","Reception: " + reception.getReceptionNumber() + " created. Please enter the content by selecting option from drop list");
    }

    @Override
    public void addNewReceptionLine(Reception reception,HttpSession session) {
        reception.setStatus(statusRepository.getStatusByStatusName("creation_pending","Reception"));

        reception.setHd_number(extremelyRepository.nextPalletNbr());
        receptionRepository.save(reception);
        Transaction transactionAdded = new Transaction();
        transactionAdded.setTransactionDescription("Reception Line created manually");
        transactionAdded.setAdditionalInformation("Reception line for reception number: " + reception.getReceptionNumber() + " created");
        transactionAdded.setTransactionGroup("Reception");
        transactionAdded.setTransactionType("116");
        saveTransactionModel(reception, transactionAdded);
        transactionAdded.setReceptionStatus("creation_pending");
        transactionService.add(transactionAdded);
        session.setAttribute("receptionMessage","Article: " + reception.getArticle().getArticle_number() + " with: " + reception.getPieces_qty() +  ", pieces added on HD: " + reception.getHd_number() + ", by: " + reception.getChangeBy());

    }

    @Override
    public void assignDoorLocationToReception(Long receptionNumber, Long doorLocation,  HttpSession session) {
        log.debug("receptionNumber: " + receptionNumber);
        log.debug("doorLocation: " + doorLocation);
        boolean enoughCapacity = false;
        List<Reception> receptions = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
        Location location = locationRepository.getOne(doorLocation);
        Transaction transaction = new Transaction();

        for (Reception singularReception: receptions) {
            if(locationService.reduceTheAvailableContentOfTheLocation(location.getLocationName(),singularReception.getArticle().getArticle_number(),singularReception.getPieces_qty(),singularReception.getWarehouse().getName(),singularReception.getCompany().getName(),"Reception_unloading")){
                singularReception.setStatus(statusRepository.getStatusByStatusName("unloading_pending","Reception"));
                singularReception.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                singularReception.setLocation(location);
                receptionRepository.save(singularReception);
                transaction.setAdditionalInformation("Assign reception: " + receptionNumber + " to dock door: " + location.getLocationName());
                transaction.setReceptionStatus(singularReception.getStatus().getStatus());
                saveTransactionModel(singularReception, transaction);
                enoughCapacity = true;
            }
            else{
                transaction.setAdditionalInformation("Assign reception: " + receptionNumber + " to dock door: " + location.getLocationName() + " unsuccessful because of not enough location free capacity");
                transaction.setReceptionStatus(singularReception.getStatus().getStatus());
                enoughCapacity = false;
            }
        }
        if(enoughCapacity){
            transaction.setTransactionDescription("Reception assigned to reception dock door");
            session.setAttribute("receptionMessage", "Reception: " + receptionNumber + " assigned to door: " + location.getLocationName());
            transaction.setTransactionType("118");
            transactionService.add(transaction);
            log.info("close creation service receptionMessage: " + session.getAttribute("receptionMessage"));
        }
        else{
            session.setAttribute("receptionMessage", "Not enough space or free weight in location: " + location.getLocationName());
        }

    }

    @Override
    public void finishUnloading(Long receptionNumber,  HttpSession session) {
        log.debug("receptionNumber: " + receptionNumber);
        List<Reception> receptions = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
        Transaction transaction = new Transaction();
        for (Reception singularReception: receptions) {
            singularReception.setStatus(statusRepository.getStatusByStatusName("put_away_pending","Reception"));
            receptionRepository.save(singularReception);
            String destinationLocation = locationRepository.getAvailableLocation("%" + singularReception.getArticle().getArticleTypes().getArticleClass() + "%",singularReception.getArticle().getWeight(),singularReception.getArticle().getVolume(),singularReception.getWarehouse().getName()).getLocation();
            log.debug("destinationLocation: " + destinationLocation);
            WorkDetails work = new WorkDetails();
            work.setPiecesQty(singularReception.getPieces_qty());
            work.setArticle(singularReception.getArticle());
            work.setCompany(singularReception.getCompany());
            work.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            work.setChangeBy(SecurityUtils.usernameForActivations());
            work.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            work.setWarehouse(singularReception.getWarehouse());
            work.setHdNumber(singularReception.getHd_number());
            work.setStatus("open");
            work.setFromLocation(singularReception.getLocation());
            work.setToLocation(locationRepository.findLocationByLocationName(destinationLocation,singularReception.getWarehouse().getName()));
            work.setHandle(receptionNumber.toString());
            work.setWorkDescription("Reception Put Away");
            log.error(LocalDate.now().getYear() +""+ StringUtils.leftPad(Integer.toString(LocalDate.now().getDayOfMonth()), 2, "0") +"" + receptionNumber + "" +singularReception.getId());
            work.setWorkNumber(Long.parseLong(LocalDate.now().getYear() +""+ StringUtils.leftPad(Integer.toString(LocalDate.now().getDayOfMonth()), 2, "0") +"" + receptionNumber));
            work.setWorkType("Reception");
            workDetailsRepository.save(work);
            transaction.setTransactionDescription("Reception unloaded");
            transaction.setAdditionalInformation("Reception: " + receptionNumber + " unloaded");
            transaction.setReceptionStatus(singularReception.getStatus().getStatus());
            saveTransactionModel(singularReception, transaction);

            Transaction workCreation = new Transaction();
            workCreation.setTransactionDescription("Reception PutAway Work created");
            workCreation.setAdditionalInformation("Work number: " + work.getWorkNumber() + "for pallet: " + work.getHdNumber() + ", from location: " + work.getFromLocation().getLocationName() + " to: " + work.getToLocation().getLocationName() + " with : " + work.getPiecesQty() + " pieces of article: " + work.getArticle().getArticle_number() + "created" );
            workCreation.setReceptionStatus(singularReception.getStatus().getStatus());
            saveTransactionModel(singularReception, workCreation);
            transactionService.add(workCreation);

            Stock stock = new Stock();
            stock.setStatus(statusRepository.getStatusByStatusName("on_reception","StockReception"));
            stock.setWarehouse(singularReception.getWarehouse());
            stock.setReceptionNumber(singularReception.getReceptionNumber());
            stock.setHandle(singularReception.getReceptionNumber().toString());
            stock.setComment("Reception: " + singularReception.getReceptionNumber());
            stock.setPieces_qty(singularReception.getPieces_qty());
            stock.setCompany(singularReception.getCompany());
            stock.setArticle(singularReception.getArticle());
            stock.setUnit(singularReception.getUnit());
            stock.setQuality(singularReception.getQuality());
            stock.setLocation(singularReception.getLocation());
            stock.setHd_number(singularReception.getHd_number());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stockRepository.save(stock);
        }
        transaction.setTransactionType("119");
        transactionService.add(transaction);

        session.setAttribute("receptionMessage","Reception: " + receptionNumber + " unloaded completely, put away works created" );

        log.error("close creation service receptionMessage: " + session.getAttribute("receptionMessage"));
    }


    @Override
    public void edit(Reception reception, HttpSession session) {
        Transaction transaction = new Transaction();
        if (reception.getStatus() == null){
            transaction.setTransactionDescription("First line for Reception created");
            transaction.setAdditionalInformation("First line for Reception number: " + reception.getReceptionNumber() + " created");
            transaction.setTransactionType("117");
            reception.setStatus(statusRepository.getStatusByStatusName("creation_pending","Reception"));
            reception.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            session.setAttribute("receptionMessage","First line for Reception: " + reception.getReceptionNumber() + " created with: " + reception.getPieces_qty() + " pieces");
        }
        if (reception.getStatus() != null){
            transaction.setTransactionDescription("Reception edited manually");
            transaction.setAdditionalInformation("Reception Number: " + reception.getReceptionNumber() + " edited");
            transaction.setTransactionType("112");
        }

        if(reception.getHd_number() == null){
            reception.setHd_number(extremelyRepository.nextPalletNbr());
        }

        saveTransactionModel(reception, transaction);
        transaction.setReceptionStatus(reception.getStatus().getStatus());
        transactionService.add(transaction);
        receptionRepository.save(reception);
    }

    private void saveTransactionModel(Reception reception, Transaction transaction) {
        transaction.setTransactionGroup("Reception");
        transaction.setCreated(reception.getCreated());
        transaction.setCreatedBy(SecurityUtils.usernameForActivations());
        transaction.setCompany(reception.getCompany());
        transaction.setWarehouse(reception.getWarehouse());
        transaction.setReceptionNumber(reception.getReceptionNumber());
        transaction.setArticle(reception.getArticle().getArticle_number());
        transaction.setQuality(reception.getQuality());
        transaction.setUnit(reception.getUnit().getName());
        transaction.setVendor(reception.getVendor().getName());
        transaction.setQuantity(reception.getPieces_qty());
        transaction.setHdNumber(reception.getHd_number());
    }




    @Override
    public Reception findById(Long id) {
        return receptionRepository.getOne(id);
    }

    @Override
    public void finishReception(Long receptionNmbr,HttpSession session) {
        List<Reception> finishedReception = receptionRepository.getReceptionByReceptionNumber(receptionNmbr);
        List<WorkDetails> workDetailsList = workDetailsRepository.getWorkDetailsByHandle(receptionNmbr.toString());
        //transaction
        for(Reception reception : finishedReception){
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Reception Finished");
            transaction.setAdditionalInformation("Reception Number: " + reception.getReceptionNumber()  +  ", HD number: " + reception.getHd_number() + " received on stock");
            transaction.setTransactionGroup("Reception");
            transaction.setReceptionStatus("Closed");
            transaction.setTransactionType("114");
            saveTransactionModel(reception, transaction);
            transactionService.add(transaction);
        }

        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForShipmentType(receptionRepository.getCompanyNameByReceptionNumber(receptionNmbr), "%Receptions%");
        String receptionNbr = receptionNmbr.toString();
        String warehouse = receptionRepository.getWarehouseByReceptionNumber(receptionNmbr);
        File reception = new File("inbound/inbound" + receptionNbr + ".txt");
        while (reception.exists()) {
            int random = new Random().nextInt(100);
            reception = new File("inbound/inbound" + receptionNbr + "duplicateNbr" + random + ".txt");
        }
        try (FileWriter fileWriter = new FileWriter(reception, true)) {
            fileWriter.append("Reception_Number:" + receptionNbr + "\n");
            for (Reception value : finishedReception) {
                fileWriter.append("ArticleNumber:" + value.getArticle().getArticle_number().toString() + ",");
                fileWriter.append("ArticleDescription:" + value.getArticle().getArticle_desc() + ",");
                fileWriter.append("HandleDeviceNumber:" + value.getHd_number().toString() + ",");
                fileWriter.append("PiecesQuantity:" + value.getPieces_qty().toString() + ",");
                fileWriter.append("VendorName:" + value.getVendor().getName() + ",").append("VendorAddress:" + value.getVendor().getCity() + "," + value.getVendor().getStreet() + "," + value.getVendor().getCountry());
                fileWriter.append("Unit:" + value.getUnit().getName() + ",");
                fileWriter.append("Company:" + value.getCompany().getName() + ",");
                fileWriter.append("FromWarehouse:" + value.getWarehouse().getName() + ",");
                fileWriter.append("ChangedBy:" + value.getChangeBy() + "\n");
                for(WorkDetails workDetails : workDetailsList){
                    log.error("workDetails.getHdNumber: " + workDetails.getHdNumber());
                    log.error("value.getHd_number: " + value.getHd_number());
                    if(workDetails.getHdNumber().equals(value.getHd_number())){
                        fileWriter.append("Receive in door: " + workDetails.getFromLocation().getLocationName()  + "\n");
                        fileWriter.append("Located in: " + workDetails.getToLocation().getLocationName() + "\n");
                    }

                }
            }

        } catch (IOException ex) {
            log.error("Cannot save a file" + reception);
        }
        String filePath = String.valueOf(reception);
        Path path = Paths.get(filePath);
        log.error(path + " of reception file");
        if (Files.exists(path)) {
            for (EmailRecipients value : mailGroup) {
                sendEmailService.sendEmail(value.getEmail(), "Dear client,<br/><br/>in Warehouse: <b>" + warehouse + "</b> our team receipt of goods for Reception number: <b>" + receptionNbr + "</b>. Goods are already available on our stock. Details you can find in attachment", "Reception " + receptionNbr, filePath);

            }
        } else {
            log.error(path + " is empty, cannot send the email");
        }
        if(workDetailsRepository.checkIfWorksExistsOnlyByHandle(receptionNmbr.toString())>0){
            for (Reception valueReception : finishedReception){
                valueReception.setStatus(statusRepository.getStatusByStatusName("closed","Reception"));
                add(valueReception);
            }
            for (WorkDetails workDetails : workDetailsList){
                workDetails.setStatus("close");
                workDetailsRepository.save(workDetails);
            }
            session.setAttribute("receptionMessage","Reception: " + receptionNmbr + " closed manually by user from reception management screen without scanner confirmation");
            Transaction transaction = new Transaction();
            transaction.setTransactionGroup("Reception");
            transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            transaction.setCreatedBy(SecurityUtils.usernameForActivations());
            transaction.setCompany(companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
            transaction.setWarehouse(warehouseRepository.getWarehouseByName(warehouse));
            transaction.setReceptionNumber(receptionNmbr);
            transaction.setArticle(0L);
            transaction.setQuality("");
            transaction.setUnit("");
            transaction.setVendor("");
            transaction.setQuantity(0L);
            transaction.setHdNumber(0L);
            transaction.setTransactionDescription("Reception: " + receptionNmbr + " closed manually by user from reception management screen without scanner confirmation");
            transactionService.add(transaction);

            List<Stock> stockList = stockRepository.getStockListByReceptionNumber(receptionNmbr);
            for(Stock stock : stockList){
                stock.setStatus(statusRepository.getStatusByStatusName("on_hand","Stock"));
                stockRepository.save(stock);
            }

        }

    }


    @Override
    public Long lastReception() {
        return receptionRepository.lastReception();
    }



    @Override
    public List<Reception> openedReceptions(Long id, String username) {
        return receptionRepository.openedReceptions(id, username);
    }

    @Override
    public int qtyOfOpenedReceptions(Long id, String username) {
        return receptionRepository.qtyOfOpenedReceptions(id, username);
    }


    @Override
    public List<Integer> pallets() {
        List<Integer> ret = new ArrayList<>();
        for (int i = 1; i <= 66; i++) {
            ret.add(i);
        }
        return ret;
    }


    @Override
    public void closeCreation(Long receptionNumber,HttpSession session) {
        List<Reception> receptions = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
        for(Reception reception : receptions){
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Reception Start to be unloaded");
            transaction.setAdditionalInformation("Reception Number: " + reception.getReceptionNumber()  +  ", HD number: " + reception.getHd_number() + " starts unloading");
            transaction.setReceptionStatus("unloading_pending");
            transaction.setTransactionType("113");
            transaction.setTransactionGroup("Reception");
            saveTransactionModel(reception, transaction);
            transactionService.add(transaction);
        }
        session.setAttribute("receptionMessage","Truck with reception: " + receptionNumber + " arrived to warehouse");

        log.error("close creation service receptionMessage: " + session.getAttribute("receptionMessage"));

    }

    @Override
    public void openCreation(Long receptionNumber,HttpSession session) {
        List<Reception> receptions = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
        for(Reception reception : receptions){
            locationService.restoreTheAvailableLocationCapacity(reception.getLocation().getLocationName(),reception.getArticle().getArticle_number(),reception.getPieces_qty(),reception.getWarehouse().getName(),reception.getCompany().getName());
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Reception Opened");
            transaction.setAdditionalInformation("Reception Number: " + reception.getReceptionNumber()  +  ", HD number: " + reception.getHd_number() + " opened");
            transaction.setTransactionGroup("Reception");
            transaction.setTransactionType("115");
            transaction.setReceptionStatus("Created");
            saveTransactionModel(reception, transaction);
            transactionService.add(transaction);
            reception.setStatus(statusRepository.getStatusByStatusName("creation_pending","Reception"));
            reception.setLocation(null);
            receptionRepository.save(reception);
        }
        session.setAttribute("receptionMessage","Reception: " + receptionNumber + " back to creating");

        log.error("close creation service receptionSearch.message: " + session.getAttribute("receptionMessage"));
    }

    @Override
    public void sendReceptions(String company) {
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForStockType("%Receptions%", company);
        Scheduler scheduler = schedulerRepository.getOneSchedulerByCompanyName(company, "%Reception%");
        LocalDate dateBack = LocalDate.now().minusDays(scheduler.getHowManyDaysBack());
        List<Reception> receptionForCompany = receptionRepository.getReceptionsFromXDayBack(String.valueOf(dateBack), company);

        File receptions = new File("receptions/receptionsFor" + company + "From" + dateBack + "To" + LocalDate.now() + ".txt");
        while (receptions.exists()) {
            int random = new Random().nextInt(100);
            receptions = new File("receptions/receptionsFor" + company + "From" + dateBack + "To" + LocalDate.now() + random + ".txt");
        }
        try (FileWriter fileWriter = new FileWriter(receptions, true)) {
            fileWriter.append("Receptions for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now() + "\n" + "\n");
            for (Reception values : receptionForCompany) {
                fileWriter.append("\n" + "Reception Number:" + values.getReceptionNumber().toString() + "\n");
                fileWriter.append("Article Number:" + values.getArticle().getArticle_number().toString() + "\n");
                fileWriter.append("Article Description:" + values.getArticle().getArticle_desc() + "\n");
                fileWriter.append("Handle Device Number:" + values.getHd_number().toString() + "\n");
                fileWriter.append("Pieces Quantity:" + values.getPieces_qty().toString() + "\n");
                fileWriter.append("Vendor Name:" + values.getVendor().getName() + ", ").append("Vendor Address:" + values.getVendor().getCity() + ", " + values.getVendor().getStreet() + ", " + values.getVendor().getCountry() + "\n");
                fileWriter.append("Unit:" + values.getUnit().getName() + "\n");
                fileWriter.append("Company:" + values.getCompany().getName() + "\n");
                fileWriter.append("Warehouse:" + values.getWarehouse().getName() + "\n");
                if (values.getStatus().getStatus().equals("closed")) {
                    fileWriter.append("Reception status: Closed - Goods were transferred to stock locations" + "\n");
                    fileWriter.append("Closed:" + values.getLast_update() + "\n");
                    fileWriter.append("Closed by:" + values.getChangeBy() + "\n");
                } else if (values.getStatus().getStatus().equals("put_away_pending")) {
                    fileWriter.append("Reception status: Put away pending - Goods are checking by our employee and locate in stock locations" + "\n");
                    fileWriter.append("Finished:" + values.getLast_update() + "\n");
                    fileWriter.append("Finished by:" + values.getChangeBy() + "\n");
                } else if (values.getStatus().getStatus().equals("unloading_pending")) {
                    fileWriter.append("Reception status: Unloading pending - Truck arrived to our warehouse and unloading is started" + "\n");
                    fileWriter.append("Created:" + values.getLast_update() + "\n");
                    fileWriter.append("Created by:" + values.getChangeBy() + "\n");
                } else if (values.getStatus().getStatus().equals("creation_pending")) {
                    fileWriter.append("Reception status: Created - Reception created but physically are not in warehouse yet" + "\n");
                    fileWriter.append("Created:" + values.getLast_update() + "\n");
                    fileWriter.append("Created by:" + values.getChangeBy() + "\n");
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = String.valueOf(receptions);
        Path path = Paths.get(filePath);
        if (Files.exists(path)) {
            for (EmailRecipients valuess : mailGroup) {
                sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> receptions for Company: <b>" + company + "</b> from: " + dateBack + ", to:" + LocalDate.now() + ". Data in attachment", "Receptions for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now(), filePath);
            }
        } else {
            log.debug(path + " is empty, cannot send the email");
        }

    }

    @Override
    public void insertFileContentToDB(File fsFile) {
        try (BufferedReader br = new BufferedReader(new FileReader(fsFile))) {
            String line;
            int counter = 0;
            String issueLog = "";
            insertReceptionFileResult = "";
            while ((line = br.readLine()) != null) {
                String[] receptionFile = line.split(",");
                counter = counter + 1;
                int round = 0;
                Reception reception = new Reception();
                for (String value : receptionFile) {
                    round++;
                    //reception number
                    if (value.contains("Reception_Number:") && round == 1) {
                        String receptionNbr = value.substring(value.lastIndexOf("Reception_Number:") + 17);
                        if (receptionRepository.checkIfReceptionAlreadyExists(Long.parseLong(receptionNbr)) < 1) {
                            reception.setReceptionNumber(Long.parseLong(receptionNbr));
                            log.debug("Line: " + counter + " Reception number not exists earlier in WMS. Reception number is proper");
                        } else {
                            log.error("Line: " + counter + " Reception number: " + Long.parseLong(receptionNbr) + " already exists in WMS");
                            issueLog = issueLog + "\n Line: " + counter + " Reception number: " + Long.parseLong(receptionNbr) + " already exists in WMS " + LocalDateTime.now();
                        }
//                        System.out.println("Numer przyjecia: " + receptionNbr);
                    } else if (!value.contains("Reception_Number:") && round == 1) {
                        log.error("Line: " + counter + " Lack information about Reception_Number in file or is not in proper place of file structure (should be first)");
                        issueLog = issueLog + "\n Line " + counter + " Lack information about Reception_Number in file or is not in proper place of file structure ( after should be first )" + LocalDateTime.now();
                    }


                    //article number
                    if (value.contains(("ArticleNumber:")) && round == 2) {
                        String articleNbr = value.substring(value.lastIndexOf("ArticleNumber:") + 14);
                        Article articleFromFile = articleRepository.findArticleByArticle_number(Long.parseLong(articleNbr));
                        if (articleFromFile != null) {
                            reception.setArticle(articleFromFile);
                            log.debug("Article number: " + articleNbr + " is proper");
                        } else {
                            log.error(" Line: " + counter + " Article number: " + articleNbr + "  not exists in DB");
                            issueLog = issueLog + "\n Line: " + counter + " Article number: " + articleNbr + " not exists in DB " + LocalDateTime.now();
                        }
                    } else if (!value.contains(("ArticleNumber:")) && round == 2) {
                        log.error("Line: " + counter + " Lack information about article in file or is not in proper place of file structure ( after Reception_Number )");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about article in file or is not in proper place of file structure ( after Reception_Number )" + LocalDateTime.now();

                    }


                    //HD_number
                    if (value.contains(("HandleDeviceNumber:")) && round == 3) {
                        String handleDeviceNbr = value.substring(value.lastIndexOf("HandleDeviceNumber:") + 19);
                        if (stockRepository.checkIfHdNumberExistsOnStock(Long.parseLong(handleDeviceNbr)) < 1 && receptionRepository.checkIfHdNumberExistsOnReception(Long.parseLong(handleDeviceNbr))<1 && workDetailsRepository.checkIfHdNumberExistsInWorkDetails(Long.parseLong(handleDeviceNbr)) < 1) {
                            reception.setHd_number(Long.parseLong(handleDeviceNbr));
                            log.debug("HD number: " + handleDeviceNbr + " not exists already in WMS and is ready to be add");
                        } else {
                            log.error("Line: " + counter + " HD number: " + handleDeviceNbr + " already exists on stock");
                            issueLog = issueLog + "\n Line: " + counter + " HD number: " + handleDeviceNbr + " already exists on stock " + LocalDateTime.now();
                        }
                    } else if (!value.contains(("HandleDeviceNumber:")) && round == 3) {
                        log.error("Line: " + counter + " Lack information about HandleDeviceNumber: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about HandleDeviceNumber: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber )" + LocalDateTime.now();

                    }


                    //Pieces Qty
                    if (value.contains(("PiecesQuantity:")) && round == 4) {
                        String piecesQuantity = value.substring(value.lastIndexOf("PiecesQuantity:") + 15);
                        reception.setPieces_qty(Long.parseLong(piecesQuantity));
                        log.debug("Proper data about PiecesQuantity: " + Long.parseLong(piecesQuantity));
                    } else if (!value.contains(("PiecesQuantity:")) && round == 4) {
                        log.error("Line: " + counter + " Lack of information about PiecesQuantity: or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber");
                        issueLog = issueLog + "\n Line: " + counter + " Lack of information about PiecesQuantity: or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber )" + LocalDateTime.now();
                    }


                    //Vendor name
                    if (value.contains(("VendorName:")) && round == 5) {
                        String vendorName = value.substring(value.lastIndexOf("VendorName:") + 11);
                        Vendor vendorFromFile = vendorRepository.getVendorByName(vendorName);

                        if (vendorFromFile != null) {
                            reception.setVendor(vendorFromFile);
                            log.debug("Vendor: " + vendorName + " from file exists in DB");
                        } else {
                            log.error(" Line: " + counter + " Vendor " + vendorName + " not exists in DB");
                        }
                    } else if (!value.contains(("VendorName:")) && round == 5) {
                        log.error("Line: " + counter + " Lack information about VendorName: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about VendorName: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity )" + LocalDateTime.now();

                    }


                    //unit
                    if (value.contains(("Unit:")) && round == 6) {
                        String unit = value.substring(value.lastIndexOf("Unit:") + 5);
                        Unit unitFromFile = unitRepository.getUnitByName(unit);

                        if (unitFromFile != null) {
                            reception.setUnit(unitFromFile);
                            log.debug("Unit from file: " + unit + " is properly in DB");
                        } else {
                            log.error("Line: " + counter + " Unit: " + unit + " not exists in DB");
                        }
                    } else if (!value.contains(("Unit:")) && round == 6) {
                        log.error("Line: " + counter + " Lack information about Unit: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about Unit: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName )" + LocalDateTime.now();

                    }


                    //company
                    if (value.contains(("Company:")) && round == 7) {
                        String company = value.substring(value.lastIndexOf("Company:") + 8);
                        Company companyFromFile = companyRepository.getCompanyByName(company);

                        if (companyFromFile != null) {
                            reception.setCompany(companyFromFile);
                            log.debug("Company: " + company + " is properly in DB");
                        } else {
                            log.error("Line: " + counter + " Company: " + company + " not exists in DB");
                        }
                    } else if (!value.contains(("Company:")) && round == 7) {
                        log.error("Line: " + counter + " Lack information about FromWarehouse: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about Company: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, " +
                                "HandleDeviceNumber, PiecesQuantity, VendorName, Unit )  "
                                + LocalDateTime.now();
                    }


                    //warehouse
                    if (value.contains(("FromWarehouse:")) && round == 8) {
                        String warehouse = value.substring(value.lastIndexOf("FromWarehouse:") + 14);
                        Warehouse warehouseFromFile = warehouseRepository.getWarehouseByName(warehouse);

                        if (warehouseFromFile != null) {
                            reception.setWarehouse(warehouseFromFile);
                            log.debug("Warehouse: " + warehouse + " from file is properly in DB");
                        } else {
                            log.error("Line: " + counter + " warehouse: " + warehouse + " not exists in DB");
                        }
                    } else if (!value.contains(("FromWarehouse:")) && round == 8) {
                        log.error("Line: " + counter + " Lack information about FromWarehouse: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about FromWarehouse: in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company )" + LocalDateTime.now();
                    }


                    //quality
                    if (value.contains("Quality:") && round == 9 ) {
                        String quality = value.substring(value.lastIndexOf("Quality:") + 8);
                        reception.setQuality(quality);
                        log.debug("Quality: " + quality + " is ok");
                    } else if (!value.contains("Quality:") && round == 9 ) {
                        log.error("Line: " + counter + " Lack information about Quality in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company, FromWarehouse");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about Quality in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company, FromWarehouse )" + LocalDateTime.now();
                    }
                    if(round == 10){
                        log.debug("Finish file insert");
                    }
                }

                reception.setStatus(statusRepository.getStatusByStatusName("creation_pending","Reception"));
                reception.setChangeBy(SecurityUtils.usernameForActivations());
                reception.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                reception.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                if (issueLog.equals("")) {
                    add(reception);

                    Transaction transaction = new Transaction();
                    transaction.setTransactionDescription("Create Reception from File");
                    transaction.setAdditionalInformation("Reception number: " + reception.getReceptionNumber() + " created from file");
                    transaction.setTransactionGroup("Reception");
                    transaction.setReceptionStatus("creation_pending");
                    transaction.setTransactionType("121");
                    saveTransactionModel(reception, transaction);
                    transactionService.add(transaction);

                    String fileName = fsFile.toPath().getFileName().toString();
                    Path passedPath = Paths.get("src/main/resources/static/files/input/receptions/passed/Passed-" + TimeUtils.timeNowShort() + fileName);
                    Files.copy(fsFile.toPath(), passedPath, StandardCopyOption.REPLACE_EXISTING);
                    log.debug("Reception created. File transferred to directory passed. Transaction Log 121 Created");
                    insertReceptionFileResult = "File " + fsFile.toPath().getFileName().toString() + " successfully inserted to DB";

                } else {

                    String fileName = fsFile.toPath().getFileName().toString();
                    Path errorPath = Paths.get("src/main/resources/static/files/input/receptions/errors/Error-" + TimeUtils.timeNowShort() + "-" + fileName);

                    //create file with issueLog
                    File errorFile = new File("src/main/resources/static/files/input/receptions/errors/IssueLog-" + TimeUtils.timeNowShort() + "-" + fileName);
                    try (FileWriter fileWriter = new FileWriter(errorFile,true)) {
                        fileWriter.append(issueLog);
                    }
                    catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Create issueLog
                    IssueLog issuelog = new IssueLog();
                    if(issueLog.length()>100){
                        issuelog.setIssueLogContent(issueLog.substring(0,100));
                    }
                    else{
                        issuelog.setIssueLogContent(issueLog);
                    }
                    issuelog.setCreated(LocalDateTime.now().toString());
                    issuelog.setIssueLogFileName(errorFile.getName());
                    issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
                    issuelog.setWarehouse(warehouseRepository.getOneWarehouse(customerUserDetailsService.chosenWarehouse));
                    issuelog.setIssueLogFilePath(errorFile.toString());
                    if(reception.getCompany() == null ) {
                        issuelog.setAdditionalInformation("");
                    }
                    else{
                        issuelog.setAdditionalInformation("Company: " + reception.getCompany().getName() + ", Reception number: " + reception.getReceptionNumber());
                    }
                    if(issueLogRepository.checkDoubleFiles("%"+fileName)<1){
                        issueLogService.add(issuelog);
                    }


                    //copy to errors directory
                    try {
                        Files.copy(fsFile.toPath(), errorPath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    log.error("File transferred to directory errors. Issue log: " + issueLog);
                    insertReceptionFileResult = "File " + fsFile.toPath().getFileName().toString() + " is improper and not all file content was insert to DB, check Issue Log file or Issue Transactions screen";
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Files.delete(fsFile.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public List<ReceptionRepository.ReceptionViewObject> receptionSummary(String receptionCompany, String receptionWarehouse,
                                                                          String receptionVendor, String receptionStatus, String receptionLocation,
                                                                          String receptionReceptionNumber, String receptionHdNumber,
                                                                          String receptionCreatedFrom, String receptionCreatedTo, String receptionCreatedBy) {
        if(receptionCompany == null || receptionCompany.equals("")){
            receptionCompany = companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName();
        }
        if(receptionWarehouse == null || receptionWarehouse.equals("")){
            receptionWarehouse = "%";
        }
        if(receptionVendor == null || receptionVendor.equals("")){
            receptionVendor = "%" ;
        }
        if(receptionStatus == null || receptionStatus.equals("")){
            receptionStatus = "%";
        }
        if( receptionReceptionNumber == null || receptionReceptionNumber.equals("")){
            receptionReceptionNumber = "%";
        }
        if(receptionHdNumber == null || receptionHdNumber.equals("")){
            receptionHdNumber = "%";
        }
        if(receptionCreatedFrom == null || receptionCreatedFrom.equals("")){
            receptionCreatedFrom = "1970-01-01";
        }
        if(receptionCreatedTo == null || receptionCreatedTo.equals("")){
            receptionCreatedTo = "2222-02-02";
        }
        if(receptionCreatedBy == null || receptionCreatedBy.equals("")){
            receptionCreatedBy = "%" ;
        }
        if(receptionLocation == null || receptionLocation.equals("")){
            receptionLocation = "%";
        }

        log.error(" createdBy: " + receptionCreatedBy);
        log.error(" warehouse: " + receptionWarehouse );
        log.error(" company: " + receptionCompany );
        log.error(" vendor: " + receptionVendor);
        log.error(" receptionNumber: " + receptionReceptionNumber);
        log.error(" hdNumber: " + receptionHdNumber);
        log.error(" status: " + receptionStatus);
        log.error(" location: " + receptionLocation);
        log.error(" createdFrom: " + receptionCreatedFrom);
        log.error(" createdTo: " + receptionCreatedTo);

        return receptionRepository.getReceptionSummary(receptionCompany,receptionWarehouse,receptionVendor,receptionStatus,receptionLocation,receptionReceptionNumber,receptionHdNumber,receptionCreatedFrom,receptionCreatedTo,receptionCreatedBy);
    }

}
