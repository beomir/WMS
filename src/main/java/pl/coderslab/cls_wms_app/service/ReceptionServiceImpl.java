package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.entity.Reception;
import lombok.extern.slf4j.Slf4j;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.repository.EmailRecipientsRepository;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;
import pl.coderslab.cls_wms_app.repository.SchedulerRepository;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Autowired
    public ReceptionServiceImpl(ReceptionRepository receptionRepository, EmailRecipientsRepository emailRecipientsRepository, SendEmailService sendEmailService, SchedulerRepository schedulerRepository) {
        this.receptionRepository = receptionRepository;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.sendEmailService = sendEmailService;
        this.schedulerRepository = schedulerRepository;
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
                fileWriter.append("Receptions for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now() + "\n");
                for (Reception values : receptionForCompany) {
                    fileWriter.append("Reception Number:" + values.getReceptionNumber().toString() + "\n" + "\n");
                    fileWriter.append("Article Number:" + values.getArticle().getArticle_number().toString() + "\n");
                    fileWriter.append("Article Description:" + values.getArticle().getArticle_desc() + "\n");
                    fileWriter.append("Handle Device Number:" + values.getHd_number().toString() + "\n");
                    fileWriter.append("Pieces Quantity:" + values.getPieces_qty().toString() + "\n");
                    fileWriter.append("Vendor Name:" + values.getVendor().getName() + ", ").append("Vendor Address:" + values.getVendor().getCity() + ", " + values.getVendor().getStreet() + ", " + values.getVendor().getCountry() + "\n");
                    fileWriter.append("Unit:" + values.getUnit().getName() + "\n");
                    fileWriter.append("Company:" + values.getCompany().getName() + "\n");
                    fileWriter.append("FromWarehouse:" + values.getWarehouse().getName() + "\n");
                    fileWriter.append("Closed:" + values.getLast_update() + "\n");
                    fileWriter.append("Closed by:" + values.getChangeBy() + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        String filePath = String.valueOf(receptions);
        Path path = Paths.get(filePath);
        if(Files.exists(path)) {
            for (EmailRecipients valuess : mailGroup) {
                    sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> receptions Company: <b>" + company + "</b> from: " + dateBack + ", to:" + LocalDate.now() + ". Data in attachment", "Receptions for: " + company + ", from: " + dateBack + ", to: " + LocalDate.now(), filePath);
            }
        }
        else{
            log.debug(path + " is empty, cannot send the email");
        }

    }
}
