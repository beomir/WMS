package pl.coderslab.cls_wms_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.EmailRecipientsRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;

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
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final SendEmailService sendEmailService;
    private final EmailRecipientsRepository emailRecipientsRepository;

    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository) {
        this.stockRepository = stockRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
    }

    @Override
    public List<Stock> getStorage(Long id, String username) {
        return stockRepository.getStorage(id, username);
    }

    @Override
    public List<Warehouse> getWarehouse(Long id) {
        return stockRepository.getWarehouse(id);
    }

    @Override
    public void add(Stock stock) {
        stockRepository.save(stock);
    }

    @Override
    public Stock get(Long id) {
        return stockRepository.getOne(id);
    }

    public void delete(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public void update(Stock stock) {
        stockRepository.save(stock);
    }


    @Override
    public Stock findById(Long id) {
        return stockRepository.getOne(id);
    }

    public void sendStock(String company) {
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForStockType("%Stock%",company);
        for(EmailRecipients value : mailGroup)
        {
            List<Stock> stockForCompany = stockRepository.getStockByCompanyName(value.getCompany().getName());
            String warehouse = "";
            for(Stock data : stockForCompany){
                warehouse = data.getWarehouse().getName();
            }
            File stock = new File("stock/stockFor" + company + LocalDate.now() + ".txt");
            while (stock.exists()) {
                int random = new Random().nextInt(100);
                stock = new File("stock/stockFor" + company + LocalDate.now() + random + ".txt");
            }
            try (FileWriter fileWriter = new FileWriter(stock, true)) {
                fileWriter.append("Stock for: " + company +"\n");
                for (Stock values : stockForCompany) {
                    fileWriter.append("ArticleNumber:" + values.getArticle().getArticle_number().toString() + ",");
                    fileWriter.append("ArticleDescription:" + values.getArticle().getArticle_desc() + ",");
                    fileWriter.append("HandleDeviceNumber:" + values.getHd_number().toString() + ",");
                    fileWriter.append("PiecesQuantity" + values.getPieces_qty().toString() + ",");
//                    fileWriter.append("VendorName" + values.getVendor().getName() + ",").append("VendorAddress:" + value.getVendor().getCity() + "," + value.getVendor().getStreet() + "," + value.getVendor().getCountry());
                    fileWriter.append("Unit:" + values.getUnit().getName() + ",");
                    fileWriter.append("Company:" + values.getCompany().getName() + ",");
                    fileWriter.append("FromWarehouse:" + values.getWarehouse().getName() + ",");
                    fileWriter.append("ChangedBy:" + values.getChangeBy() + "\n");

                }

            } catch (IOException ex) {
                log.debug("Cannot save a file" + stock);
            }
            String filePath = String.valueOf(stock);
            Path path = Paths.get(filePath);
            if(Files.exists(path)) {
                for (EmailRecipients valuess : mailGroup) {
                    if(warehouse.equals("")){
                        sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> For Company: <b>" + company + "</b> on: " + LocalDate.now() + ", " + String.valueOf(LocalTime.now()).substring(0,8) + ", we don't have goods in our Warehouses", "Stock for: " + company + " in our Warehouses", filePath);
                    }
                    else{
                        sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> goods for Company: <b>" + company + "</b> on: " + LocalDate.now() + ", " + String.valueOf(LocalTime.now()).substring(0,8) + ". Data in attachment", "Stock for: " + company + " in: " +warehouse, filePath);
                    }

                }
            }
            else{
                log.debug(path + " is empty, cannot send the email");
            }

        }

    }

}

