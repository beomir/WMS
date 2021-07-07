package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.repository.ProductionArticleRepository;

import pl.coderslab.cls_wms_app.repository.TransactionRepository;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Slf4j
@Service
public class ProductionArticleServiceImpl implements ProductionArticleService {
    private ProductionArticleRepository productionArticleRepository;
    private WarehouseService warehouseService;
    private TransactionRepository transactionRepository;


    @Autowired
    public ProductionArticleServiceImpl(ProductionArticleRepository productionArticleRepository, WarehouseService warehouseService, TransactionRepository transactionRepository) {
        this.productionArticleRepository = productionArticleRepository;
        this.warehouseService = warehouseService;
        this.transactionRepository = transactionRepository;
    }


    @Override
    public void add(ProductionArticle productionArticle) {
        productionArticleRepository.save(productionArticle);
    }

    @Override
    public void addNew(ProductionArticle productionArticle, Article article) {
        if(article.isProduction()){

            productionArticle.setArticle(article);
            productionArticle.setCompany(article.getCompany());
            productionArticleRepository.save(productionArticle);
        }
    }

    @Override
    public void edit(ProductionArticle productionArticle, Article article,String warehouseName,String productionArticleId) {
        String storageZoneEdited = "";
        String storageZonePrimary = "";

        if(productionArticle.getStorageZone() == null){
            storageZoneEdited = "not assigned";
        }
        if(productionArticle.getStorageZone() != null){
            storageZoneEdited = productionArticle.getStorageZone().getStorageZoneName();
        }

        if(article.isProduction()){

            Long id = Long.parseLong(productionArticleId);
            ProductionArticle productionArticleEdited = productionArticleRepository.getOne(id);
            if(productionArticleEdited.getStorageZone() == null){
                storageZonePrimary = "not assigned";
            }
            if(productionArticleEdited.getStorageZone() != null){
                storageZonePrimary = productionArticleEdited.getStorageZone().getStorageZoneName();
            }

            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Production Article Edited || before changing: " + productionArticleEdited.getArticle().getArticle_number() + ", type: " + productionArticleEdited.getProductionArticleType() + ", connected: " +  productionArticleEdited.getProductionArticleConnection() + ",qty for finished: " + productionArticleEdited.getQuantityForFinishedProduct() + ",location: " + productionArticleEdited.getLocation().getLocationName() + ",storageZone " + storageZonePrimary + ",warehouse: " + productionArticleEdited.getWarehouse().getName());
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
            transaction.setTransactionDescription("Production Article Edited || after changing: " + article.getArticle_number() + ", type: " + productionArticle.getProductionArticleType() + ", connected: " + productionArticle.getProductionArticleConnection()  + ",qty for finished: " + productionArticle.getQuantityForFinishedProduct() + ",location: " + productionArticle.getLocation().getLocationName() + ",storageZone " + storageZoneEdited + ",warehouse: " + warehouseName);
            transaction.setTransactionType("502");
            transaction.setTransactionGroup("Configuration");
            transaction.setUnit("");
            transaction.setCompany(article.getCompany());
            transaction.setVendor("");
            transaction.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            transactionRepository.save(transaction);

            productionArticleEdited.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            productionArticleEdited.setCompany(article.getCompany());
            productionArticleEdited.setArticle(article);
            productionArticleEdited.setLocation(productionArticle.getLocation());
            productionArticleEdited.setProductionArticleType(productionArticle.getProductionArticleType());
            productionArticleEdited.setStorageZone(productionArticle.getStorageZone());
            productionArticleEdited.setProductionArticleConnection(productionArticle.getProductionArticleConnection());
            productionArticleEdited.setQuantityForFinishedProduct(productionArticle.getQuantityForFinishedProduct());
            productionArticleEdited.setChangeBy(article.getChangeBy());
            productionArticleEdited.setLast_update(article.getLast_update());
            productionArticleRepository.save(productionArticleEdited);
        }
        else if(!article.isProduction() && productionArticleRepository.getOne(Long.parseLong(productionArticleId)) != null){

            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Production Article: " + article.getArticle_number() + "which was assigned to: " + productionArticle.getProductionArticleConnection() + ", type: " + productionArticle.getProductionArticleType() + " changed on not production Article");
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
            productionArticleRepository.delete(productionArticleRepository.getOne(Long.parseLong(productionArticleId)));
            log.error("Edited article: " + article.getArticle_number() + " deleted from production Article table");
        }
    }

    @Override
    public List<ProductionArticle> getProductionArticlesByUsername(String username) {
        return productionArticleRepository.getProductionArticlesByUsername(username);
    }

    @Override
    public List<ProductionArticle> getProductionArticles() {
        return productionArticleRepository.getProductionArticles();
    }

    @Override
    public ProductionArticle findById(Long id) {
        return productionArticleRepository.getOne(id);
    }

    @Override
    public ProductionArticle getProductionArticleByArticleId(Long id) {
        return productionArticleRepository.getProductionArticleByArticleId(id);
    }

    @Override
    public void delete(Long id) {
        productionArticleRepository.delete(productionArticleRepository.getOne(id));
    }

}
