package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.ArticleRepository;
import pl.coderslab.cls_wms_app.repository.IntermediateArticleRepository;
import pl.coderslab.cls_wms_app.repository.ProductionArticleRepository;
import pl.coderslab.cls_wms_app.repository.TransactionRepository;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
public class IntermediateArticleServiceImpl implements IntermediateArticleService {
    private ProductionArticleRepository productionArticleRepository;
    private WarehouseService warehouseService;
    private TransactionRepository transactionRepository;
    private IntermediateArticleRepository intermediateArticleRepository;
    private ArticleRepository articleRepository;


    @Autowired
    public IntermediateArticleServiceImpl(ProductionArticleRepository productionArticleRepository, WarehouseService warehouseService, TransactionRepository transactionRepository, IntermediateArticleRepository intermediateArticleRepository, ArticleRepository articleRepository) {
        this.productionArticleRepository = productionArticleRepository;
        this.warehouseService = warehouseService;
        this.transactionRepository = transactionRepository;
        this.intermediateArticleRepository = intermediateArticleRepository;
        this.articleRepository = articleRepository;
    }


    @Override
    public void add(IntermediateArticle intermediateArticle) {
        intermediateArticleRepository.save(intermediateArticle);
    }

    @Override
    public void addNew(ProductionArticle productionArticle, Article article) {
        if(article.isProduction()){
            log.error("intermediate way 2/2");
            IntermediateArticle intermediateArticle = new IntermediateArticle();
            intermediateArticle.setArticle(article);
            intermediateArticle.setCompany(article.getCompany());
            intermediateArticle.setChangeBy(productionArticle.getChangeBy());
            intermediateArticle.setCreated(productionArticle.getCreated());
            intermediateArticle.setLast_update(productionArticle.getLast_update());
            intermediateArticle.setQuantityForFinishedProduct(productionArticle.getQuantityForFinishedProduct());
            intermediateArticle.setStorageZone(productionArticle.getStorageZone());
            intermediateArticle.setProductionArticleType(productionArticle.getProductionArticleType());
            intermediateArticle.setLocation(productionArticle.getLocation());
            intermediateArticle.setWarehouse(productionArticle.getWarehouse());
            if(!productionArticle.getProductionArticleConnection().equals("")){
            ProductionArticle productionArticleMain = productionArticleRepository.getProductionArticleByArticleNumber(Long.parseLong(productionArticle.getProductionArticleConnection()));
                intermediateArticle.getProductionArticle().add(productionArticleMain);
            }

            intermediateArticleRepository.save(intermediateArticle);
        }
    }

