package pl.coderslab.cls_wms_app.service.storage;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.ArticleRepository;
import pl.coderslab.cls_wms_app.repository.ProductionArticleRepository;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.wmsSettings.TransactionService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.temporaryObjects.ArticleSearch;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService{
    private ArticleRepository articleRepository;
    private TransactionService transactionService;
    private IssueLogService issueLogService;
    private WarehouseRepository warehouseRepository;
    private CompanyService companyService;
    public ArticleSearch articleSearch;



    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository, TransactionService transactionService, IssueLogService issueLogService, WarehouseRepository warehouseRepository, CompanyService companyService, ArticleSearch articleSearch) {
        this.articleRepository = articleRepository;
        this.transactionService = transactionService;
        this.issueLogService = issueLogService;
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
        this.articleSearch = articleSearch;


    }

    @Override
    public void add(Article article) {
        articleRepository.save(article);
    }

    @Override
    public void addNew(Article article, ProductionArticle productionArticle,HttpServletRequest request,HttpSession session,boolean productionArticleCheckbox) {
        if(article.getDepth() * article.getHeight() * article.getWidth() > 0 && article.getWeight() > 0){
            if(articleRepository.findArticleByArticle_numberAndCompanyName(article.getArticle_number(),article.getCompany()) == null){
                if(!productionArticle.getProductionArticleType().equals("intermediate")) {
                    article.setVolume(article.getDepth() * article.getHeight() * article.getWidth());
                    articleRepository.save(article);
                    Transaction transaction = new Transaction();
                    transaction.setHdNumber(0L);
                    transaction.setAdditionalInformation("Article: " + article.getArticle_number() + ", created for Company: " + article.getCompany().getName());
                    transaction.setArticle(article.getArticle_number());
                    transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    transaction.setCreatedBy(SecurityUtils.usernameForActivations());
                    transaction.setCustomer("");
                    transaction.setQuality("");
                    transaction.setQuantity(0L);
                    transaction.setReceptionNumber(0L);
                    transaction.setReceptionStatus("");
                    transaction.setShipmentNumber(0L);
                    transaction.setShipmentStatus("");
                    transaction.setTransactionDescription("Article Created");
                    transaction.setTransactionType("500");
                    transaction.setTransactionGroup("Configuration");
                    transaction.setUnit("");
                    transaction.setCompany(article.getCompany());
                    transaction.setVendor("");
                    transaction.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                    transactionService.add(transaction);
                    session.setAttribute("articleMessage","Article successfully created");

                }
                else articleEdition(article, productionArticle, request,session,productionArticleCheckbox);
            }
            else{
                IssueLog issueLog = new IssueLog();
                issueLog.setIssueLogContent("Article number: " + article.getArticle_number() + ", already exists");
                issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                issueLog.setAdditionalInformation("Article number: " + article.getArticle_number() + ", already exists for company: " + article.getCompany().getName());
                issueLog.setIssueLogFileName("");
                issueLog.setIssueLogFilePath("");
                issueLog.setIssueLogFileName("");
                issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                issueLogService.add(issueLog);
                session.setAttribute("articleMessage","Article number: " + article.getArticle_number() + ", already exists for company: " + article.getCompany().getName() + ". Check IssueLog ");
            }

        }
        else{
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("one from dimension were 0 or under 0." );
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setAdditionalInformation("Width: " + article.getWidth() + ", Depth: " + article.getDepth() + ", Height: " + article.getHeight() + ", Weight: " + article.getWeight());
            issueLog.setIssueLogFileName("");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
            issueLogService.add(issueLog);
            session.setAttribute("articleMessage","One from dimension were 0 or under 0. Check issue log");

        }
        if(articleSearch.company == null){
            articleSearch.company = companyService.getOneCompanyByUsername(SecurityUtils.username()).getName();
        }
    }

    @Override
    public void edit(Article article, ProductionArticle productionArticle, HttpServletRequest request, HttpSession session,boolean productionArticleCheckbox) {
        session.setAttribute("intermediateQtyForFinishProductStatus",true);
        if(article.getDepth() * article.getHeight() * article.getWidth() > 0 && article.getWeight() > 0){
            if(article.getId() == articleRepository.findArticleByArticle_number(article.getArticle_number()).getId()){
                if(!article.isProduction()) {
                    article.setVolume(article.getDepth() * article.getHeight() * article.getWidth());
                    article.setProduction(productionArticleCheckbox);
                    articleRepository.save(article);
                    Transaction transaction = new Transaction();
                    transaction.setHdNumber(0L);
                    transaction.setAdditionalInformation("Article: " + article.getArticle_number() + ", edited for Company: " + article.getCompany().getName());
                    transaction.setArticle(article.getArticle_number());
                    transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    transaction.setCreatedBy(SecurityUtils.usernameForActivations());
                    transaction.setCustomer("");
                    transaction.setQuality("");
                    transaction.setQuantity(0L);
                    transaction.setReceptionNumber(0L);
                    transaction.setReceptionStatus("");
                    transaction.setShipmentNumber(0L);
                    transaction.setShipmentStatus("");
                    transaction.setTransactionDescription("Article Edited");
                    transaction.setTransactionType("501");
                    transaction.setTransactionGroup("Configuration");
                    transaction.setUnit("");
                    transaction.setCompany(article.getCompany());
                    transaction.setVendor("");
                    transaction.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                    transactionService.add(transaction);
                    session.setAttribute("articleMessage","Article successfully edited");

                }
                else if(article.isProduction() && productionArticle.getProductionArticleType().equals("finish product")){
                    log.error("article.getArticle_number(): " + article.getArticle_number());
                    log.debug("sum of intermediate article qty needed to finish product:  " + articleRepository.sumOfAssignedIntermediateArticlesQty(article.getArticle_number(),article.getCompany().getName()));
                    log.debug("value from form about qty needed for finish product: " + productionArticle.getQuantityForFinishedProduct());
                    if(productionArticle.getQuantityForFinishedProduct() >= articleRepository.sumOfAssignedIntermediateArticlesQty(article.getArticle_number(),article.getCompany().getName())){
                        article.setVolume(article.getDepth() * article.getHeight() * article.getWidth());
                        article.setProduction(productionArticleCheckbox);
                        articleRepository.save(article);
                        Transaction transaction = new Transaction();
                        transaction.setHdNumber(0L);
                        transaction.setAdditionalInformation("Article: " + article.getArticle_number() + ", edited for Company: " + article.getCompany().getName());
                        transaction.setArticle(article.getArticle_number());
                        transaction.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                        transaction.setCreatedBy(SecurityUtils.usernameForActivations());
                        transaction.setCustomer("");
                        transaction.setQuality("");
                        transaction.setQuantity(0L);
                        transaction.setReceptionNumber(0L);
                        transaction.setReceptionStatus("");
                        transaction.setShipmentNumber(0L);
                        transaction.setShipmentStatus("");
                        transaction.setTransactionDescription("Article Edited");
                        transaction.setTransactionType("501");
                        transaction.setTransactionGroup("Configuration");
                        transaction.setUnit("");
                        transaction.setCompany(article.getCompany());
                        transaction.setVendor("");
                        transaction.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                        transactionService.add(transaction);
                        session.setAttribute("articleMessage","Finish product successfully edited");
                    }
                    else{
                        IssueLog issueLog = new IssueLog();
                        issueLog.setIssueLogContent("Qty setup to create finish product (article number: " + article.getArticle_number() + "): " + productionArticle.getQuantityForFinishedProduct() + " are less then sum of already assigned intermediate articles: " + articleRepository.sumOfAssignedIntermediateArticlesQty(article.getArticle_number(),article.getCompany().getName()));
                        issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                        issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                        issueLog.setAdditionalInformation("Qty setup to create finish product (article number: " + article.getArticle_number() + "): " + productionArticle.getQuantityForFinishedProduct() + " are less then sum of already assigned intermediate articles: " + articleRepository.sumOfAssignedIntermediateArticlesQty(article.getArticle_number(),article.getCompany().getName()));
                        issueLog.setIssueLogFileName("");
                        issueLog.setIssueLogFilePath("");
                        issueLog.setIssueLogFileName("");
                        issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                        issueLogService.add(issueLog);
                        session.setAttribute("articleMessage","Qty setup to create finish product (article number: " + article.getArticle_number() + "): " + productionArticle.getQuantityForFinishedProduct() + " are less then sum of already assigned intermediate articles: " + articleRepository.sumOfAssignedIntermediateArticlesQty(article.getArticle_number(),article.getCompany().getName()) + ", check issueLog");
                        session.setAttribute("intermediateQtyForFinishProductStatus",false);
                    }
                }
                else {
                    articleEdition(article,productionArticle,request,session,productionArticleCheckbox);
                }
            }
        }
        else{
            IssueLog issueLog = new IssueLog();
            issueLog.setIssueLogContent("one from dimension were 0 or under 0." );
            issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
            issueLog.setAdditionalInformation("Width: " + article.getWidth() + ", Depth: " + article.getDepth() + ", Height: " + article.getHeight() + ", Weight: " + article.getWeight());
            issueLog.setIssueLogFileName("");
            issueLog.setIssueLogFilePath("");
            issueLog.setIssueLogFileName("");
            issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
            issueLogService.add(issueLog);
            session.setAttribute("articleMessage","One from dimension were 0 or under 0. Check issue log");
            session.setAttribute("intermediateQtyForFinishProductStatus",false);
        }
    }

    private void articleEdition(Article article,ProductionArticle productionArticle, HttpServletRequest request,HttpSession session, boolean productionArticleCheckbox) {
        if(article.isProduction() && productionArticle.getProductionArticleType().equals("intermediate")){
            log.error("getProductionArticleConnection: " + productionArticle.getProductionArticleConnection());
            log.error("getProductionArticleType: " + productionArticle.getProductionArticleType());
            try{ Long productionArticleNumberForConnection = Long.parseLong(productionArticle.getProductionArticleConnection());
                if(articleRepository.checkIfFinishProductExists(productionArticleNumberForConnection)>0) {
                    log.error("+ sumOfAssignedIntermediateArticlesQtyForEdition: " + articleRepository.sumOfAssignedIntermediateArticlesQtyForEdition(productionArticleNumberForConnection,article.getCompany().getName(),article.getArticle_number()));
                    log.error("+ QuantityForFinishedProduct from form(input): " + productionArticle.getQuantityForFinishedProduct());
                    log.error("- articleRepository.qtyNeededToCreateFinishProductFromSingleIntermediateArticle(article.getArticle_number(),article.getCompany().getName()): " + articleRepository.qtyNeededToCreateFinishProductFromSingleIntermediateArticle(article.getArticle_number(),article.getCompany().getName()));
                    log.error("article.getArticle_number(): " + article.getArticle_number());
                    log.error("QuantityForFinishedProduct from DB: " + articleRepository.qtyNeededToCreateFinishProductFromSingleIntermediateArticle(Long.parseLong(productionArticle.getProductionArticleConnection()),article.getCompany().getName()));
                    if(articleRepository.qtyNeededToCreateFinishProduct(productionArticleNumberForConnection,article.getCompany().getName()) >= articleRepository.sumOfAssignedIntermediateArticlesQtyForEdition(productionArticleNumberForConnection,article.getCompany().getName(),article.getArticle_number()) + productionArticle.getQuantityForFinishedProduct() - articleRepository.qtyNeededToCreateFinishProductFromSingleIntermediateArticle(article.getArticle_number(),article.getCompany().getName())){
                        article.setVolume(article.getDepth() * article.getHeight() * article.getWidth());
                        article.setProduction(productionArticleCheckbox);
                        articleRepository.save(article);
                        if(request.getHeader("Referer").contains("formEditArticle")){
                            session.setAttribute("articleMessage","Article successfully edited");

                        }
                        if(request.getHeader("Referer").contains("formArticle")){
                            session.setAttribute("articleMessage","Article successfully created");

                        }

                    }
                    else{
                        log.error("Qty needed to create finish product");
                        IssueLog issueLog = new IssueLog();
                        issueLog.setIssueLogContent("Qty needed to create finish product (article number: " + productionArticle.getProductionArticleConnection() + "): " +  articleRepository.qtyNeededToCreateFinishProduct(productionArticleNumberForConnection,article.getCompany().getName()) + ", already assigned qty: " + articleRepository.sumOfAssignedIntermediateArticlesQty(productionArticleNumberForConnection,article.getCompany().getName()) + " + qty from last entry: " + productionArticle.getQuantityForFinishedProduct());
                        issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                        issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                        issueLog.setAdditionalInformation("Qty needed to create finish good (article number: " + productionArticle.getProductionArticleConnection() + "): " +  articleRepository.qtyNeededToCreateFinishProduct(productionArticleNumberForConnection,article.getCompany().getName()) + ", already assigned qty: " + articleRepository.sumOfAssignedIntermediateArticlesQty(productionArticleNumberForConnection,article.getCompany().getName()) + " + qty from last entry: " + productionArticle.getQuantityForFinishedProduct());
                        issueLog.setIssueLogFileName("");
                        issueLog.setIssueLogFilePath("");
                        issueLog.setIssueLogFileName("");
                        issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                        issueLogService.add(issueLog);
                        session.setAttribute("articleMessage","Information about sum of quantity needed to create finish good: " + productionArticle.getProductionArticleConnection() + ", is lower than sum  already assigned articles quantity + value from last intermediate article, check issuelog");
                        session.setAttribute("intermediateQtyForFinishProductStatus",false);
                    }

                } else {
                    log.error("Article connector for production");
                    IssueLog issueLog = new IssueLog();
                    issueLog.setIssueLogContent("Article connector for production: " + productionArticle.getProductionArticleConnection() + ", not exists as finish product");
                    issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                    issueLog.setAdditionalInformation("Article connector for production: " + productionArticle.getProductionArticleConnection() + ", not exists as finish product");
                    issueLog.setIssueLogFileName("");
                    issueLog.setIssueLogFilePath("");
                    issueLog.setIssueLogFileName("");
                    issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                    issueLogService.add(issueLog);
                    session.setAttribute("articleMessage","Article connector for production: " + productionArticle.getProductionArticleConnection() + ", not exists as finish product. Check IssueLog ");
                    session.setAttribute("intermediateQtyForFinishProductStatus",false);
                }
            }
            catch (NumberFormatException e){
                if(!productionArticleCheckbox){
                    article.setChangeBy(SecurityUtils.usernameForActivations());
                    article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    article.setProduction(productionArticleCheckbox);
                    article.setVolume(article.getDepth() * article.getHeight() * article.getWidth());
                    articleRepository.save(article);
                    session.setAttribute("articleMessage", "Intermediate article: " + article.getArticle_number() + " setup as not production article");
                }
                else {
                    log.error(" can't be parse on number");
                    IssueLog issueLog = new IssueLog();
                    issueLog.setIssueLogContent(productionArticle.getProductionArticleConnection() + " can't be parse on number");
                    issueLog.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
                    issueLog.setCreatedBy(SecurityUtils.usernameForActivations());
                    issueLog.setAdditionalInformation("Article connector for production with name: " + productionArticle.getProductionArticleConnection() + " can't be parse on number");
                    issueLog.setIssueLogFileName("");
                    issueLog.setIssueLogFilePath("");
                    issueLog.setIssueLogFileName("");
                    issueLog.setWarehouse(warehouseRepository.getOneWarehouse(1L));
                    issueLogService.add(issueLog);
                    session.setAttribute("articleMessage", "Article connector for production: " + productionArticle.getProductionArticleConnection() + " can't be parse on number. Check IssueLog ");
                    session.setAttribute("intermediateQtyForFinishProductStatus", false);
                }
            }
        }
    }


    @Override
    public List<Article> getArticle(String username) {
        return articleRepository.getArticle(username);
    }

    //for fixtures
    @Override
    public List<Article> getArticles() {
        return articleRepository.getArticles();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.getOne(id);
    }


    @Override
    public void delete(Long id,HttpSession session) {
        Article article = articleRepository.getOne(id);
        article.setActive(false);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
        session.setAttribute("articleMessage","Article: " + article.getArticle_number() + " deactivated");
    }

    @Override
    public void deactivateFinishProductWithIntermediates(Long id,HttpSession session) {
        Article article = articleRepository.getOne(id);
        article.setActive(false);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
        List<Long> intermediateArticlesList = articleRepository.intermediateProductNumberByFinishProductNumberAndCompanyName(article.getArticle_number(),article.getCompany().getName());
        for (Long intermediateArticleNumber : intermediateArticlesList){
            Article intermediateArticle = articleRepository.findArticleByArticle_number(intermediateArticleNumber);
            intermediateArticle.setActive(false);
            intermediateArticle.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            intermediateArticle.setChangeBy(SecurityUtils.usernameForActivations());
            articleRepository.save(intermediateArticle);
        }
        session.setAttribute("articleMessage","Articles: " + intermediateArticlesList.toString().replace("]","") + "," + article.getArticle_number() +  "] deactivated");
    }


    @Override
    public void activate(Long id,HttpSession session) {
        Article article = articleRepository.getOne(id);
        article.setActive(true);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
        session.setAttribute("articleMessage","Article: " + article.getArticle_number() + " activated");
    }

    @Override
    public void activateFinishProductWithIntermediates(Long id,HttpSession session) {
        Article article = articleRepository.getOne(id);
        article.setActive(true);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
        List<Long> intermediateArticlesList = articleRepository.intermediateProductNumberByFinishProductNumberAndCompanyName(article.getArticle_number(),article.getCompany().getName());
        for (Long intermediateArticleNumber : intermediateArticlesList){
            Article intermediateArticle = articleRepository.findArticleByArticle_number(intermediateArticleNumber);
            intermediateArticle.setActive(true);
            intermediateArticle.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
            intermediateArticle.setChangeBy(SecurityUtils.usernameForActivations());
            articleRepository.save(intermediateArticle);
        }

        session.setAttribute("articleMessage","Articles: " + intermediateArticlesList.toString().replace("]","") + ", " + article.getArticle_number()  + "] activated");
    }

    @Override
    public List<Article> getDeactivatedArticle() {
        return articleRepository.getDeactivatedArticle();
    }


    @Override
    public List<Article> getArticleByAllCriteria(String article_number,String volumeBiggerThan,String volumeLowerThan,String widthBiggerThan,String widthLowerThan,String depthBiggerThan,String depthLowerThan,String heightBiggerThan,String heightLowerThan,String weightBiggerThan,String weightLowerThan,String createdBy,String creationDateFrom,String creationDateTo,String lastUpdateDateFrom,String lastUpdateDateTo,String company,String articleDescription,String articleTypes) {
        if(article_number == null || article_number.equals("")){
            article_number = "%";
        }
        if(articleDescription == null || articleDescription.equals("")){
            articleDescription = "%";
        }
        if(company == null || company.equals("")){
            company = "%";
        }
        if(createdBy == null || createdBy.equals("")){
            createdBy = "%";
        }
        if(articleTypes == null || articleTypes.equals("")){
            articleTypes = "%";
        }
        if(volumeBiggerThan == null || volumeBiggerThan.equals("")){
            volumeBiggerThan = "0.0";
        }
        if(volumeLowerThan == null || volumeLowerThan.equals("")){
            volumeLowerThan = "9999999999999";
        }
        if(widthBiggerThan == null || widthBiggerThan.equals("")){
            widthBiggerThan = "0";
        }
        if(widthLowerThan == null || widthLowerThan.equals("")){
            widthLowerThan = "9999999999999";
        }
        if(depthBiggerThan == null || depthBiggerThan.equals("")){
            depthBiggerThan = "0";
        }
        if(depthLowerThan == null || depthLowerThan.equals("")){
            depthLowerThan = "9999999999999";
        }
        if(heightBiggerThan == null || heightBiggerThan.equals("")){
            heightBiggerThan = "0";
        }
        if(heightLowerThan == null || heightLowerThan.equals("")){
            heightLowerThan = "999999999999";
        }
        if(weightBiggerThan == null || weightBiggerThan.equals("")){
            weightBiggerThan = "0";
        }
        if(weightLowerThan == null || weightLowerThan.equals("")){
            weightLowerThan = "99999999999";
        }


        if(creationDateFrom == null || creationDateFrom.equals("")){
            creationDateFrom = "1970-01-01";
        }
        if(creationDateTo == null || creationDateTo.equals("")){
            creationDateTo = "2222-02-02";
        }
        if(lastUpdateDateFrom == null || lastUpdateDateFrom.equals("")){
            lastUpdateDateFrom = "1970-01-01";
        }
        if(lastUpdateDateTo == null || lastUpdateDateTo.equals("")){
            lastUpdateDateTo = "2222-02-02";
        }


        return articleRepository.getArticleByCriteria(article_number,Double.parseDouble(volumeBiggerThan),Double.parseDouble(volumeLowerThan),Double.parseDouble(widthBiggerThan),Double.parseDouble(widthLowerThan),Double.parseDouble(depthBiggerThan),Double.parseDouble(depthLowerThan),Double.parseDouble(heightBiggerThan),Double.parseDouble(heightLowerThan),Double.parseDouble(weightBiggerThan),Double.parseDouble(weightLowerThan),createdBy,creationDateFrom,creationDateTo,lastUpdateDateFrom,lastUpdateDateTo,company,articleDescription,articleTypes);
    }

    @Override
    public void save(ArticleSearch articleSearching) {
        articleSearch.setArticle_number(articleSearching.getArticle_number());
        articleSearch.setCreatedBy(articleSearching.getCreatedBy());
        articleSearch.setCreationDateFrom(articleSearching.getCreationDateFrom());
        articleSearch.setCreationDateTo(articleSearching.getCreationDateTo());
        articleSearch.setDepthBiggerThan(articleSearching.getDepthBiggerThan());
        articleSearch.setDepthLowerThan(articleSearching.getDepthLowerThan());
        articleSearch.setHeightBiggerThan(articleSearching.getHeightBiggerThan());
        articleSearch.setHeightLowerThan(articleSearching.getHeightLowerThan());
        articleSearch.setLastUpdateDateFrom(articleSearching.getLastUpdateDateFrom());
        articleSearch.setLastUpdateDateTo(articleSearching.getLastUpdateDateTo());
        articleSearch.setWeightLowerThan(articleSearching.getWeightLowerThan());
        articleSearch.setWeightBiggerThan(articleSearching.getWeightBiggerThan());
        articleSearch.setWidthLowerThan(articleSearching.getWidthLowerThan());
        articleSearch.setWidthBiggerThan(articleSearching.getWidthBiggerThan());
        articleSearch.setVolumeLowerThan(articleSearching.getVolumeLowerThan());
        articleSearch.setVolumeBiggerThan(articleSearching.getVolumeBiggerThan());
        articleSearch.setCompany(articleSearching.getCompany());
        articleSearch.setArticleDescription(articleSearching.getArticleDescription());
        articleSearch.setArticleTypes(articleSearching.getArticleTypes());
    }

}
