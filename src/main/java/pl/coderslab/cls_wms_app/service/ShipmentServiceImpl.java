package pl.coderslab.cls_wms_app.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.repository.EmailRecipientsRepository;
import pl.coderslab.cls_wms_app.repository.ShipmentRepository;
import pl.coderslab.cls_wms_app.repository.UsersRepository;

import javax.mail.MessagingException;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Service
@Slf4j
public class ShipmentServiceImpl implements ShipmentService {
    private ShipmentRepository shipmentRepository;
    private UsersRepository usersRepository;
    private SendEmailService sendEmailService;
    private EmailRecipientsRepository emailRecipientsRepository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository, UsersRepository usersRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository) {
        this.shipmentRepository = shipmentRepository;
        this.usersRepository = usersRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
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
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForShipmentType(shipmentRepository.getOneShipmentByShipmentNumber(shipmentNbrtoFinish),"Shipment");
        String shipmentNbr = shipmentNbrtoFinish.toString();
        String warehouse = shipmentRepository.getWarehouseByShipmentNumber(shipmentNbrtoFinish);
        File shipment = new File("outbound/" + shipmentNbr + ".txt");
        while (shipment.exists()) {
            int random = new Random().nextInt(100);
            shipment = new File("outbound/" + shipmentNbr + "duplicateNbr" + random + ".txt");
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
                sendEmailService.sendEmail(value.getEmail(), "Dear client, Warehouse: " + warehouse + " prepared, Shipment number: " + shipmentNbr + ". Truck is loading now. After couple of minutes we will send your goods", "Shipment " + shipmentNbr, filePath);
            }
        }
        else{
            log.debug(path + " is empty, cannot send the email");
        }
        shipmentRepository.updateFinishedShipmentValue(shipmentNbrtoFinish);
        shipmentRepository.deleteStockAfterFinishShipment(shipmentNbrtoFinish);
    }

    @Override
    public int checkHowManyNotfinishedShipments(Long id, String username) {
        return shipmentRepository.checkHowManyNotfinishedShipments(id, username);
    }

    @Override
    public Map<String, Integer> surveyMap(Long id, String username) {
        return shipmentRepository.surveyMap(id, username);
    }


}
