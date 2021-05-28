package pl.coderslab.cls_wms_app.service.wmsSettings;

import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

public interface ProductionArticleService {

    void add(ProductionArticle productionArticle);

    void edit(ProductionArticle productionArticle);

    List<ProductionArticle> getProductionArticlesByUsername(String username);

    List<ProductionArticle> getProductionArticles(); //for fixtures

    ProductionArticle findById(Long id);

    void delete(Long id);


}
