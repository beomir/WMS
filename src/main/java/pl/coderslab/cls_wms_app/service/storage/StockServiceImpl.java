package pl.coderslab.cls_wms_app.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.EmailRecipientsRepository;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

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
    private final ReceptionRepository receptionRepository;
    private final TransactionService transactionService;
    private CustomerUserDetailsService customerUserDetailsService;

    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ReceptionRepository receptionRepository, TransactionService transactionService, CustomerUserDetailsService customerUserDetailsService) {
        this.stockRepository = stockRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.receptionRepository = receptionRepository;
        this.transactionService = transactionService;
        this.customerUserDetailsService = customerUserDetailsService;
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
    public void addNewStock(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Manual stock creation");
        transaction.setAdditionalInformation("New stock line created: Article: " + stock.getArticle().getArticle_number() + ", HD number:" + stock.getHd_number() + ", Qty: " + stock.getPieces_qty());
        transaction.setTransactionType("209");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeStatus(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Status changed on stock");
        transaction.setAdditionalInformation("Status changed from: " + customerUserDetailsService.chosenStockPosition.getStatus().getStatus() + " on: " + stock.getStatus().getStatus() + " for article: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("201");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeArticleNumber(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Article number changed on stock");
        transaction.setAdditionalInformation("Article number changed from: " + customerUserDetailsService.chosenStockPosition.getArticle().getArticle_number()  + " on: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("202");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeQty(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quantity changed on stock");
        transaction.setAdditionalInformation("Quantity changed from: " + customerUserDetailsService.chosenStockPosition.getPieces_qty()  + " on: " + stock.getPieces_qty() + " for article: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("203");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeQuality(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quality changed on stock");
        transaction.setAdditionalInformation("Quality changed from: " + customerUserDetailsService.chosenStockPosition.getQuality()  + " on: " + stock.getQuality() + " for article: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("204");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeUnit(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Unit changed on stock");
        transaction.setAdditionalInformation("Unit changed from: " + customerUserDetailsService.chosenStockPosition.getUnit().getName()  + " on: " + stock.getUnit().getName() + " for article: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("205");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeComment(Stock stock) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Comment changed");
        transaction.setAdditionalInformation("Comment changed from: " + customerUserDetailsService.chosenStockPosition.getComment()  + " on: " + stock.getComment() + " for article: " + stock.getArticle().getArticle_number());
        transaction.setTransactionType("206");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

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
            List<Stock> stockForCompany = stockRepository.getStockByCompanyName(company);
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


    static void transactionStock(Stock stock, Transaction transaction, ReceptionRepository receptionRepository) {
        transaction.setTransactionGroup("Stock");
        transaction.setReceptionNumber(stock.getReceptionNumber());
        transaction.setArticle(stock.getArticle().getArticle_number());
        transaction.setQuality(stock.getQuality());
        transaction.setUnit(stock.getUnit().getName());
        if(stock.getReceptionNumber() != null){
            transaction.setVendor(receptionRepository.getOneReceptionByReceptionNumber(stock.getReceptionNumber()).getVendor().getName());
        }
        transaction.setQuantity(stock.getPieces_qty());
        transaction.setHdNumber(stock.getHd_number());
        transaction.setCreated(stock.getCreated());
        transaction.setCreatedBy(SecurityUtils.usernameForActivations());
        transaction.setCompany(stock.getCompany());
        transaction.setWarehouse(stock.getWarehouse());
    }

    @Override
    public void remove(Long id) {
        stockRepository.deleteById(id);
    }

}

