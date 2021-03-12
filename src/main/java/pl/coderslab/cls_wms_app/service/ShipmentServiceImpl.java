package pl.coderslab.cls_wms_app.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
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


    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ShipmentInCreationRepository shipmentInCreationRepository, SchedulerRepository schedulerRepository, StockService stockService, StockRepository stockRepository) {
        this.shipmentRepository = shipmentRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.shipmentInCreationRepository = shipmentInCreationRepository;
        this.schedulerRepository = schedulerRepository;
        this.stockService = stockService;
        this.stockRepository = stockRepository;
    }

    @Override
    public void add(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public List<Shipment> getShipment(Long id, String username) {
        return shipmentRepository.getShipment(id, username);
    }

    @Override
    public List<Shipment> getShipments() {
        return shipmentRepository.getShipmenta();
    }

    @Override
    public Shipment findById(Long id) {
        return shipmentRepository.getOne(id);
    }

    @Override
    public void finishShipment(Long shipmentNbrtoFinish)  {
        List<Shipment> finishedShipment = shipmentRepository.getShipmentByShipmentNumber(shipmentNbrtoFinish);
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForShipmentType(shipmentRepository.getConmpanyNameByShipmentNumber(shipmentNbrtoFinish),"Shipment");
        String shipmentNbr = shipmentNbrtoFinish.toString();
        String warehouse = shipmentRepository.getWarehouseByShipmentNumber(shipmentNbrtoFinish);
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

        for(Stock stockToRemove : stockRepository.getStockListByShipmentNumber(shipmentNbrtoFinish)) {
            stockService.remove(stockToRemove.getId());
        }
    }

    @Override
    public int checkHowManyNotfinishedShipments(Long id, String username) {
        return shipmentRepository.checkHowManyNotfinishedShipments(id, username);
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



}
