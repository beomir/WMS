package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
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
            new Article(null, 1234567890123455L, "bicycle",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123456L, "sandbag",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123457L, "cement",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123458L, "glue",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123459L, "wheel",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123460L, "electric cables",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 3234567890123458L, "tv",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 3234567890123458L, "laptop",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 4234567890123458L, "screw",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 7234567890123458L, "propeller",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 5234567890123458L, "trousers",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 6234567890123458L, "socks",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 5234567890123423L, "jewelry",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 1234567890123475L, "mini-parts",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Article(null, 9234567890123455L, "wooden box",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system")
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
        Article article5 = articleList.get(4);
        Article article6 = articleList.get(5);
        Article article7 = articleList.get(6);
        Article article8 = articleList.get(7);
        Article article9 = articleList.get(8);
        Article article10 = articleList.get(9);
        Article article11 = articleList.get(10);
        Article article12 = articleList.get(11);
        Article article13 = articleList.get(12);
        Article article14 = articleList.get(13);
        Article article15 = articleList.get(14);

        article1.setCompany(companies.get(0));
        article2.setCompany(companies.get(1));
        article3.setCompany(companies.get(2));
        article4.setCompany(companies.get(3));
        article5.setCompany(companies.get(3));
        article6.setCompany(companies.get(4));
        article7.setCompany(companies.get(5));
        article8.setCompany(companies.get(5));
        article9.setCompany(companies.get(1));
        article10.setCompany(companies.get(2));
        article11.setCompany(companies.get(0));
        article12.setCompany(companies.get(4));
        article13.setCompany(companies.get(4));
        article14.setCompany(companies.get(5));
        article15.setCompany(companies.get(2));

        articleService.add(article1);
        articleService.add(article2);
        articleService.add(article3);
        articleService.add(article4);
        articleService.add(article5);
        articleService.add(article6);
        articleService.add(article7);
        articleService.add(article8);
        articleService.add(article9);
        articleService.add(article10);
        articleService.add(article11);
        articleService.add(article12);
        articleService.add(article13);
        articleService.add(article14);
        articleService.add(article15);

    }
}
