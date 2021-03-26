package pl.coderslab.cls_wms_app.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class StockServiceImpl implements StockService {
    private final StockRepository stockRepository;
    private final LocationRepository locationRepository;
    private final SendEmailService sendEmailService;
    private final EmailRecipientsRepository emailRecipientsRepository;
    private final ReceptionRepository receptionRepository;
    private final StatusRepository statusRepository;
    private final UnitRepository unitRepository;
    private final ArticleRepository articleRepository;
    private final TransactionService transactionService;
    private final WarehouseRepository warehouseRepository;
    private final CompanyRepository companyRepository;
    private CustomerUserDetailsService customerUserDetailsService;
    public String locationName;
    private IssueLogService issueLogService;

    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, LocationRepository locationRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ReceptionRepository receptionRepository, StatusRepository statusRepository, UnitRepository unitRepository, ArticleRepository articleRepository, TransactionService transactionService, WarehouseRepository warehouseRepository, CompanyRepository companyRepository, CustomerUserDetailsService customerUserDetailsService, IssueLogService issueLogService) {
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.receptionRepository = receptionRepository;
        this.statusRepository = statusRepository;
        this.unitRepository = unitRepository;
        this.articleRepository = articleRepository;
        this.transactionService = transactionService;
        this.warehouseRepository = warehouseRepository;
        this.companyRepository = companyRepository;
        this.customerUserDetailsService = customerUserDetailsService;
        this.issueLogService = issueLogService;
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
    public void addNewStock(Stock stock,String locationNames) {

        Location location = locationRepository.findLocationByLocationName(locationNames);
        stock.setLocation(location);
        location.setFreeSpace(location.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
        location.setFreeWeight(location.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Manual stock creation");
        transaction.setAdditionalInformation("New stock line created: Article: " + stock.getArticle().getArticle_number() + ", HD number:" + stock.getHd_number() + ", Qty: " + stock.getPieces_qty() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("209");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
        locationRepository.save(location);
    }

    @Override
    public void changeStatus(Stock stock, ChosenStockPositional chosenStockPositional) {
        log.error("SERVICE chosenStockPosition: " + chosenStockPositional.statusId  );
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Status changed on stock");
        transaction.setAdditionalInformation("Status changed from: " + statusRepository.getStatusById(chosenStockPositional.statusId).getStatus()  + " on: " + stock.getStatus().getStatus() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("201");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);

        stockRepository.save(stock);
    }

    @Override
    public void changeArticleNumber(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Article number changed on stock");
        transaction.setAdditionalInformation("Article number changed from: " + articleRepository.getOne(chosenStockPositional.articleId).getArticle_number()  + " on: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("202");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName());
        location.setFreeSpace(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty());
        location.setFreeWeight(location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
        locationRepository.save(location);
        stockRepository.save(stock);
    }

    @Override
    public void changeQty(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quantity changed on stock");
        transaction.setAdditionalInformation("Quantity changed from: " + chosenStockPositional.pieces_qtyObj  + " on: " + stock.getPieces_qty() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("203");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName());
        location.setFreeSpace(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty());
        location.setFreeWeight(location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
        stockRepository.save(stock);
        locationRepository.save(location);
    }

    @Override
    public void changeQuality(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quality changed on stock");
        transaction.setAdditionalInformation("Quality changed from: " + chosenStockPositional.qualityObj  + " on: " + stock.getQuality() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("204");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
    }

    @Override
    public void changeUnit(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Unit changed on stock");
        transaction.setAdditionalInformation("Unit changed from: " + unitRepository.getOne(chosenStockPositional.getUnitId()).getName()  + " on: " + stock.getUnit().getName() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("205");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
    }

    @Override
    public void changeComment(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Comment changed");
        transaction.setAdditionalInformation("Comment changed from: " + chosenStockPositional.getCommentObj()  + " on: " + stock.getComment() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
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
        stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        stock.setChangeBy(SecurityUtils.usernameForActivations());
    }

    @Override
    public void remove(Long id) {
        stockRepository.deleteById(id);
    }

    @Override
    public void transfer(Stock stock, String locationNames, ChosenStockPositional chosenStockPositional) {
        log.error("stock.getPieces_qty(): " + stock.getPieces_qty());
        log.error("chosenStockPositional.pieces_qtyObj: " +chosenStockPositional.pieces_qtyObj);
        if(stock.getPieces_qty() < chosenStockPositional.pieces_qtyObj){
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames);
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock splittedPallet = new Stock();
            splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setQuality(chosenStockPositional.getQualityObj());
            splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
            splittedPallet.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            splittedPallet.setReceptionNumber(chosenStockPositional.receptionNumberObj);
            splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames));
            splittedPallet.setArticle(stock.getArticle());
            splittedPallet.setWarehouse(warehouseRepository.getOne(chosenStockPositional.getWarehouseId()));
            splittedPallet.setCompany(companyRepository.getOne(chosenStockPositional.getCompanyId()));
            splittedPallet.setHd_number(stock.getHd_number());
            splittedPallet.setPieces_qty(stock.getPieces_qty());
            splittedPallet.setStatus(statusRepository.getOne(chosenStockPositional.getStatusId()));
            splittedPallet.setComment(chosenStockPositional.commentObj);
            stockRepository.save(splittedPallet);
            locationRepository.save(locationForSplittedPallet);

            Location locationForTheRemainingQuantity = locationRepository.getOne(chosenStockPositional.locationId);
            Long remainingQuantity = chosenStockPositional.pieces_qtyObj - stock.getPieces_qty();
            locationForTheRemainingQuantity.setFreeSpace(locationForTheRemainingQuantity.getFreeSpace() + stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForTheRemainingQuantity.setFreeWeight(locationForTheRemainingQuantity.getFreeWeight() + stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock remainingStockInLocation = stockRepository.getOne(chosenStockPositional.getIdObj());

            remainingStockInLocation.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            remainingStockInLocation.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            remainingStockInLocation.setQuality(chosenStockPositional.getQualityObj());
            remainingStockInLocation.setChangeBy(SecurityUtils.usernameForActivations());
            remainingStockInLocation.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            remainingStockInLocation.setReceptionNumber(chosenStockPositional.receptionNumberObj);
            remainingStockInLocation.setLocation(locationForTheRemainingQuantity);
            remainingStockInLocation.setArticle(stock.getArticle());
            remainingStockInLocation.setWarehouse(warehouseRepository.getOne(chosenStockPositional.getWarehouseId()));
            remainingStockInLocation.setCompany(companyRepository.getOne(chosenStockPositional.getCompanyId()));
            remainingStockInLocation.setHd_number(remainingStockInLocation.getHd_number());
            remainingStockInLocation.setPieces_qty(remainingQuantity);
            remainingStockInLocation.setStatus(statusRepository.getOne(chosenStockPositional.getStatusId()));
            remainingStockInLocation.setComment(chosenStockPositional.commentObj);
            stockRepository.save(remainingStockInLocation);
            locationRepository.save(locationForTheRemainingQuantity);
        }
        else if(stock.getPieces_qty() == chosenStockPositional.pieces_qtyObj){
            Location location = locationRepository.findLocationByLocationName(locationNames);
            Location remainingLocation = locationRepository.getOne(chosenStockPositional.locationId);
            try {
                Stock stockInDestinationLocation = stockRepository.getStockByLocationName(locationNames);
                log.error("stockInDestinationLocation.getHd_number(): " + stockInDestinationLocation.getHd_number());
                log.error("stock.getHd_number(): " + stock.getHd_number());
                if(stockInDestinationLocation.getHd_number().equals(stock.getHd_number())){
                    stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
                    log.error("Qty cumulated for one stock id");
                    stockRepository.save(stockInDestinationLocation);
                    stockRepository.delete(stock);
                }
                if(!stockInDestinationLocation.getHd_number().equals(stock.getHd_number())){
                    stock.setLocation(location);
                    stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()) );
                    stock.setCreated(chosenStockPositional.getCreatedObj());
                    stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    stock.setChangeBy(SecurityUtils.usernameForActivations());
                    stock.setQuality(chosenStockPositional.getQualityObj());
                    stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
                    log.error("Transfer done for separate pallet numbers");
                    stockRepository.save(stock);
                }
            }
            catch (NullPointerException e) {
                stock.setLocation(location);
                stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()) );
                stock.setCreated(chosenStockPositional.getCreatedObj());
                stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                stock.setChangeBy(SecurityUtils.usernameForActivations());
                stock.setQuality(chosenStockPositional.getQualityObj());
                stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
                log.error("Transfer done for separate pallet numbers");
                stockRepository.save(stock);
            }
            remainingLocation.setFreeSpace(remainingLocation.getFreeSpace() + stock.getArticle().getVolume() * stock.getPieces_qty());
            remainingLocation.setFreeWeight(remainingLocation.getFreeWeight() + stock.getArticle().getWeight() * stock.getPieces_qty());

            location.setFreeSpace(location.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            location.setFreeWeight(location.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            locationRepository.save(location);
            locationRepository.save(remainingLocation);

            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Transfer stock");
            transaction.setAdditionalInformation("Transfer stock from location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName()  +  "Article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number()  + ", HD number:" + chosenStockPositional.getHd_numberObj() + ", Qty: " + chosenStockPositional.getPieces_qtyObj() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames).getLocationName());
            transaction.setTransactionType("210");
            transaction.setTransactionGroup("Stock");
            transaction.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
            transaction.setArticle(stock.getArticle().getArticle_number());
            transaction.setQuality(chosenStockPositional.getQualityObj());
            transaction.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()).getName());
            if(stock.getReceptionNumber() != null){
                transaction.setVendor(receptionRepository.getOneReceptionByReceptionNumber(stock.getReceptionNumber()).getVendor().getName());
            }
            transaction.setQuantity(stock.getPieces_qty());
            transaction.setHdNumber(stock.getHd_number());
            transaction.setCreated(stock.getCreated());
            transaction.setCreatedBy(SecurityUtils.usernameForActivations());
            transaction.setCompany(stock.getCompany());
            transaction.setWarehouse(stock.getWarehouse());
            transactionService.add(transaction);
        }
        else{
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Requested qty to send: " + stock.getPieces_qty() + ", bigger than on pallet: " + chosenStockPositional.pieces_qtyObj);
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("Requested qty to send: " + stock.getPieces_qty() + ", bigger than on pallet: " + chosenStockPositional.pieces_qtyObj);
            issueLogService.add(issueLog);
        }

    }
}

