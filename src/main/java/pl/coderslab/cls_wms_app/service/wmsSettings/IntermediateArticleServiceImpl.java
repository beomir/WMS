package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.Transaction;
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


    @Autowired
    public IntermediateArticleServiceImpl(ProductionArticleRepository productionArticleRepository, WarehouseService warehouseService, TransactionRepository transactionRepository, IntermediateArticleRepository intermediateArticleRepository) {
        this.productionArticleRepository = productionArticleRepository;
        this.warehouseService = warehouseService;
        this.transactionRepository = transactionRepository;
        this.intermediateArticleRepository = intermediateArticleRepository;
    }


    @Override
    public void add(IntermediateArticle intermediateArticle) {
        intermediateArticleRepository.save(intermediateArticle);
    }

    @Override
    public void addNew(IntermediateArticle intermediateArticle, Article article) {
        if(article.isProduction()){

            intermediateArticle.setArticle(article);
            intermediateArticle.setCompany(article.getCompany());
            intermediateArticleRepository.save(intermediateArticle);
        }
    }

    @Override
    public void edit(IntermediateArticle intermediateArticle, Article article,String warehouseName,String intermediateArticleId) {
        String storageZoneEdited = "";
        String storageZonePrimary = "";

        if(intermediateArticle.getStorageZone() == null){
            storageZoneEdited = "not assigned";
        }
        if(intermediateArticle.getStorageZone() != null){
            storageZoneEdited = intermediateArticle.getStorageZone().getStorageZoneName();
        }

        if(article.isProduction()){

            Long id = Long.parseLong(intermediateArticleId);
            IntermediateArticle intermediateArticleEdited = intermediateArticleRepository.getOne(id);
            if(intermediateArticleEdited.getStorageZone() == null){
                storageZonePrimary = "not assigned";
            }
            if(intermediateArticleEdited.getStorageZone() != null){
                storageZonePrimary = intermediateArticleEdited.getStorageZone().getStorageZoneName();
            }

            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Production Article Edited || before changing: " + intermediateArticleEdited.getArticle().getArticle_number() + ", type: " + intermediateArticleEdited.getProductionArticleType()  + ",qty for finished: " + intermediateArticleEdited.getQuantityForFinishedProduct() + ",location: " + intermediateArticleEdited.getLocation().getLocationName() + ",storageZone " + storageZonePrimary + ",warehouse: " + intermediateArticleEdited.getWarehouse().getName());
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
            transaction.setTransactionDescription("Production Article Edited || after changing: " + article.getArticle_number() + ", type: " + intermediateArticleEdited.getProductionArticleType() +  ",qty for finished: " + intermediateArticleEdited.getQuantityForFinishedProduct() + ",location: " + intermediateArticleEdited.getLocation().getLocationName() + ",storageZone " + storageZoneEdited + ",warehouse: " + warehouseName);
            transaction.setTransactionType("502");
            transaction.setTransactionGroup("Configuration");
            transaction.setUnit("");
            transaction.setCompany(article.getCompany());
            transaction.setVendor("");
            transaction.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            transactionRepository.save(transaction);

            intermediateArticleEdited.setWarehouse(warehouseService.getWarehouseByName(warehouseName));
            intermediateArticleEdited.setCompany(article.getCompany());
            intermediateArticleEdited.setArticle(article);
            intermediateArticleEdited.setLocation(intermediateArticle.getLocation());
            intermediateArticleEdited.setProductionArticleType(intermediateArticle.getProductionArticleType());
            intermediateArticleEdited.setStorageZone(intermediateArticle.getStorageZone());
            intermediateArticleEdited.setQuantityForFinishedProduct(intermediateArticle.getQuantityForFinishedProduct());
            intermediateArticleEdited.setChangeBy(article.getChangeBy());
            intermediateArticleEdited.setLast_update(article.getLast_update());
            intermediateArticleRepository.save(intermediateArticleEdited);
        }
        else if(!article.isProduction() && intermediateArticleRepository.getOne(Long.parseLong(intermediateArticleId)) != null){

            Transaction transaction = new Transaction();
            transaction.setHdNumber(0L);
            transaction.setAdditionalInformation("Production Article: " + article.getArticle_number() + ", type: " + intermediateArticle.getProductionArticleType() + " changed on not production Article");
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
            productionArticleRepository.delete(productionArticleRepository.getOne(Long.parseLong(intermediateArticleId)));
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
