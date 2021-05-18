package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.ArticleTypesService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class ArticleFixture {

    private ArticleService articleService;
    private CompanyService companyService;
    private ArticleTypesService articleTypesService;


    private List<Article> articleList = Arrays.asList(
            new Article(null, 1234567890123455L, "bicycle",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",97,20,185,358900,15.00,null,false,null,null,null),
            new Article(null, 1234567890123456L, "sandbag",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",60,40,30,72000,30.00,null,false,null,null,null),
            new Article(null, 1234567890123457L, "cement",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",60,40,30,72000,35.00,null,false,null,null,null),
            new Article(null, 1234567890123458L, "glue",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",40,20,20,16000,20.00,null,false,null,null,null),
            new Article(null, 1234567890123459L, "wheel",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",50,50,20,50000,15.00,null,false,null,null,null),
            new Article(null, 1234567890123460L, "electric cables",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",500,5,5,12500,10.00,null,false,null,null,null),
            new Article(null, 3234567890123458L, "tv",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",150,50,15,112500,15.00,null,false,null,null,null),
            new Article(null, 3234567890123458L, "laptop",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",30,15,5,2250,1.00,null,false,null,null,null),
            new Article(null, 4234567890123458L, "screw 12mm",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",5,1,1,5,0.30,null,false,null,null,null),
            new Article(null, 7234567890123458L, "propeller",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",800,100,50,4000000,300,null,false,null,null,null),
            new Article(null, 5234567890123458L, "trousers",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",120,50,10,60000,1.40,null,false,null,null,null),
            new Article(null, 6234567890123458L, "socks",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",10,40,7,2800,0.30,null,false,null,null,null),
            new Article(null, 5234567890123423L, "jewelry",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",5,5,5,125,1.00,null,false,null,null,null),
            new Article(null, 1234567890123475L, "mini-parts",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",10,3,3,90,2.00,null,false,null,null,null),
            new Article(null, 9234567890123455L, "wooden box",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",10,10,10,1000,3.30,null,false,null,null,null),
            new Article(null, 1122334455747896L, "table",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",100,230,130,2990000,17.80,null,true,"finish product","6",35L),
            new Article(null, 7122334455747896L, "screw 8mm",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",5,1,1,5,0.02,null,true,"intermediate","1122334455747896",10L),
            new Article(null, 7122334455747897L, "screw 13mm",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",5,1,1,5,0.02,null,true,"intermediate","1122334455747896",8L),
            new Article(null, 7122334455747996L, "metal buckles",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",3,3,0.1,0.9,0.10,null,true,"intermediate","1122334455747896",8L),
            new Article(null, 5122334455747896L, "non-slip pads",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",1,10,10,100,0.10,null,true,"intermediate","1122334455747896",4L),
            new Article(null, 9122334455747196L, "wooden table legs 80x15x15cm color light pine",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",80,15,15,18000,2.00,null,true,"intermediate","1122334455747896",4L),
            new Article(null, 9122334455747296L, "table top 220x120x10cm color light pine",null,null,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",220,120,10,264000,8.50,null,true,"intermediate","1122334455747896",1L)

    );

    @Autowired
    public ArticleFixture(ArticleService articleService, CompanyService companyService, ArticleTypesService articleTypesService) {
        this.articleService = articleService;
        this.companyService = companyService;
        this.articleTypesService = articleTypesService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<ArticleTypes> articleTypes = articleTypesService.getArticleTypesForFixture();


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

        Article article16 = articleList.get(15);
        Article article17 = articleList.get(16);
        Article article18 = articleList.get(17);
        Article article19 = articleList.get(18);
        Article article20 = articleList.get(19);
        Article article21 = articleList.get(20);
        Article article22 = articleList.get(21);

        article1.setArticleTypes(articleTypes.get(2));
        article2.setArticleTypes(articleTypes.get(1));
        article3.setArticleTypes(articleTypes.get(1));
        article4.setArticleTypes(articleTypes.get(1));
        article5.setArticleTypes(articleTypes.get(3));
        article6.setArticleTypes(articleTypes.get(3));
        article7.setArticleTypes(articleTypes.get(5));
        article8.setArticleTypes(articleTypes.get(5));
        article9.setArticleTypes(articleTypes.get(2));
        article10.setArticleTypes(articleTypes.get(2));
        article11.setArticleTypes(articleTypes.get(4));
        article12.setArticleTypes(articleTypes.get(4));
        article13.setArticleTypes(articleTypes.get(6));
        article14.setArticleTypes(articleTypes.get(2));
        article15.setArticleTypes(articleTypes.get(3));

        article16.setArticleTypes(articleTypes.get(3));
        article17.setArticleTypes(articleTypes.get(3));
        article18.setArticleTypes(articleTypes.get(3));
        article19.setArticleTypes(articleTypes.get(3));
        article20.setArticleTypes(articleTypes.get(4));
        article21.setArticleTypes(articleTypes.get(3));
        article22.setArticleTypes(articleTypes.get(3));

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

        article16.setCompany(companies.get(0));
        article17.setCompany(companies.get(0));
        article18.setCompany(companies.get(0));
        article19.setCompany(companies.get(0));
        article20.setCompany(companies.get(0));
        article21.setCompany(companies.get(0));
        article22.setCompany(companies.get(0));

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

        articleService.add(article16);
        articleService.add(article17);
        articleService.add(article18);
        articleService.add(article19);
        articleService.add(article20);
        articleService.add(article21);
        articleService.add(article22);

    }
}
