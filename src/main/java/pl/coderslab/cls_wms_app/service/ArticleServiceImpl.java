package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService{
    private ArticleRepository articleRepository;

    @Autowired
    public ArticleServiceImpl(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @Override
    public void add(Article article) {
        articleRepository.save(article);
    }


    @Override
    public List<Article> getArticle(String username) {
        return articleRepository.getArticle(username);
    }

    //for fixtures
    @Override
    public List<Article> getArticles() {
        return articleRepository.getArticles();
    }

    @Override
    public Article findById(Long id) {
        return articleRepository.getOne(id);
    }


    @Override
    public void delete(Long id) {
        Article article = articleRepository.getOne(id);
        article.setActive(false);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
    }

    @Override
    public void activate(Long id) {
        Article article = articleRepository.getOne(id);
        article.setActive(true);
        article.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        article.setChangeBy(SecurityUtils.usernameForActivations());
        articleRepository.save(article);
    }

    @Override
    public List<Article> getDeactivatedArticle() {
        return articleRepository.getDeactivatedArticle();
    }


}