    @Override
    public void edit(ProductionArticle productionArticle, Article article,String warehouseName) {
        String storageZoneEdited = "";
        String storageZonePrimary = "";
        boolean articleWasNotProductionBefore = false;

        if(productionArticle.getStorageZone() == null){
            storageZoneEdited = "not assigned";
        }
        if(productionArticle.getStorageZone() != null){
            storageZoneEdited = productionArticle.getStorageZone().getStorageZoneName();
        }
        IntermediateArticle intermediateArticleEdited = intermediateArticleRepository.getIntermediateArticleByArticle(article);
        if (intermediateArticleEdited == null){
            intermediateArticleEdited = new IntermediateArticle();
            articleWasNotProductionBefore = true;
        }
        ProductionArticle productionArticleOld = productionArticleRepository.getProductionArticleByArticleNumber(articleRepository.finishProductNumberByIntermediateNumberAndCompanyName(article.getArticle_number(),article.getCompany().getName()));
        log.error("productionArticleOld: " + productionArticleOld);
        if(article.isProduction()){

            if(intermediateArticleEdited.getStorageZone() == null){
                storageZonePrimary = "not assigned";
            }
            if(intermediateArticleEdited.getStorageZone() != null){
                storageZonePrimary = intermediateArticleEdited.getStorageZone().getStorageZoneName();
            }
            Transaction transaction = new Transaction();
            if(!articleWasNotProductionBefore){
                transaction.setAdditionalInformation("Production Article Edited || before changing: " + intermediateArticleEdited.getArticle().getArticle_number() + ", type: " + intermediateArticleEdited.getProductionArticleType()  + ",qty for finished: " + intermediateArticleEdited.getQuantityForFinishedProduct() + ",location: " + intermediateArticleEdited.getLocation().getLocationName() + ",storageZone " + storageZonePrimary + ",warehouse: " + intermediateArticleEdited.getWarehouse().getName());
            }
            transaction.setHdNumber(0L);
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
            transaction.setTransactionDescription("Production Article Edited || after changing: " + article.getArticle_number() + ", type: " + productionArticle.getProductionArticleType() +  ",qty for finished: " + productionArticle.getQuantityForFinishedProduct() + ",location: " + productionArticle.getLocation().getLocationName() + ",storageZone " + storageZoneEdited + ",warehouse: " + warehouseName);
            transaction.setTransactionType("502");
            transaction.setTransactionGroup("Configuration");
            transaction.setUnit("");
            transaction.setCompany(article.getCompany());
            transaction.setVendor("");
            transaction.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            transactionRepository.save(transaction);


            intermediateArticleEdited.getProductionArticle().remove(productionArticleOld);
            intermediateArticleEdited.setArticle(article);
            intermediateArticleEdited.setCompany(article.getCompany());
            intermediateArticleEdited.setChangeBy(productionArticle.getChangeBy());
            intermediateArticleEdited.setCreated(productionArticle.getCreated());
            intermediateArticleEdited.setLast_update(productionArticle.getLast_update());
            intermediateArticleEdited.setQuantityForFinishedProduct(productionArticle.getQuantityForFinishedProduct());
            intermediateArticleEdited.setStorageZone(productionArticle.getStorageZone());
            intermediateArticleEdited.setProductionArticleType(productionArticle.getProductionArticleType());
            intermediateArticleEdited.setLocation(productionArticle.getLocation());
            intermediateArticleEdited.setWarehouse(productionArticle.getWarehouse());
            intermediateArticleEdited.getProductionArticle().add(productionArticleRepository.getProductionArticleByArticleNumber(Long.parseLong(productionArticle.getProductionArticleConnection())));
            intermediateArticleRepository.save(intermediateArticleEdited);


        }
        else if(!article.isProduction()){

            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Production Article: " + article.getArticle_number() + ", type: " + productionArticle.getProductionArticleType() + " changed on not production Article");
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
            transaction.setTransactionDescription("Production Article Edited");
            transaction.setTransactionType("502");
            transaction.setTransactionGroup("Configuration");
            transaction.setUnit("");
            transaction.setCompany(article.getCompany());
            transaction.setVendor("");
            transaction.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            transactionRepository.save(transaction);
            intermediateArticleEdited.getProductionArticle().remove(productionArticleOld);
            intermediateArticleRepository.delete(intermediateArticleEdited);
            log.error("Edited article: " + article.getArticle_number() + " deleted from production Article table");
        }
    }

    @Override
    public List<IntermediateArticle> getIntermediateArticlesByUsername(String username) {
        return intermediateArticleRepository.getIntermediateArticlesByUsername(username);
    }

    @Override
    public List<IntermediateArticle> getIntermediateArticles() {
        return intermediateArticleRepository.getIntermediateArticles();
    }

    @Override
    public IntermediateArticle findById(Long id) {
        return intermediateArticleRepository.getOne(id);
    }

    @Override
    public IntermediateArticle getIntermediateArticleByArticleId(Long id) {
        return intermediateArticleRepository.getIntermediateArticleByArticleId(id);
    }

    @Override
    public void delete(Long id) {
        intermediateArticleRepository.delete(intermediateArticleRepository.getOne(id));
    }

}
