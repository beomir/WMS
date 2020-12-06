package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.service.ArticleService;
import pl.coderslab.cls_wms_app.service.CompanyService;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class ArticleFixture {

    private ArticleService articleService;
    private CompanyService companyService;


    private List<Article> articleList = Arrays.asList(
            new Article(null, 1234567890123456L, "sandbag",null,null,null, "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Article(null, 1234567890123456L, "sandbag",null,null,null, "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Article(null, 1234567890123457L, "cement",null,null,null, "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Article(null, 1234567890123458L, "glue",null,null,null, "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true)
    );

    @Autowired
    public ArticleFixture(ArticleService articleService, CompanyService companyService) {
        this.articleService = articleService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();


        for (Article article : articleList) {
            articleService.add(article);
        }
        Article article1 = articleList.get(0);
        Article article2 = articleList.get(1);
        Article article3 = articleList.get(2);
        Article article4 = articleList.get(3);

        article1.setCompany(companies.get(0));
        article2.setCompany(companies.get(1));
        article3.setCompany(companies.get(2));
        article4.setCompany(companies.get(3));

        articleService.add(article1);
        articleService.add(article2);
        articleService.add(article3);
        articleService.add(article4);

    }
}
