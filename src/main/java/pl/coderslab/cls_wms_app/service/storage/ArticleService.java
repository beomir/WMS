package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Article;

import java.util.List;

public interface ArticleService {

    void add(Article article);

    List<Article> getArticle(String username);

    List<Article> getArticles(); //for fixtures

    List<Article> getDeactivatedArticle();

    Article findById(Long id);


    void delete(Long id);

    void activate(Long id);

}
