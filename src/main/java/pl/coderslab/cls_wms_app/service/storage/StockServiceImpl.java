package pl.coderslab.cls_wms_app.service.storage;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.util.Precision;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;

import javax.servlet.http.HttpSession;
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
    public String locationName;
    private final IssueLogService issueLogService;
    private final WorkDetailsRepository workDetailsRepository;
    private final LocationService locationService;

    public List<Stock> storage = new ArrayList<>();

    @Autowired
    public StockServiceImpl(StockRepository stockRepository, LocationRepository locationRepository, SendEmailService sendEmailService, EmailRecipientsRepository emailRecipientsRepository, ReceptionRepository receptionRepository, StatusRepository statusRepository, UnitRepository unitRepository, ArticleRepository articleRepository, TransactionService transactionService, IssueLogService issueLogService, WorkDetailsRepository workDetailsRepository, LocationService locationService) {
        this.stockRepository = stockRepository;
        this.locationRepository = locationRepository;
        this.sendEmailService = sendEmailService;
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.receptionRepository = receptionRepository;
        this.statusRepository = statusRepository;
        this.unitRepository = unitRepository;
        this.articleRepository = articleRepository;
        this.transactionService = transactionService;
        this.issueLogService = issueLogService;
        this.workDetailsRepository = workDetailsRepository;
        this.locationService = locationService;
    }

    @Override
    public List<Stock> getStorage(String warehouseName, String username) {
        return stockRepository.getStorage(warehouseName, username);
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

        Location location = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
        stock.setLocation(location);
        location.setFreeSpace(Precision.round(location.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty(), 2));
        location.setFreeWeight(Precision.round(location.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty(), 2));
        location.setTemporaryFreeSpace(Precision.round(location.getTemporaryFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty(), 2));
        location.setTemporaryFreeWeight(Precision.round(location.getTemporaryFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty(), 2));
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
    public void changeStatus(Stock stock, String newStatus) {
        Status newStockStatus = statusRepository.checkIfStockStatusExists(newStatus);
        if (newStockStatus != null) {
            log.debug("SERVICE newStatus: " + newStatus);
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Status changed on stock");
            transaction.setAdditionalInformation("Status changed from: " + stock.getStatus().getStatus() + " on: " + newStockStatus.getStatus() + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
            transaction.setTransactionType("301");
            transactionStock(stock, transaction, receptionRepository);
            transactionService.add(transaction);
            stock.setStatus(newStockStatus);
            stockRepository.save(stock);
        } else {
            log.error("Incorrect status set: " + newStatus);
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Incorrect status set: " + newStatus);
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setAdditionalInformation("Attempt of set incorrect status: " + newStatus + ", for hd number: " + stock.getHd_number());
            issueLogService.add(issueLog);
        }
    }

    @Override
    public void changeArticleNumber(Stock stock, String newArticleNumber, HttpSession session) {
        boolean canBeMixed = true;
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName(), stock.getWarehouse().getName());
        List<Stock> locationContent = stockRepository.getStockContentByLocationNameAndWarehouse(stock.getLocation().getLocationName(),stock.getWarehouse().getName());
        double locationFreeSpace = 0;
        double locationFreeWeight = 0;
        String wrongClass = "";
        try{
            Long articleNumber = Long.parseLong(newArticleNumber);
            Article article = articleRepository.findArticleByArticle_numberAndCompanyName(articleNumber,stock.getCompany());
            int qtyOfDifferentArticleInStockLocation = stockRepository.qtyOfDifferentArticleNumberInStockLocation(stock.getLocation().getLocationName());
            log.error("qtyOfDifferentArticleInStockLocation: " + qtyOfDifferentArticleInStockLocation);
            //if more than one article in location
            if(qtyOfDifferentArticleInStockLocation>1){
                log.error("qtyOfDifferentArticleInStockLocation bigger than one");
                //then check each one of them if they can be mix with new one
                for (Stock stockContent:locationContent) {
                    log.error("one of article stayed in location: " + stockContent.getArticle().getArticleTypes().getMixed());
                    log.error("new potential article class: " + article.getArticleTypes().getArticleClass());
                    log.error("can be mixed: " + stockContent.getArticle().getArticleTypes().getMixed().contains(article.getArticleTypes().getArticleClass()));
                    if(!stockContent.getArticle().getArticleTypes().getMixed().contains(article.getArticleTypes().getArticleClass())){
                        wrongClass = wrongClass + " " + stockContent.getArticle().getArticle_number() + " " + stockContent.getArticle().getArticleTypes().getArticleClass();
                        canBeMixed = false;
                    }
                }
            }
            log.error("canBeMixed value: " + canBeMixed);

            //calculate free space in location when new article is bigger than old one
            if(article.getVolume() > stock.getArticle().getVolume()){
                 locationFreeSpace = location.getTemporaryFreeSpace() - (article.getVolume() * stock.getPieces_qty() - stock.getArticle().getVolume() * stock.getPieces_qty());
                log.error("changed article volume is bigger than previous article. Volume of new article: " + article.getVolume() );
                log.error("changed article volume is bigger than previous article. Volume of old article: " + stock.getArticle().getVolume() );
                log.error("locationFreeSpace: " + locationFreeSpace);
            }
            //calculate free space in location when new article is lower than old one
            else if(article.getVolume() < stock.getArticle().getVolume()){
                 locationFreeSpace = location.getTemporaryFreeSpace() + ( stock.getArticle().getVolume() * stock.getPieces_qty() - article.getVolume() * stock.getPieces_qty());
                log.error("changed article volume is lower than previous article. Volume of new article: " + article.getVolume() );
                log.error("changed article volume is lower than previous article. Volume of old article: " + stock.getArticle().getVolume() );
                log.error("locationFreeSpace: " + locationFreeSpace);
            }
            //calculate free weight in location when new article is heavier than old one
            if(article.getWeight() > stock.getArticle().getWeight()){
                locationFreeWeight = location.getFreeWeight() - (article.getWeight() * stock.getPieces_qty() - stock.getArticle().getWeight() * stock.getPieces_qty());
                log.error("changed article weight is bigger than previous article. Volume of new article: " + article.getWeight() );
                log.error("changed article weight is bigger than previous article. Volume of old article: " + stock.getArticle().getWeight() );
                log.error("locationFreeWeight: " + locationFreeWeight);
            }
            //calculate free weight in location when new article is lighter than old one
            else if(article.getWeight() < stock.getArticle().getWeight()){
                locationFreeWeight = location.getTemporaryFreeWeight() + (stock.getArticle().getWeight() * stock.getPieces_qty() - article.getWeight() * stock.getPieces_qty());
                log.error("changed article weight is lower than previous article. Volume of new article: " + article.getWeight() );
                log.error("changed article weight is lower than previous article. Volume of old article: " + stock.getArticle().getWeight() );
                log.error("locationFreeWeight: " + locationFreeWeight);
            }

            if (canBeMixed && locationFreeSpace > 0 && locationFreeWeight > 0) {
                Transaction transaction = new Transaction();
                transaction.setTransactionDescription("Article number changed on stock");
                transaction.setAdditionalInformation("Article number changed from: " + stock.getArticle().getArticle_number()  + " on: " + article.getArticle_number()  + " in location: " + stock.getLocation().getLocationName());
                transaction.setTransactionType("302");
                transactionStock(stock, transaction, receptionRepository);
                transactionService.add(transaction);
                if(article.getVolume() > stock.getArticle().getVolume()){

                    log.error("changed article volume is bigger than previous article. location temporaryFreeWeight before change: " + location.getTemporaryFreeSpace() );
                    location.setFreeSpace(location.getFreeSpace() - (article.getVolume() * stock.getPieces_qty() - stock.getArticle().getVolume() * stock.getPieces_qty()));
                    location.setTemporaryFreeSpace(location.getTemporaryFreeSpace() - (article.getVolume() * stock.getPieces_qty() - stock.getArticle().getVolume() * stock.getPieces_qty()));
                    log.error("changed article volume is bigger than previous article. location temporaryFreeWeight after change: " + location.getTemporaryFreeSpace() );
                }
                else if(article.getVolume() < stock.getArticle().getVolume()){

                    log.error("changed article volume is lower than previous article. location temporaryFreeWeight before change: " + location.getTemporaryFreeSpace() );
                    location.setFreeSpace(location.getFreeSpace() + ( stock.getArticle().getVolume() * stock.getPieces_qty() - article.getVolume() * stock.getPieces_qty()));
                    location.setTemporaryFreeSpace(location.getTemporaryFreeSpace() + ( stock.getArticle().getVolume() * stock.getPieces_qty() - article.getVolume() * stock.getPieces_qty() ));
                    log.error("changed article volume is lower than previous article. location temporaryFreeWeight after change: " + location.getTemporaryFreeSpace() );
                }
                if(article.getWeight() > stock.getArticle().getWeight()){

                    log.error("changed article weight is bigger than previous article. location temporaryFreeWeight before change: " + location.getTemporaryFreeWeight() );
                    location.setTemporaryFreeWeight(location.getTemporaryFreeWeight() - (article.getWeight() * stock.getPieces_qty() - stock.getArticle().getWeight() * stock.getPieces_qty()));
                    location.setFreeWeight(location.getFreeWeight() - (article.getWeight() * stock.getPieces_qty() - stock.getArticle().getWeight() * stock.getPieces_qty()));
                    log.error("changed article weight is bigger than previous article. location temporaryFreeWeight after change: " + location.getTemporaryFreeWeight() );
                }
                else if(article.getWeight() < stock.getArticle().getWeight()){
                    log.error("changed article weight is lower than previous article. location temporaryFreeWeight before change: " + location.getTemporaryFreeWeight() );
                    location.setFreeWeight(location.getFreeWeight() + (stock.getArticle().getWeight() * stock.getPieces_qty() - article.getWeight() * stock.getPieces_qty()));
                    location.setTemporaryFreeWeight(location.getTemporaryFreeWeight() + (stock.getArticle().getWeight() * stock.getPieces_qty() - article.getWeight() * stock.getPieces_qty()));
                    log.error("changed article weight is lower than previous article. location temporaryFreeWeight after change: " + location.getTemporaryFreeWeight() );
                }
                session.setAttribute("stockMessage","Article: " + stock.getArticle().getArticle_number() + " changed on new one: " + newArticleNumber);
                stock.setArticle(article);
                locationRepository.save(location);
                stockRepository.save(stock);

            }
            //check if is more than one article in location, and changed article cannot be mixed with another article in location
            else if (qtyOfDifferentArticleInStockLocation > 1 && !canBeMixed) {
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Articles in location can't be mix.");
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(stock.getWarehouse());
                issueLog.setAdditionalInformation("Article: " + article.getArticle_number() + " with class:" + article.getArticleTypes().getArticleClass() + " can't be mix with another articles in this location because of: " + wrongClass);
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLogService.add(issueLog);
                session.setAttribute("stockMessage","Article: " + article.getArticle_number() + " with class:" + article.getArticleTypes().getArticleClass() + " can't be mix with another articles in this location because of: " + wrongClass + ". Information saved in issueLog");
            } else if (locationFreeSpace < 0) {
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Location after change article, have not enough space");
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(stock.getWarehouse());
                issueLog.setAdditionalInformation("Potential free space in location after change article on new one: " + locationFreeSpace);
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLogService.add(issueLog);
                session.setAttribute("stockMessage","Location after change article, have not enough space. Information saved in issueLog");
            } else {
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Location after change article, is overweight");
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(stock.getWarehouse());
                issueLog.setAdditionalInformation("Potential free weight in location after change article on new one: " + locationFreeSpace);
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLogService.add(issueLog);
                session.setAttribute("stockMessage","Location after change article, have not enough free weight. Information saved in issueLog");
            }
        }
        catch(Exception e){

        }


    }

    @Override
    public void changeQty(Stock stock, String newQuantity) {
        Location location = locationRepository.findLocationByLocationName(stock.getLocation().getLocationName(), stock.getWarehouse().getName());
        try {
            Long newQuantityNumber = Long.parseLong(newQuantity);

            if (location.getTemporaryFreeSpace() - stock.getArticle().getVolume() * newQuantityNumber > 0 && location.getTemporaryFreeWeight() - stock.getArticle().getWeight() * newQuantityNumber > 0) {
                Transaction transaction = new Transaction();
                transaction.setTransactionDescription("Quantity changed on stock");

                transaction.setTransactionType("303");
                transactionStock(stock, transaction, receptionRepository);
                transactionService.add(transaction);
                log.error("location temporary volume before change Qty: " + location.getTemporaryFreeSpace());
                log.error("location temporary free weight before change Qty: " + location.getTemporaryFreeWeight());
                if(newQuantityNumber > stock.getPieces_qty()){
                    location.setFreeSpace(location.getFreeSpace() - stock.getArticle().getVolume() * (newQuantityNumber - stock.getPieces_qty()));
                    location.setFreeWeight(location.getFreeWeight() - stock.getArticle().getWeight() * (newQuantityNumber - stock.getPieces_qty()));
                    location.setTemporaryFreeSpace(location.getTemporaryFreeSpace() - stock.getArticle().getVolume() * (newQuantityNumber - stock.getPieces_qty()));
                    location.setTemporaryFreeWeight(location.getTemporaryFreeWeight() - stock.getArticle().getWeight() * (newQuantityNumber - stock.getPieces_qty()));
                    log.error("increase qty location temporary volume after change Qty: " + location.getTemporaryFreeSpace());
                    log.error("increase qty location temporary free weight after change Qty: " + location.getTemporaryFreeWeight());
                    transaction.setAdditionalInformation("Quantity increased from: " + stock.getPieces_qty() + " on: " + newQuantityNumber  + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
                }
                else if(newQuantityNumber < stock.getPieces_qty()){
                    location.setFreeSpace(location.getFreeSpace() + stock.getArticle().getVolume() * (stock.getPieces_qty() -newQuantityNumber ));
                    location.setFreeWeight(location.getFreeWeight() + stock.getArticle().getWeight() * (stock.getPieces_qty() - newQuantityNumber));
                    location.setTemporaryFreeSpace(location.getTemporaryFreeSpace() + stock.getArticle().getVolume() * (stock.getPieces_qty() - newQuantityNumber));
                    location.setTemporaryFreeWeight(location.getTemporaryFreeWeight() + stock.getArticle().getWeight() * (stock.getPieces_qty() - newQuantityNumber));
                    log.error("decrease qty location temporary volume after change Qty: " + location.getTemporaryFreeSpace());
                    log.error("decrease qty location temporary free after before change Qty: " + location.getTemporaryFreeWeight());
                    transaction.setAdditionalInformation("Quantity decreased from: " + stock.getPieces_qty() + " on: " + newQuantityNumber  + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
                }

                stock.setPieces_qty(newQuantityNumber);
                stockRepository.save(stock);
                locationRepository.save(location);
            } else if (location.getTemporaryFreeSpace() - stock.getArticle().getVolume() * newQuantityNumber < 0) {
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Location after change quantity, have not enough space");
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(stock.getWarehouse());
                issueLog.setAdditionalInformation("");
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLogService.add(issueLog);
            } else {
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
        } catch (Exception e) {
            log.error("newQuantity value: " + newQuantity);
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Enter value is not a number");
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
    public void changeQuality(Stock stock, String newQuality) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Quality changed on stock");
        transaction.setAdditionalInformation("Quality changed from: " + stock.getQuality() + " on: " + newQuality + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("304");
        transactionStock(stock, transaction, receptionRepository);
        transactionService.add(transaction);
        stock.setQuality(newQuality);
        stockRepository.save(stock);
    }

    @Override
    public void changeUnit(Stock stock, String newUnit) {
        Unit unit = unitRepository.getUnitByName(newUnit);
        if (unit != null) {
            Transaction transaction = new Transaction();
            transaction.setTransactionDescription("Unit changed on stock");
            transaction.setAdditionalInformation("Unit changed from: " + stock.getUnit().getName() + " on: " + newUnit + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
            transaction.setTransactionType("305");
            transactionStock(stock, transaction, receptionRepository);
            transactionService.add(transaction);
            stock.setUnit(unit);
            stockRepository.save(stock);
        } else {
            log.error("Incorrect status set: " + newUnit);
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("Incorrect status set: " + newUnit);
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setAdditionalInformation("Attempt of set incorrect status: " + newUnit + ", for hd number: " + stock.getHd_number());
            issueLogService.add(issueLog);
        }

    }

    @Override
    public void changeComment(Stock stock, String newComment) {
        Transaction transaction = new Transaction();
        transaction.setTransactionDescription("Comment changed");
        transaction.setAdditionalInformation("Comment changed from: " + stock.getComment() + " on: " + newComment + " for article: " + stock.getArticle().getArticle_number() + " in location: " + stock.getLocation().getLocationName());
        transaction.setTransactionType("306");
        stock.setComment(newComment);
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
    public void transfer(Stock stock, String locationNames, Stock chosenStockPositional) {

        log.error("stock.getPieces_qty(): " + stock.getPieces_qty());
        log.error("chosenStockPositional.pieces_qtyObj: " + chosenStockPositional.getPieces_qty());
        Transaction transaction = new Transaction();
        transaction.setTransactionGroup("Stock");
        transaction.setReceptionNumber(chosenStockPositional.getReceptionNumber());
        transaction.setArticle(stock.getArticle().getArticle_number());
        transaction.setQuality(chosenStockPositional.getQuality());
        transaction.setUnit(chosenStockPositional.getUnit().getName());
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
        if (stock.getPieces_qty() < chosenStockPositional.getPieces_qty()) {

            log.error("partial transfer");

            try {
                if (stockRepository.getStockByHdNumber(stock.getHd_number()) != null) {
                    Stock stockInDestinationLocation = stockRepository.getStockByHdNumber(stock.getHd_number());
                    partialTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                    log.error("stockRepository.getStockByHdNumber(stock.getHd_number())" + stockRepository.getStockByHdNumber(stock.getHd_number()));
                } else {
                    Stock stockInDestinationLocation = stockRepository.getStockByLocationName(locationNames);
                    partialTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                    log.error("stockRepository.getStockByLocationName(locationNames)" + stockRepository.getStockByLocationName(locationNames));
                }

            } catch (NullPointerException e) {
                transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " destiny location was empty before transfer");
                transaction.setTransactionDescription("Transfer stock Partial: New pallet created, destiny location was empty before transfer");
                transaction.setTransactionType("317");
                transactionService.add(transaction);
                Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
                locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
                locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
                Stock splittedPallet = new Stock();
                splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                splittedPallet.setQuality(chosenStockPositional.getQuality());
                splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
                splittedPallet.setUnit(chosenStockPositional.getUnit());
                splittedPallet.setReceptionNumber(chosenStockPositional.getReceptionNumber());
                splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()));
                splittedPallet.setArticle(stock.getArticle());
                splittedPallet.setWarehouse(chosenStockPositional.getWarehouse());
                splittedPallet.setCompany(chosenStockPositional.getCompany());
                splittedPallet.setHd_number(stock.getHd_number());
                splittedPallet.setPieces_qty(stock.getPieces_qty());
                splittedPallet.setStatus(chosenStockPositional.getStatus());
                splittedPallet.setComment(chosenStockPositional.getComment());
                stockRepository.save(splittedPallet);
                locationRepository.save(locationForSplittedPallet);
            }

            Location locationForTheRemainingQuantity = locationRepository.getOne(chosenStockPositional.getLocation().getId());
            Long remainingQuantity = chosenStockPositional.getPieces_qty() - stock.getPieces_qty();
            locationForTheRemainingQuantity.setFreeSpace(locationForTheRemainingQuantity.getFreeSpace() + stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForTheRemainingQuantity.setFreeWeight(locationForTheRemainingQuantity.getFreeWeight() + stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock remainingStockInLocation = stockRepository.getOne(chosenStockPositional.getId());

            remainingStockInLocation.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            remainingStockInLocation.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            remainingStockInLocation.setQuality(chosenStockPositional.getQuality());
            remainingStockInLocation.setChangeBy(SecurityUtils.usernameForActivations());
            remainingStockInLocation.setUnit(chosenStockPositional.getUnit());
            remainingStockInLocation.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            remainingStockInLocation.setLocation(locationForTheRemainingQuantity);
            remainingStockInLocation.setArticle(stock.getArticle());
            remainingStockInLocation.setWarehouse(chosenStockPositional.getWarehouse());
            remainingStockInLocation.setCompany(chosenStockPositional.getCompany());
            remainingStockInLocation.setHd_number(remainingStockInLocation.getHd_number());
            remainingStockInLocation.setPieces_qty(remainingQuantity);
            remainingStockInLocation.setStatus(chosenStockPositional.getStatus());
            remainingStockInLocation.setComment(chosenStockPositional.getComment());
            stockRepository.save(remainingStockInLocation);
            locationRepository.save(locationForTheRemainingQuantity);

        }
        //transfer all qty
        else if (stock.getPieces_qty() == chosenStockPositional.getPieces_qty()) {
            log.error("full transfer");
            Location location = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
            Location remainingLocation = locationRepository.getOne(chosenStockPositional.getLocation().getId());
            try {

                Stock stockInDestinationLocation = stockRepository.getStockByLocationName(locationNames);
                fullTransfer(stock, locationNames, chosenStockPositional, stockInDestinationLocation, transaction);
                log.error("stockRepository.getStockByLocationName(locationNames)");

            } catch (NullPointerException e) {
                stock.setLocation(location);
                stock.setUnit(chosenStockPositional.getUnit());
                stock.setCreated(chosenStockPositional.getCreated());
                stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                stock.setChangeBy(SecurityUtils.usernameForActivations());
                stock.setQuality(chosenStockPositional.getQuality());
                stock.setReceptionNumber(chosenStockPositional.getReceptionNumber());
                log.error("Transfer to not occupied location");
                stockRepository.save(stock);
                transaction.setAdditionalInformation("Transfer stock full pallet: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " destiny location was empty before transfer");
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
            issueLog.setIssueLogContent("Requested qty to send: " + stock.getPieces_qty() + ", bigger than on pallet: " + chosenStockPositional.getPieces_qty());
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setWarehouse(stock.getWarehouse());
            issueLog.setAdditionalInformation("Requested qty to send: " + stock.getPieces_qty() + ", bigger than on pallet: " + chosenStockPositional.getPieces_qty());
            issueLogService.add(issueLog);
        }

    }


    void partialTransfer(Stock stock, String locationNames, Stock chosenStockPositional, Stock stockInDestinationLocation, Transaction transaction) {
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            log.error("Qty cumulated for one stock id, for partial transfer, the same article ( destination == original)");
            stockRepository.save(stockInDestinationLocation);
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number());
            transaction.setTransactionDescription("Transfer stock Partial: cumulate to destination HD with the same article");
            transaction.setTransactionType("311");
            transactionService.add(transaction);
        }

        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            log.error("Qty transfer to different pallet number, for partial transfer, different article ( destination != original)");
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock splittedPallet = new Stock();
            splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setQuality(chosenStockPositional.getQuality());
            splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
            splittedPallet.setUnit(chosenStockPositional.getUnit());
            splittedPallet.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()));
            splittedPallet.setArticle(stock.getArticle());
            splittedPallet.setWarehouse(chosenStockPositional.getWarehouse());
            splittedPallet.setCompany(chosenStockPositional.getCompany());
            splittedPallet.setHd_number(stock.getHd_number());
            splittedPallet.setPieces_qty(stock.getPieces_qty());
            splittedPallet.setStatus(chosenStockPositional.getStatus());
            splittedPallet.setComment(chosenStockPositional.getComment());
            stockRepository.save(splittedPallet);
            locationRepository.save(locationForSplittedPallet);

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " remaining pieces stayed on origin location");
            transaction.setTransactionDescription("Transfer stock Partial: new pallet in destination location created, with different article");
            transaction.setTransactionType("312");
            transactionService.add(transaction);
        }
        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            log.error("Qty transfer to different pallet number, for partial transfer, the same article ( destination == original)");
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());
            Stock splittedPallet = new Stock();
            splittedPallet.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            splittedPallet.setQuality(chosenStockPositional.getQuality());
            splittedPallet.setChangeBy(SecurityUtils.usernameForActivations());
            splittedPallet.setUnit(chosenStockPositional.getUnit());
            splittedPallet.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            splittedPallet.setLocation(locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()));
            splittedPallet.setArticle(stock.getArticle());
            splittedPallet.setWarehouse(chosenStockPositional.getWarehouse());
            splittedPallet.setCompany(chosenStockPositional.getCompany());
            splittedPallet.setHd_number(stock.getHd_number());
            splittedPallet.setPieces_qty(stock.getPieces_qty());
            splittedPallet.setStatus(chosenStockPositional.getStatus());
            splittedPallet.setComment(chosenStockPositional.getComment());
            stockRepository.save(splittedPallet);
            locationRepository.save(locationForSplittedPallet);

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " remaining pieces stayed on origin location");
            transaction.setTransactionDescription("Transfer stock Partial: new pallet in destination location created, with the same article");
            transaction.setTransactionType("313");
            transactionService.add(transaction);
        }
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            log.error("Qty cumulated for one stock id, for partial transfer, different article ( destination != original)");
            stockRepository.save(stockInDestinationLocation);
            Location locationForSplittedPallet = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
            locationForSplittedPallet.setFreeSpace(locationForSplittedPallet.getFreeSpace() - stock.getArticle().getVolume() * stock.getPieces_qty());
            locationForSplittedPallet.setFreeWeight(locationForSplittedPallet.getFreeWeight() - stock.getArticle().getWeight() * stock.getPieces_qty());

            transaction.setAdditionalInformation("Transfer partial quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number());
            transaction.setTransactionDescription("Transfer stock Partial: cumulate to destination HD with different article");
            transaction.setTransactionType("320");
            transactionService.add(transaction);
        }
    }


    void fullTransfer(Stock stock, String locationNames, Stock chosenStockPositional, Stock stockInDestinationLocation, Transaction transaction) {
        Location location = locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName());
        log.error("stockInDestinationLocation.getHd_number(): " + stockInDestinationLocation.getHd_number());
        log.error("stockInDestinationLocation.location: " + stockInDestinationLocation.getLocation().getLocationName());
        log.error("stock.getHd_number(): " + stock.getHd_number());
        //transfer all qty from origin pallet and cumulate to destination pallet - article on pallets are the same
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stockInDestinationLocation.setPieces_qty(stockInDestinationLocation.getPieces_qty() + stock.getPieces_qty());
            stockRepository.save(stockInDestinationLocation);
            stockRepository.delete(stock);
            log.error("transfer all qty from origin pallet and cumulate to destination pallet - article on pallets are the same");

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from location: " + chosenStockPositional.getLocation().getLocationName() + "Article: " + chosenStockPositional.getArticle().getArticle_number() + ", HD number:" + chosenStockPositional.getHd_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + ". Quantity after transfer on pallet: " + (stockInDestinationLocation.getPieces_qty()));
            transaction.setTransactionDescription("Transfer stock full pallet: cumulate all pieces from origin pallet to pallet in destination location. Article are the same");
            transaction.setTransactionType("314");
            transactionService.add(transaction);

        }
        //transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with desitnation location
        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() == stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(chosenStockPositional.getUnit());
            stock.setCreated(chosenStockPositional.getCreated());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQuality());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            log.error("transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with destination location");
            stockRepository.save(stock);

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " transferred pieces are kept on the same origin pallet number, same articles");
            transaction.setTransactionDescription("Transfer stock full pallet: origin pallet number kept, article in destiny location is the same like in origin location");
            transaction.setTransactionType("315");
            transactionService.add(transaction);
        }
        //transfer all qty from origin pallet and cumulate to destination pallet - articles are not the same
        if (stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(chosenStockPositional.getUnit());
            stock.setCreated(chosenStockPositional.getCreated());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQuality());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            log.error("transfer all qty from origin pallet and cumulate to destination pallet - articles are not the same");
            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " and cumulate it to HD number: " + stockInDestinationLocation.getHd_number() + " where are multiple items");
            transaction.setTransactionDescription("Transfer stock full pallet: cumulate all pieces from origin pallet to pallet in destination location. Article are different");
            transaction.setTransactionType("316");
            transactionService.add(transaction);
            stockRepository.save(stock);
        }

        if (!stockInDestinationLocation.getHd_number().equals(stock.getHd_number()) && stockInDestinationLocation.getArticle().getArticle_number() != stock.getArticle().getArticle_number()) {
            stock.setLocation(location);
            stock.setUnit(chosenStockPositional.getUnit());
            stock.setCreated(chosenStockPositional.getCreated());
            stock.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            stock.setChangeBy(SecurityUtils.usernameForActivations());
            stock.setQuality(chosenStockPositional.getQuality());
            stock.setReceptionNumber(chosenStockPositional.getReceptionNumber());
            log.error("transfer all qty from origin pallet with keeping pallet number - article on origin pallet is the same like on pallet with destination location");
            stockRepository.save(stock);

            transaction.setAdditionalInformation("Transfer all quantity: " + stock.getPieces_qty() + " from origin pallet: " + chosenStockPositional.getHd_number() + " in location: " + chosenStockPositional.getLocation().getLocationName() + ", article: " + chosenStockPositional.getArticle().getArticle_number() + ", to Location: " + locationRepository.findLocationByLocationName(locationNames, stock.getWarehouse().getName()).getLocationName() + " transferred pieces are kept on the same origin pallet number, different articles");
            transaction.setTransactionDescription("Transfer stock full pallet: origin pallet number kept, article in destiny location is different than this from origin location");
            transaction.setTransactionType("319");
            transactionService.add(transaction);
        }
    }


    @Override
    public void produceGoods(Long productionNumberToConfirm) throws CloneNotSupportedException {
        Stock finishProduct = new Stock();
        WorkDetails workDetails = workDetailsRepository.getOneWorkDetailsByWorkNumber(productionNumberToConfirm);
        locationService.reduceTheAvailableContentOfTheLocation(workDetails.getToLocation().getLocationName(), workDetails.getArticle().getArticle_number(), workDetails.getPiecesQty(), workDetails.getWarehouse().getName(), workDetails.getCompany().getName());
        locationService.restoreTheAvailableLocationCapacity(workDetails.getFromLocation().getLocationName(), workDetails.getArticle().getArticle_number(), workDetails.getPiecesQty(), workDetails.getWarehouse().getName(), workDetails.getCompany().getName());


        List<Stock> stockList = stockRepository.getStockByWorkHandleAndWorkDescription(workDetailsRepository.workDetailHandle(productionNumberToConfirm, "Producing finish product from collected intermediate articles"), "Production picking");
        log.debug("productionNumberToConfirm: " + productionNumberToConfirm);
        for (Stock intermediateGoods : stockList) {
            finishProduct = (Stock) intermediateGoods.clone();
            stockRepository.delete(intermediateGoods);
        }

        WorkDetails workForFinishProduct = workDetailsRepository.getOneWorkDetailsByWorkNumber(productionNumberToConfirm);
        finishProduct.setStatus(statusRepository.getStatusByStatusName("production_put_away_pending", "Production"));
        finishProduct.setArticle(workForFinishProduct.getArticle());
        finishProduct.setHd_number(workForFinishProduct.getHdNumber());
        finishProduct.setPieces_qty(workForFinishProduct.getPiecesQty());
        finishProduct.setCreated(TimeUtils.timeNowLong());
        finishProduct.setLast_update(TimeUtils.timeNowLong());
        finishProduct.setChangeBy(SecurityUtils.usernameForActivations());
        stockRepository.save(finishProduct);
    }


}

