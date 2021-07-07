package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.temporaryObjects.ArticleSearch;


import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface ArticleService {

    void add(Article article);

    void addNew(Article article, ProductionArticle productionArticle, HttpServletRequest request);

    void edit(Article article, ProductionArticle productionArticle,HttpServletRequest request);

    List<Article> getArticle(String username);

    List<Article> getArticles(); //for fixtures

    List<Article> getDeactivatedArticle();

    Article findById(Long id);


    void delete(Long id);

    void activate(Long id);

    void save(ArticleSearch articleSearch);

    List<Article> getArticleByAllCriteria(String article_number,String volumeBiggerThan,String volumeLowerThan,String widthBiggerThan,String widthLowerThan,String depthBiggerThan,String depthLowerThan,String heightBiggerThan,String heightLowerThan,String weightBiggerThan,String weightLowerThan,String createdBy,String creationDateFrom,String creationDateTo,String lastUpdateDateFrom,String lastUpdateDateTo,String company, String articleDescription,String articleTypes);

}
