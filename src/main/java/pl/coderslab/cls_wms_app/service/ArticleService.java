package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;

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
