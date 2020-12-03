package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.repository.ArticleRepository;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;

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
    public List<Article> getArticle() {
        return articleRepository.getArticle();
    }

    @Override
    public Article findById(Long id) {
        return null;
    }

    @Override
    public Article get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Article article) {

    }
}
