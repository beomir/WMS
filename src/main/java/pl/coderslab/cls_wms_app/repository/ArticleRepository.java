package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query("Select distinct a from Article a join fetch a.company c JOIN fetch Users u on u.company = c.name where a.active = true and u.username like ?1 order by a.article_number")
    List<Article> getArticle(String username);

    @Query("Select a from Article a where a.active = false")
    List<Article> getDeactivatedArticle();

    //for fixtures
    @Query("Select a from Article a")
    List<Article> getArticles();

    @Query("Select a from Article a where a.article_number = ?1")
    Article findArticleByArticle_number(Long articleNbr);

    @Query("Select a from Article a where a.article_number =?1 and a.company = ?2")
    Article findArticleByArticle_numberAndCompanyName(Long article_number, Company company);

    @Query("Select a from Article a where CONCAT(a.article_number,'') like ?1 and a.volume > ?2 and a.volume < ?3 and a.width > ?4 and a.width < ?5 and a.depth > ?6 and a.depth < ?7 and a.height > ?8 and a.height < ?9 and a.weight > ?10 and a.weight < ?11 and a.changeBy like ?12 and a.created > ?13 and a.created < ?14 and a.last_update > ?15 and a.last_update < ?16 and a.company.name like ?17 and a.article_desc like ?18 and a.articleTypes.articleClass like ?19")
    List<Article> getArticleByCriteria(String article_number,double volumeBiggerThan,double volumeLowerThan,double widthBiggerThan,double widthLowerThan,double depthBiggerThan,double depthLowerThan,double heightBiggerThan,double heightLowerThan,double weightBiggerThan,double weightLowerThan,String createdBy,String creationDateFrom,String creationDateTo,String lastUpdateDateFrom,String lastUpdateDateTo, String company, String articleDescription, String articleTypes);

    @Query(value = "Select count(article_number) from article where article_number = ?1 and production_article_type = 'finish product'",nativeQuery = true)
    int checkIfFinishProductExists(Long articleNumber);

    @Query(value = "Select sum(quantity_for_finished_product) from article inner join company c on article.company_id = c.id where production_article_connection = ?1 and production_article_type = 'intermediate' and c.name = ?2",nativeQuery = true)
    int sumOfAssignedIntermediateArticlesQty(Long articleNumber,String companyName);

    @Query(value = "Select sum(quantity_for_finished_product) from article inner join company c on article.company_id = c.id where article_number = ?1 and production_article_type = 'finish product' and c.name = ?2",nativeQuery = true)
    int qtyNeededToCreateFinishProduct(Long articleNumber,String companyName);
}
