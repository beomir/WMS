package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import lombok.extern.slf4j.Slf4j;
import pl.coderslab.cls_wms_app.repository.*;

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
    private final StatusService statusService;
    private final StockRepository stockRepository;
    private final TransactionService transactionService;
    private final IssueLogService issueLogService;
    public String insertReceptionFileResult;

    @Autowired
    public ReceptionServiceImpl(ReceptionRepository receptionRepository, EmailRecipientsRepository emailRecipientsRepository, SendEmailService sendEmailService, SchedulerRepository schedulerRepository, ArticleRepository articleRepository, VendorRepository vendorRepository, UnitRepository unitRepository, CompanyRepository companyRepository, WarehouseRepository warehouseRepository, StatusService statusService, StockRepository stockRepository, TransactionService transactionService, IssueLogService issueLogService) {
        this.receptionRepository = receptionRepository;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.sendEmailService = sendEmailService;
        this.schedulerRepository = schedulerRepository;
        this.articleRepository = articleRepository;
        this.vendorRepository = vendorRepository;
        this.unitRepository = unitRepository;
        this.companyRepository = companyRepository;
        this.warehouseRepository = warehouseRepository;
        this.statusService = statusService;
        this.stockRepository = stockRepository;
        this.transactionService = transactionService;
        this.issueLogService = issueLogService;
    }

    @Override
    public void add(Reception reception) {
        receptionRepository.save(reception);
    }

    @Override
    public int getCreatedReceptionById(Long receptionNbr) {
        return receptionRepository.getCreatedReceptionById(receptionNbr);
    }

    @Override
    public void updateCloseCreationValue(Long receptionNbr) {
        if (getCreatedReceptionById(receptionNbr) > 0) {
            receptionRepository.updateCloseCreationValue(receptionNbr);
        }
    }


    @Override
    public void updateFinishedReceptionValue(Long receptionNbrtoFinish) {
        receptionRepository.updateFinishedReceptionValue(receptionNbrtoFinish);
    }

    @Override
    public void insertDataToStockAfterFinishedReception(Long receptionNbr) {
        receptionRepository.insertDataToStockAfterFinishedReception(receptionNbr);
    }


    @Override
    public List<Reception> getReceptions(Long id, String username) {
        return receptionRepository.getReceptions(id, username);
    }

    @Override
    public List<Reception> getReception(Long id) {
        return receptionRepository.getReception(id);
    }

    @Override
    public Reception findById(Long id) {
        return receptionRepository.getOne(id);
    }

    @Override
    public void finishReception(Long receptionNmbr) {
        List<Reception> finishedReception = receptionRepository.getReceptionByReceptionNumber(receptionNmbr);
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
            }

        } catch (IOException ex) {
            System.out.println("Cannot save a file" + reception);
        }
        String filePath = String.valueOf(reception);
        Path path = Paths.get(filePath);
        System.out.println(path + " of reception file");
        if (Files.exists(path)) {
            for (EmailRecipients value : mailGroup) {
                sendEmailService.sendEmail(value.getEmail(), "Dear client,<br/><br/>in Warehouse: <b>" + warehouse + "</b> our team receipt of goods for Reception number: <b>" + receptionNbr + "</b>. After couple of minutes goods should be located in proper warehouse areas and available to proceed", "Reception " + receptionNbr, filePath);

            }
        } else {
            System.out.println(path + " is empty, cannot send the email");
            log.debug(path + " is empty, cannot send the email");
        }
    }


    @Override
    public Long lastReception() {
        return receptionRepository.lastReception();
    }

    @Override
    public Long nextPalletNbr() {
        return receptionRepository.nextPalletNbr();
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
    public void closeCreation(Long id) {
        Reception reception = receptionRepository.getOne(id);
        reception.setCreation_closed(true);
        Long receptionNmbr = receptionRepository.getOne(id).getReceptionNumber();
        receptionRepository.updateCloseCreationValue(receptionNmbr);
    }

    @Override
    public void openCreation(Long id) {
        Reception reception = receptionRepository.getOne(id);
        reception.setCreation_closed(false);
        receptionRepository.save(reception);
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
                if (values.isCreation_closed() && !values.isFinished()) {
                    fileWriter.append("Reception status: Closed - Goods are on reception locations and checking is started" + "\n");
                    fileWriter.append("Closed:" + values.getLast_update() + "\n");
                    fileWriter.append("Closed by:" + values.getChangeBy() + "\n");
                } else if (values.isFinished()) {
                    fileWriter.append("Reception status: Finished - Goods were counted properly and starts be available on stock" + "\n");
                    fileWriter.append("Finished:" + values.getLast_update() + "\n");
                    fileWriter.append("Finished by:" + values.getChangeBy() + "\n");
                } else if (!values.isCreation_closed() && !values.isFinished()) {
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
//                    System.out.println("Linia: " + counter);
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
//                            System.out.println("Numer Artykułu: " + articleNbr);
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
                        if (stockRepository.checkIfHdNumberExistsOnStock(Long.parseLong(handleDeviceNbr)) < 1 && receptionRepository.checkIfHdNumberExistsOnReception(Long.parseLong(handleDeviceNbr))<1) {
                            reception.setHd_number(Long.parseLong(handleDeviceNbr));
//                            System.out.println("Numer Palety: " + handleDeviceNbr);
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
//                        System.out.println("Ilość sztuk: " + piecesQuantity);
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
//                            System.out.println("Nazwa dostawcy: " + vendorName);
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
//                            System.out.println("Jednostka: " + unit);
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
//                            System.out.println("Firma: " + company);
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
//                            System.out.println("Magazyn: " + warehouse);
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
//                        System.out.println("Jakość: " + quality);
                        reception.setQuality(quality);
                        log.debug("Quality: " + quality + " is ok");
                    } else if (!value.contains("Quality:") && round == 9 ) {
                        log.error("Line: " + counter + " Lack information about Quality in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company, FromWarehouse");
                        issueLog = issueLog + "\n Line: " + counter + " Lack information about Quality in file or is not in proper place of file structure ( after Reception_Number, ArticleNumber, HandleDeviceNumber, PiecesQuantity, VendorName, Unit, Company, FromWarehouse )" + LocalDateTime.now();
                    }
                    if(round == 10){
                        System.out.println("end");
                    }
                }

                List<Status> statusList = statusService.getStatus();
                reception.setStatus(statusList.get(0));
                reception.setChangeBy(SecurityUtils.usernameForActivations());
                reception.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                reception.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                if (issueLog.equals("")) {
                    add(reception);
                    Transaction transaction = new Transaction();
                    transaction.setWarehouse(reception.getWarehouse());
                    transaction.setCompany(reception.getCompany());
                    transaction.setCreated(LocalDateTime.now().toString());
                    transaction.setCreatedBy(SecurityUtils.usernameForActivations());
                    transaction.setTransactionType("121");
                    transaction.setTransactionGroup("Reception");
                    transaction.setTransactionDescription("Create Reception from File");
                    transaction.setAdditionalInformation("Reception number: " + reception.getReceptionNumber() + " created");
                    transactionService.add(transaction);

                    String fileName = fsFile.toPath().getFileName().toString();
                    Path passedPath = Paths.get("input/receptions/passed/Passed-" + TimeUtils.timeNowShort() + fileName);
                    Files.copy(fsFile.toPath(), passedPath, StandardCopyOption.REPLACE_EXISTING);
                    log.debug("Reception created. File transferred to directory passed. Transaction Log 121 Created");

                } else {

                    String fileName = fsFile.toPath().getFileName().toString();
                    Path errorPath = Paths.get("input/receptions/errors/Error-" + TimeUtils.timeNowShort() + "-" + fileName);

                    //create file with issueLog
                    File errorFile = new File("input/receptions/errors/IssueLog-" + TimeUtils.timeNowShort() + "-" + fileName);
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
                    issuelog.setIssueLogFileName(fileName);
                    issuelog.setCreatedBy(SecurityUtils.usernameForActivations());
                    issuelog.setWarehouse(reception.getWarehouse());
                    issuelog.setIssueLogFilePath(errorFile.toString());
                    if(reception.getCompany().getName() == null ) {
                        issuelog.setAdditionalInformation("");
                    }
                    else{
                        issuelog.setAdditionalInformation("Company: " + reception.getCompany().getName() + ", Reception number: " + reception.getReceptionNumber());
                    }

                    issueLogService.add(issuelog);

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

}
