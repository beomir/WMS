package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;


import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {
    private final ShipmentRepository shipmentRepository;

    private final SendEmailService sendEmailService;
    private final EmailRecipientsRepository emailRecipientsRepository;
    private final ShipmentInCreationRepository shipmentInCreationRepository;
    private final SchedulerRepository schedulerRepository;
    private final StockService stockService;
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final LocationService locationService;
    private final StatusRepository statusRepository;
    private final TransactionService transactionService;
    private final CompanyRepository companyRepository;


    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ShipmentInCreationRepository shipmentInCreationRepository, SchedulerRepository schedulerRepository, StockService stockService, StockRepository stockRepository, LocationRepository locationRepository, LocationService locationService, StatusRepository statusRepository, TransactionService transactionService, CompanyRepository companyRepository) {
        this.shipmentRepository = shipmentRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.shipmentInCreationRepository = shipmentInCreationRepository;
        this.schedulerRepository = schedulerRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.locationService = locationService;
        this.statusRepository = statusRepository;
        this.transactionService = transactionService;
        this.companyRepository = companyRepository;
    }

    @Override
    public void add(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public void addComment(Shipment shipment, String newComment, HttpSession session) {
        shipment.setComment(newComment);
        shipmentRepository.save(shipment);
        session.setAttribute("shipmentMessage","New comment: \"" + newComment + "\", added" );
    }

    @Override
    public List<Shipment> getShipments() {
        return shipmentRepository.getShipments();
    }

    @Override
    public Shipment findById(Long id) {
        return shipmentRepository.getOne(id);
    }

    @Override
    public List<Shipment> getShipmentsForLoggedUser(String companyName, String username) {
        return shipmentRepository.getShipmentsForLoggedUser(companyName, username);
    }

    @Override
    public List<Shipment> getShipmentsForLoggedUserByShipmentNumber(String warehouseName, String username, Long shipmentNumber) {
        return shipmentRepository.getShipmentsForLoggedUserByShipmentNumber(warehouseName, username,shipmentNumber);
    }

    @Override
    public void finishShipment(Long shipmentNbrToFinish)  {
        List<Shipment> finishedShipment = shipmentRepository.getShipmentByShipmentNumber(shipmentNbrToFinish);
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForShipmentType(shipmentRepository.getCompanyNameByShipmentNumber(shipmentNbrToFinish),"Shipment");
        String shipmentNbr = shipmentNbrToFinish.toString();
        String warehouse = shipmentRepository.getWarehouseByShipmentNumber(shipmentNbrToFinish);
        File shipment = new File("outbound/outbound" + shipmentNbr + ".txt");
        while (shipment.exists()) {
            int random = new Random().nextInt(100);
            shipment = new File("outbound/outbound" + shipmentNbr + "duplicateNbr" + random + ".txt");
        }
        try (FileWriter fileWriter = new FileWriter(shipment, true)) {
            fileWriter.append("Shipment_Number:" + shipmentNbr + "\n");
            for (Shipment value : finishedShipment)
            {
                fileWriter.append("ArticleNumber:"+value.getArticle().getArticle_number().toString()+",");
                fileWriter.append("ArticleDescription:"+value.getArticle().getArticle_desc()+ ",");
                fileWriter.append("HandleDeviceNumber:"+value.getHd_number().toString()+ ",");
                fileWriter.append("PiecesQuantity"+value.getPieces_qty().toString()+ ",");
                fileWriter.append("CustomerName"+value.getCustomer().getName()+ ",").append("CustomerAddress:"+value.getCustomer().getCity()+","+value.getCustomer().getStreet()+","+value.getCustomer().getCountry());
                fileWriter.append("Unit:"+value.getUnit().getName()+ ",");
                fileWriter.append("Company:"+value.getCompany().getName()+ ",");
                fileWriter.append("FromWarehouse:"+value.getWarehouse().getName()+ ",");
                fileWriter.append("ChangedBy:"+value.getChangeBy()+ "\n");
            }

        } catch (IOException ex) {
            log.debug("Cannot save a file" + shipment);
        }
        String filePath = String.valueOf(shipment);
        Path path = Paths.get(filePath);
        if(Files.exists(path)) {
            for (EmailRecipients value : mailGroup) {
                sendEmailService.sendEmail(value.getEmail(), "Dear client,<br/><br/> Warehouse: <b>" + warehouse + "</b> prepared, Shipment number: <b>" + shipmentNbr + "</b>. Truck is loading now. After couple of minutes we will send your goods", "Shipment " + shipmentNbr, filePath);
            }
        }
        else{
            log.debug(path + " is empty, cannot send the email");
        }
//old logic
//        Change shipment status on finished
//        shipmentRepository.updateFinishedShipmentValue(shipmentNbrtoFinish);
//        shipmentRepository.getShipmentByShipmentNumber(shipmentNbrtoFinish).setFinished(true);

//        update stock after finish shipment
//        shipmentRepository.deleteStockAfterFinishShipment(shipmentNbrtoFinish);
//        stockService.remove(stockRepository.getStockListByShipmentNumber(shipmentNbrtoFinish).getId());

        for(Shipment shipmentsToFinish : finishedShipment){
            shipmentsToFinish.setFinished(true);
        }

        for(Stock stockToRemove : stockRepository.getStockListByShipmentNumber(shipmentNbrToFinish)) {
            stockService.remove(stockToRemove.getId());
        }
    }

    @Override
    public int checkHowManyNotFinishedShipments(String warehouseName, String username) {
        return shipmentRepository.checkHowManyNotFinishedShipments(warehouseName, username);
    }

    @Override
    public Map<String, Integer> surveyMap(Long id, String username) {
        return shipmentRepository.surveyMap(id, username);
    }

    @Override
    public void sentShipments(String company) {
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForStockType("%Shipment%", company);
        Scheduler scheduler = schedulerRepository.getOneSchedulerByCompanyName(company, "%Shipment%");
        LocalDate dateBack = LocalDate.now().minusDays(scheduler.getHowManyDaysBack());
        List<Shipment> shipmentForCompany = shipmentRepository.getShipmentsFromXDayBack(String.valueOf(dateBack), company);
        List<ShipmentInCreation> shipmentInCreationForCompany = shipmentInCreationRepository.getShipmentsInCreationFromXDayBack(String.valueOf(dateBack), company);

        File shipments = new File("shipments/shipmentsFor" + company + "From" + dateBack + "To" + LocalDate.now() + ".txt");
        while (shipments.exists()) {
            int random = new Random().nextInt(100);
            shipments = new File("shipments/shipmentsFor" + company + "From" + dateBack + "To" + LocalDate.now() + random + ".txt");
        }
        try (FileWriter fileWriter = new FileWriter(shipments, true)) {
            fileWriter.append("Shipments for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now() + "\n" + "\n");
            for (Shipment values : shipmentForCompany) {
                if(values.isCreation_closed() && !values.isFinished())
                {
                    fileWriter.append("\n" +"Shipment durring preperation:" + values.getShipmentNumber().toString() + "\n");
                    fileWriter.append("\n" +"Preperations started:" + values.getCreated() + "\n");
                    fileWriter.append("Article Number:" + values.getArticle().getArticle_number().toString() + "\n");
                    fileWriter.append("Article Description:" + values.getArticle().getArticle_desc() + "\n");
                    fileWriter.append("Handle Device Number:" + values.getHd_number().toString() + "\n");
                    fileWriter.append("Pieces Quantity:" + values.getPieces_qty().toString() + "\n");
                    fileWriter.append("Vendor Name:" + values.getCustomer().getName() + ", ").append("Vendor Address:" + values.getCustomer().getCity() + ", " + values.getCustomer().getStreet() + ", " + values.getCustomer().getCountry() + "\n");
                    fileWriter.append("Unit:" + values.getUnit().getName() + "\n");
                    fileWriter.append("Company:" + values.getCompany().getName() + "\n");
                    fileWriter.append("Warehouse:" + values.getWarehouse().getName() + "\n");
                }
                else if(values.isCreation_closed() && values.isFinished()){
                    fileWriter.append("\n" +"Closed shipment number:" + values.getShipmentNumber().toString() + "\n");
                    fileWriter.append("\n" +"Shipment closed:" + values.getLast_update() + "\n");
                    fileWriter.append("Article Number:" + values.getArticle().getArticle_number().toString() + "\n");
                    fileWriter.append("Article Description:" + values.getArticle().getArticle_desc() + "\n");
                    fileWriter.append("Handle Device Number:" + values.getHd_number().toString() + "\n");
                    fileWriter.append("Pieces Quantity:" + values.getPieces_qty().toString() + "\n");
                    fileWriter.append("Vendor Name:" + values.getCustomer().getName() + ", ").append("Vendor Address:" + values.getCustomer().getCity() + ", " + values.getCustomer().getStreet() + ", " + values.getCustomer().getCountry() + "\n");
                    fileWriter.append("Unit:" + values.getUnit().getName() + "\n");
                    fileWriter.append("Company:" + values.getCompany().getName() + "\n");
                    fileWriter.append("Warehouse:" + values.getWarehouse().getName() + "\n");
                }

            }
            for (ShipmentInCreation values : shipmentInCreationForCompany){
                fileWriter.append("\n" + "Shipment durring creation:" + values.getShipmentNumber().toString() + "\n");
                fileWriter.append("\n" +"Shipment created:" + values.getCreated() + "\n");
                fileWriter.append("Article Number:" + values.getArticle().getArticle_number().toString() + "\n");
                fileWriter.append("Article Description:" + values.getArticle().getArticle_desc() + "\n");
                fileWriter.append("Pieces Quantity:" + values.getPieces_qty().toString() + "\n");
                fileWriter.append("Vendor Name:" + values.getCustomer().getName() + ", ").append("Vendor Address:" + values.getCustomer().getCity() + ", " + values.getCustomer().getStreet() + ", " + values.getCustomer().getCountry() + "\n");
                fileWriter.append("Unit:" + values.getUnit().getName() + "\n");
                fileWriter.append("Company:" + values.getCompany().getName() + "\n");
                fileWriter.append("Warehouse:" + values.getWarehouse().getName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filePath = String.valueOf(shipments);
        Path path = Paths.get(filePath);
        if(Files.exists(path)) {
            for (EmailRecipients valuess : mailGroup) {
                sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> shimpents for Company: <b>" + company + "</b> from: " + dateBack + ", to:" + LocalDate.now() + ". Data in attachment", "Shipments for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now(), filePath);
            }
        }
        else{
            log.debug(path + " is empty, cannot send the email");
        }

    }

    @Override
    public void assignDoorLocationToShipment(Long shipmentNumber, Long doorLocation, HttpSession session,String warehouseName) {
        log.error("shipmentNumber: " + shipmentNumber);
        log.error("doorLocation: " + doorLocation);
        boolean enoughCapacity = false;
        List<ShipmentInCreation> shipmentsInCreation = shipmentInCreationRepository.getShipmentInCreationByShipmentNumberAndUserNameAndWarehouseName(shipmentNumber,warehouseName,SecurityUtils.usernameForActivations());
        log.error("shipmentsList: " + shipmentsInCreation);
        Location location = locationRepository.getOne(doorLocation);
        Transaction transaction = new Transaction();
        log.error("location for shipment: " + location.getLocationName());
        for (ShipmentInCreation singularShipment: shipmentsInCreation) {
            if(locationService.reduceTheAvailableContentOfTheLocation(location.getLocationName(),singularShipment.getArticle().getArticle_number(),singularShipment.getPieces_qty(),singularShipment.getWarehouse().getName(),singularShipment.getCompany().getName())){
                singularShipment.setStatus(statusRepository.getStatusByStatusName("picking_pending","Shipment"));
                singularShipment.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                singularShipment.setLocation(location);
                shipmentInCreationRepository.save(singularShipment);
                transaction.setAdditionalInformation("Assign shipment: " + singularShipment + " to dock door: " + location.getLocationName());
                transaction.setReceptionStatus(singularShipment.getStatus().getStatus());
                saveTransactionModel(singularShipment, transaction);
                enoughCapacity = true;
                log.error("1.Enough space for shipment: " + shipmentNumber);
            }
            else{
                transaction.setAdditionalInformation("Assign shipment: " + shipmentNumber + " to dock door: " + location.getLocationName() + " unsuccessful because of not enough location free capacity");
                transaction.setReceptionStatus(singularShipment.getStatus().getStatus());
                enoughCapacity = false;
                log.error("1.Not enough space for shipment: " + shipmentNumber);
            }
        }
        if(enoughCapacity){
            transaction.setTransactionDescription("Reception assigned to reception dock door");
            session.setAttribute("shipmentMessage", "Shipment: " + shipmentNumber + " assigned to door: " + location.getLocationName());
            transaction.setTransactionType("118");
            transactionService.add(transaction);
            log.error("2.close creation service shipmentMessage: " + session.getAttribute("shipmentMessage"));
        }
        else{
            session.setAttribute("shipmentMessage", "Not enough space or free weight in location: " + location.getLocationName());
            log.error("2.Not enough space for shipment: " + shipmentNumber);
        }
    }

    @Override
    public List<ShipmentRepository.ShipmentViewObject> shipmentSummary(String shipmentCompany, String shipmentWarehouse,
                                                                       String shipmentCustomer, String shipmentStatus, String shipmentLocation,
                                                                       String shipmentShipmentNumber, String shipmentHdNumber,
                                                                       String shipmentCreatedFrom, String shipmentCreatedTo, String shipmentCreatedBy) {
        if(shipmentCompany == null || shipmentCompany.equals("")){
            shipmentCompany = companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName();
        }
        if(shipmentWarehouse == null || shipmentWarehouse.equals("")){
            shipmentWarehouse = "%";
        }
        if(shipmentCustomer == null || shipmentCustomer.equals("")){
            shipmentCustomer = "%" ;
        }
        if(shipmentStatus == null || shipmentStatus.equals("")){
            shipmentStatus = "%";
        }
        if( shipmentShipmentNumber == null || shipmentShipmentNumber.equals("")){
            shipmentShipmentNumber = "%";
        }
        if(shipmentHdNumber == null || shipmentHdNumber.equals("")){
            shipmentHdNumber = "%";
        }
        if(shipmentCreatedFrom == null || shipmentCreatedFrom.equals("")){
            shipmentCreatedFrom = "1970-01-01";
        }
        if(shipmentCreatedTo == null || shipmentCreatedTo.equals("")){
            shipmentCreatedTo = "2222-02-02";
        }
        if(shipmentCreatedBy == null || shipmentCreatedBy.equals("")){
            shipmentCreatedBy = "%" ;
        }
        if(shipmentLocation == null || shipmentLocation.equals("")){
            shipmentLocation = "%";
        }

        log.error(" createdBy: " + shipmentCreatedBy);
        log.error(" warehouse: " + shipmentWarehouse );
        log.error(" company: " + shipmentCompany );
        log.error(" customer: " + shipmentCustomer);
        log.error(" shipmentNumber: " + shipmentShipmentNumber);
        log.error(" hdNumber: " + shipmentHdNumber);
        log.error(" status: " + shipmentStatus);
        log.error(" location: " + shipmentLocation);
        log.error(" createdFrom: " + shipmentCreatedFrom);
        log.error(" createdTo: " + shipmentCreatedTo);

        return shipmentRepository.getShipmentSummary(shipmentCompany,shipmentWarehouse,shipmentCustomer,shipmentStatus,shipmentLocation,shipmentShipmentNumber,shipmentHdNumber,shipmentCreatedFrom,shipmentCreatedTo,shipmentCreatedBy);
    }

    private void saveTransactionModel(ShipmentInCreation shipment, Transaction transaction) {
        transaction.setTransactionGroup("Shipment");
        transaction.setCreated(shipment.getCreated());
        transaction.setCreatedBy(SecurityUtils.usernameForActivations());
        transaction.setCompany(shipment.getCompany());
        transaction.setWarehouse(shipment.getWarehouse());
        transaction.setReceptionNumber(shipment.getShipmentNumber());
        transaction.setArticle(shipment.getArticle().getArticle_number());
        transaction.setQuality(shipment.getQuality());
        transaction.setUnit(shipment.getUnit().getName());
        transaction.setVendor(shipment.getCustomer().getName());
        transaction.setQuantity(shipment.getPieces_qty());
        transaction.setHdNumber(0L);
    }


}
