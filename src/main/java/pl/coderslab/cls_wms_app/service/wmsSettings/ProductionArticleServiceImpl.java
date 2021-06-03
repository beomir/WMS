package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.repository.ProductionArticleRepository;

import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.List;


@Slf4j
@Service
public class ProductionArticleServiceImpl implements ProductionArticleService {
    private ProductionArticleRepository productionArticleRepository;
    private WarehouseService warehouseService;


    @Autowired
    public ProductionArticleServiceImpl(ProductionArticleRepository productionArticleRepository, WarehouseService warehouseService) {
        this.productionArticleRepository = productionArticleRepository;
        this.warehouseService = warehouseService;
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
        if(article.isProduction()){
            Long id = Long.parseLong(productionArticleId);
            ProductionArticle productionArticleEdited = productionArticleRepository.getOne(id);
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
