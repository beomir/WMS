package pl.coderslab.cls_wms_app.service.wmsSettings;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

public interface IntermediateArticleService {

    void add(IntermediateArticle intermediateArticle);

    void addNew(ProductionArticle productionArticle, Article article);

    void edit(ProductionArticle productionArticle, Article article, String warehouseName);

    List<IntermediateArticle> getIntermediateArticlesByUsername(String username);

    List<IntermediateArticle> getIntermediateArticles(); //for fixtures

    IntermediateArticle findById(Long id);

    IntermediateArticle getIntermediateArticleByArticleId(Long id);

    void delete(Long id);


}
