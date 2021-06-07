package pl.coderslab.cls_wms_app.service.wmsSettings;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

public interface IntermediateArticleService {

    void add(IntermediateArticle intermediateArticle);

    void addNew(IntermediateArticle intermediateArticle, Article article);

    void edit(IntermediateArticle intermediateArticle, Article article, String warehouseName,String productionArticleId);

    List<IntermediateArticle> getIntermediateArticlesByUsername(String username);

    List<IntermediateArticle> getIntermediateArticles(); //for fixtures

    IntermediateArticle findById(Long id);

    IntermediateArticle getIntermediateArticleByArticleId(Long id);

    void delete(Long id);


}
