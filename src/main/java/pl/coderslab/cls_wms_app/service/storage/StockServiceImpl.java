package pl.coderslab.cls_wms_app.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;

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
    public String locationName;
    private final IssueLogService issueLogService;
    private final WorkDetailsRepository workDetailsRepository;

    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, LocationRepository locationRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ReceptionRepository receptionRepository, StatusRepository statusRepository, UnitRepository unitRepository, ArticleRepository articleRepository, TransactionService transactionService, WarehouseRepository warehouseRepository, CompanyRepository companyRepository, IssueLogService issueLogService, WorkDetailsRepository workDetailsRepository) {
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
        this.issueLogService = issueLogService;
        this.workDetailsRepository = workDetailsRepository;
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
    public void addNewStock(Stock stock, String locationNames) {

        Location location = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
        stock.setLocation(location);
        location.setFreeSpace(location.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
        location.setFreeWeight(location.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Manual stock creation");
        transaction.setAdditionalInformation("New stock line created: Article: " + stock.getArticle().getArticle_number() + ", HD number:" + stock.getHd_number() + ", Qty: " + stock.getPieces_qty() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("309");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
        locationRepository.save(location);
    }

    @Override
    public void changeStatus(Stock stock, ChosenStockPositional chosenStockPositional) {
        try {
            if(statusRepository.checkIfStockStatusExists(stock.getStatus().getStatus()) != null){
                log.debug("SERVICE chosenStockPosition: " + chosenStockPositional.statusId);
                Transaction transaction = new Transaction();
                transaction.setTransactionDescription("Status changed on stock");
                transaction.setAdditionalInformation("Status changed from: " + statusRepository.getStatusById(chosenStockPositional.statusId).getStatus() + " on: " + stock.getStatus().getStatus() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
                transaction.setTransactionType("301");
                transactionStock(stock, transaction, receptionRepository);
                transactionService.add(transaction);
                stockRepository.save(stock);
            }
            else{
                log.error("Incorrect status set: " + stock.getStatus().getStatus());
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Incorrect status set: " + stock.getStatus().getStatus());
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(stock.getWarehouse());
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setAdditionalInformation("Attempt of set incorrect status: " + stock.getStatus().getStatus() + ", for hd number: " + stock.getHd_number() );
                issueLogService.add(issueLog);
            }
        } catch (NullPointerException e) {
            log.error("Status not exists in DB");
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Incorrect status set. Status not exists in DB");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setAdditionalInformation("Attempt of set incorrect status for hd number: " + stock.getHd_number() + ".Status not exists in DB  " );
            issueLogService.add(issueLog);
        }

    }

    @Override
    public void changeArticleNumber(Stock stock, ChosenStockPositional chosenStockPositional) {
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName(),stock.getWarehouse().getName());
        if (articleRepository.getOne(chosenStockPositional.articleId).getArticleTypes().getMixed().contains(stock.getArticle().getArticleTypes().getArticleClass()) && location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty() > 0 && location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty() > 0) {
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Article number changed on stock");
            transaction.setAdditionalInformation("Article number changed from: " + articleRepository.getOne(chosenStockPositional.articleId).getArticle_number() + " on: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
            transaction.setTransactionType("302");
            transactionStock(stock, transaction, receptionRepository);
            transactionService.add(transaction);
            location.setFreeSpace(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty());
            location.setFreeWeight(location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            locationRepository.save(location);
            stockRepository.save(stock);
        } else if (!articleRepository.getOne(chosenStockPositional.articleId).getArticleTypes().getMixed().contains(stock.getArticle().getArticleTypes().getArticleClass())) {
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Articles in location can't be mix.");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("Article: " + articleRepository.getOne(chosenStockPositional.articleId).getArticle_number() + ",have class: "+ articleRepository.getOne(chosenStockPositional.articleId).getArticleTypes().getArticleClass() + ", article: " + stock.getArticle().getArticle_number() + ", have class: " + stock.getArticle().getArticleTypes().getArticleClass());
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLogService.add(issueLog);
        }
        else if(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty() < 1){
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Location after change article, have not enough space");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("");
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLogService.add(issueLog);
        }
        else{
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Location after change article, is overweight");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("");
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLogService.add(issueLog);
        }

    }

    @Override
    public void changeQty(Stock stock, ChosenStockPositional chosenStockPositional) {
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName(),stock.getWarehouse().getName());
        if(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty() > 0 && location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty() > 0){
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Quantity changed on stock");
            transaction.setAdditionalInformation("Quantity changed from: " + chosenStockPositional.pieces_qtyObj + " on: " + stock.getPieces_qty() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
            transaction.setTransactionType("303");
            transactionStock(stock, transaction, receptionRepository);
            transactionService.add(transaction);
            location.setFreeSpace(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty());
            location.setFreeWeight(location.getMaxWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            stockRepository.save(stock);
            locationRepository.save(location);
        }
        else if(location.getVolume() - stock.getArticle().getVolume() * stock.getPieces_qty() < 1){
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Location after change quantity, have not enough space");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("");
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLogService.add(issueLog);
        }
        else{
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Location after change quantity, is overweight");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("");
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLogService.add(issueLog);
        }

    }

    @Override
    public void changeQuality(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quality changed on stock");
        transaction.setAdditionalInformation("Quality changed from: " + chosenStockPositional.qualityObj + " on: " + stock.getQuality() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("304");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
    }

    @Override
    public void changeUnit(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Unit changed on stock");
        transaction.setAdditionalInformation("Unit changed from: " + unitRepository.getOne(chosenStockPositional.getUnitId()).getName() + " on: " + stock.getUnit().getName() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("305");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stockRepository.save(stock);
    }

    @Override
    public void changeComment(Stock stock, ChosenStockPositional chosenStockPositional) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Comment changed");
        transaction.setAdditionalInformation("Comment changed from: " + chosenStockPositional.getCommentObj() + " on: " + stock.getComment() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("306");
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
        List<EmailRecipients> mailGroup = emailRecipientsRepository.getEmailRecipientsByCompanyForStockType("%Stock%", company);
        List<Stock> stockForCompany = stockRepository.getStockByCompanyName(company);
        String warehouse = "";
        for (Stock data : stockForCompany) {
            warehouse = data.getWarehouse().getName();
        }
        File stock = new File("stock/stockFor" + company + LocalDate.now() + ".txt");
        while (stock.exists()) {
            int random = new Random().nextInt(100);
            stock = new File("stock/stockFor" + company + LocalDate.now() + random + ".txt");
        }
        try (FileWriter fileWriter = new FileWriter(stock, true)) {
            fileWriter.append("Stock for: " + company + "\n");
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
        if (Files.exists(path)) {
            for (EmailRecipients valuess : mailGroup) {
                if (warehouse.equals("")) {
                    sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> For Company: <b>" + company + "</b> on: " + LocalDate.now() + ", " + String.valueOf(LocalTime.now()).substring(0, 8) + ", we don't have goods in our Warehouses", "Stock for: " + company + " in our Warehouses", filePath);
                } else {
                    sendEmailService.sendEmail(valuess.getEmail(), "Dear client,<br/><br/> goods for Company: <b>" + company + "</b> on: " + LocalDate.now() + ", " + String.valueOf(LocalTime.now()).substring(0, 8) + ". Data in attachment", "Stock for: " + company + " in: " + warehouse, filePath);
                }

            }
        } else {
            log.debug(path + " is empty, cannot send the email");
        }
    }

    static void transactionStock(Stock stock, Transaction transaction, ReceptionRepository receptionRepository) {
        transaction.setTransactionGroup("Stock");
        transaction.setReceptionNumber(stock.getReceptionNumber());
        transaction.setArticle(stock.getArticle().getArticle_number());
        transaction.setQuality(stock.getQuality());
        transaction.setUnit(stock.getUnit().getName());
        if (stock.getReceptionNumber() != null) {
            transaction.setVendor(receptionRepository.getVendorNameByReceptionNumber(stock.getReceptionNumber()));
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
        log.error("chosenStockPositional.pieces_qtyObj: " + chosenStockPositional.pieces_qtyObj);
        Transaction transaction = new Transaction();
        transaction.setTransactionGroup("Stock");
        transaction.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
        transaction.setArticle(stock.getArticle().getArticle_number());
        transaction.setQuality(chosenStockPositional.getQualityObj());
        transaction.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()).getName());
        if (stock.getReceptionNumber() != null) {
            transaction.setVendor(receptionRepository.getOneReceptionByReceptionNumber(stock.getReceptionNumber()).getVendor().getName());
        }
        transaction.setQuantity(stock.getPieces_qty());
        transaction.setHdNumber(stock.getHd_number());
        transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        transaction.setCreatedBy(SecurityUtils.usernameForActivations());
        transaction.setCompany(stock.getCompany());
        transaction.setWarehouse(stock.getWarehouse());

        //partial transfer
        if (stock.getPieces_qty() < chosenStockPositional.pieces_qtyObj) {
            try {
                if (stockRepository.getStockByHdNumber(stock.getHd_number()) != null) {
                    Stock stockInDestinationLocation = stockRepository.getStockByHdNumber(stock.getHd_number());
                    partialTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                    log.error("stockRepository.getStockByHdNumber(stock.getHd_number())");
                } else {
                    Stock stockInDestinationLocation = stockRepository.getStockByLocationName(locationNames);
                    partialTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                    log.error("stockRepository.getStockByLocationName(locationNames)");
                }

            } catch (NullPointerException e) {
                transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " destiny location was empty before transfer");
                transaction.setTransactionDescription("Transfer stock Partial: New pallet created, destiny location was empty before transfer");
                transaction.setTransactionType("317");
                transactionService.add(transaction);
                Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
                locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
                locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
                Stock splittedPallet = new Stock();
                splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                splittedPallet.setQuality(chosenStockPositional.getQualityObj());
                splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
                splittedPallet.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
                splittedPallet.setReceptionNumber(chosenStockPositional.receptionNumberObj);
                splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()));
                splittedPallet.setArticle(stock.getArticle());
                splittedPallet.setWarehouse(warehouseRepository.getOne(chosenStockPositional.getWarehouseId()));
                splittedPallet.setCompany(companyRepository.getOne(chosenStockPositional.getCompanyId()));
                splittedPallet.setHd_number(stock.getHd_number());
                splittedPallet.setPieces_qty(stock.getPieces_qty());
                splittedPallet.setStatus(statusRepository.getOne(chosenStockPositional.getStatusId()));
                splittedPallet.setComment(chosenStockPositional.commentObj);
                stockRepository.save(splittedPallet);
                locationRepository.save(locationForSplittedPallet);
            }

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
        //transfer all qty
        else if (stock.getPieces_qty() == chosenStockPositional.pieces_qtyObj) {
            Location location = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
            Location remainingLocation = locationRepository.getOne(chosenStockPositional.locationId);
            try {

                Stock stockInDestinationLocation = stockRepository.getStockByLocationName(locationNames);
                fullTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                log.error("stockRepository.getStockByLocationName(locationNames)");

            } catch (NullPointerException e) {
                stock.setLocation(location);
                stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
                stock.setCreated(chosenStockPositional.getCreatedObj());
                stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                stock.setChangeBy(SecurityUtils.usernameForActivations());
                stock.setQuality(chosenStockPositional.getQualityObj());
                stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
                log.error("Transfer to not occupied location");
                stockRepository.save(stock);
                transaction.setAdditionalInformation("Transfer stock full pallet: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " destiny location was empty before transfer");
                transaction.setTransactionDescription("Transfer stock full pallet: New pallet created, destiny location was empty before transfer");
                transaction.setTransactionType("318");
                transactionService.add(transaction);
            }
            remainingLocation.setFreeSpace(remainingLocation.getFreeSpace() + stock.getArticle().getVolume() * stock.getPieces_qty());
            remainingLocation.setFreeWeight(remainingLocation.getFreeWeight() + stock.getArticle().getWeight() * stock.getPieces_qty());

            location.setFreeSpace(location.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            location.setFreeWeight(location.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            locationRepository.save(location);
            locationRepository.save(remainingLocation);
        } else {
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



    void partialTransfer(Stock stock, String locationNames, ChosenStockPositional chosenStockPositional, Stock stockInDestinationLocation, Transaction transaction) {
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            log.error("Qty cumulated for one stock id, for partial transfer, the same article ( destination == original)");
            stockRepository.save(stockInDestinationLocation);
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number());
            transaction.setTransactionDescription("Transfer stock Partial: cumulate to destination HD with the same article");
            transaction.setTransactionType("311");
            transactionService.add(transaction);
        }

        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            log.error("Qty transfer to different pallet number, for partial transfer, different article ( destination != original)");
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock splittedPallet = new Stock();
            splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setQuality(chosenStockPositional.getQualityObj());
            splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
            splittedPallet.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            splittedPallet.setReceptionNumber(chosenStockPositional.receptionNumberObj);
            splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()));
            splittedPallet.setArticle(stock.getArticle());
            splittedPallet.setWarehouse(warehouseRepository.getOne(chosenStockPositional.getWarehouseId()));
            splittedPallet.setCompany(companyRepository.getOne(chosenStockPositional.getCompanyId()));
            splittedPallet.setHd_number(stock.getHd_number());
            splittedPallet.setPieces_qty(stock.getPieces_qty());
            splittedPallet.setStatus(statusRepository.getOne(chosenStockPositional.getStatusId()));
            splittedPallet.setComment(chosenStockPositional.commentObj);
            stockRepository.save(splittedPallet);
            locationRepository.save(locationForSplittedPallet);

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " remaining pieces stayed on origin location");
            transaction.setTransactionDescription("Transfer stock Partial: new pallet in destination location created, with different article");
            transaction.setTransactionType("312");
            transactionService.add(transaction);
        }
        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            log.error("Qty transfer to different pallet number, for partial transfer, the same article ( destination == original)");
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock splittedPallet = new Stock();
            splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setQuality(chosenStockPositional.getQualityObj());
            splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
            splittedPallet.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            splittedPallet.setReceptionNumber(chosenStockPositional.receptionNumberObj);
            splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()));
            splittedPallet.setArticle(stock.getArticle());
            splittedPallet.setWarehouse(warehouseRepository.getOne(chosenStockPositional.getWarehouseId()));
            splittedPallet.setCompany(companyRepository.getOne(chosenStockPositional.getCompanyId()));
            splittedPallet.setHd_number(stock.getHd_number());
            splittedPallet.setPieces_qty(stock.getPieces_qty());
            splittedPallet.setStatus(statusRepository.getOne(chosenStockPositional.getStatusId()));
            splittedPallet.setComment(chosenStockPositional.commentObj);
            stockRepository.save(splittedPallet);
            locationRepository.save(locationForSplittedPallet);

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " remaining pieces stayed on origin location");
            transaction.setTransactionDescription("Transfer stock Partial: new pallet in destination location created, with the same article");
            transaction.setTransactionType("313");
            transactionService.add(transaction);
        }
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            log.error("Qty cumulated for one stock id, for partial transfer, different article ( destination != original)");
            stockRepository.save(stockInDestinationLocation);
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number());
            transaction.setTransactionDescription("Transfer stock Partial: cumulate to destination HD with different article");
            transaction.setTransactionType("320");
            transactionService.add(transaction);
        }
    }


    void fullTransfer(Stock stock, String locationNames, ChosenStockPositional chosenStockPositional, Stock stockInDestinationLocation, Transaction transaction) {
        Location location = locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName());
        log.error("stockInDestinationLocation.getHd_number(): " + stockInDestinationLocation.getHd_number());
        log.error("stockInDestinationLocation.location: " + stockInDestinationLocation.getLocation().getLocationName());
        log.error("stock.getHd_number(): " + stock.getHd_number());
        //transfer all qty from origin pallet and cumulate to destination pallet - article on pallets are the same
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            stockRepository.save(stockInDestinationLocation);
            stockRepository.delete(stock);
            log.error("transfer all qty from origin pallet and cumulate to destination pallet - article on pallets are the same");

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + "Article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", HD number:" + chosenStockPositional.getHd_numberObj() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + ". Quantity after transfer on pallet: " + (stockInDestinationLocation.getPieces_qty()));
            transaction.setTransactionDescription("Transfer stock full pallet: cumulate all pieces from origin pallet to pallet in destination location. Article are the same");
            transaction.setTransactionType("314");
            transactionService.add(transaction);

        }
        //transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with desitnation location
        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            stock.setCreated(chosenStockPositional.getCreatedObj());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQualityObj());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
            log.error("transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with destination location");
            stockRepository.save(stock);

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " transferred pieces are kept on the same origin pallet number, same articles");
            transaction.setTransactionDescription("Transfer stock full pallet: origin pallet number kept, article in destiny location is the same like in origin location");
            transaction.setTransactionType("315");
            transactionService.add(transaction);
        }
        //transfer all qty from origin pallet and cumulate to destination pallet - articles are not the same
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            stock.setCreated(chosenStockPositional.getCreatedObj());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQualityObj());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
            log.error("transfer all qty from origin pallet and cumulate to destination pallet - articles are not the same");
            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " where are multiple items");
            transaction.setTransactionDescription("Transfer stock full pallet: cumulate all pieces from origin pallet to pallet in destination location. Article are different");
            transaction.setTransactionType("316");
            transactionService.add(transaction);
            stockRepository.save(stock);
        }

        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(unitRepository.getOne(chosenStockPositional.getUnitId()));
            stock.setCreated(chosenStockPositional.getCreatedObj());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQualityObj());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumberObj());
            log.error("transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with destination location");
            stockRepository.save(stock);

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_numberObj() + " in location: " + locationRepository.getOne(chosenStockPositional.locationId).getLocationName() + ", article: " + articleRepository.getOne(chosenStockPositional.getArticleId()).getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames,stock.getWarehouse().getName()).getLocationName() + " transferred pieces are kept on the same origin pallet number, different articles");
            transaction.setTransactionDescription("Transfer stock full pallet: origin pallet number kept, article in destiny location is different than this from origin location");
            transaction.setTransactionType("319");
            transactionService.add(transaction);
        }
    }


    @Override
    public void produceGoods(Long productionNumberToConfirm) throws CloneNotSupportedException {
        Stock finishProduct = new Stock();

        List<Stock> stockList = stockRepository.getStockByWorkHandleAndWorkDescription(workDetailsRepository.workDetailHandle(productionNumberToConfirm,"Producing finish product from collected intermediate articles"),"Production picking");
        log.debug("productionNumberToConfirm: " + productionNumberToConfirm);
        for (Stock intermediateGoods: stockList) {
            finishProduct = (Stock) intermediateGoods.clone();
            stockRepository.delete(intermediateGoods);
        }

        WorkDetails workForFinishProduct = workDetailsRepository.getOneWorkDetailsByWorkNumber(productionNumberToConfirm);
        finishProduct.setStatus(statusRepository.getStatusByStatusName("production_put_away_pending","Production"));
        finishProduct.setArticle(workForFinishProduct.getArticle());
        finishProduct.setHd_number(workForFinishProduct.getHdNumber());
        finishProduct.setPieces_qty(workForFinishProduct.getPiecesQty());
        finishProduct.setCreated(TimeUtils.timeNowLong());
        finishProduct.setLast_update(TimeUtils.timeNowLong());
        finishProduct.setChangeBy(SecurityUtils.usernameForActivations());
        finishProduct.setComment("");
        stockRepository.save(finishProduct);
    }
}

