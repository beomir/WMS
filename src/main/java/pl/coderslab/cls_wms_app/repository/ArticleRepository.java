package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;

import java.util.List;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

    @Query(value="select b.article_number from(Select a.article_number,pa.production_article_type,iapa.production_article_id pai,c.name from article a inner join intermediate_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.intermediate_article_id union select a.article_number, production_article_type, pa.id, c.name from article a inner join production_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.production_article_id ) b join intermediate_article p on  b.pai = p.id join article a2 on p.article_id = a2.id where b.production_article_type = 'finish product' and a2.article_number = ?1 and b.name = ?2",nativeQuery = true)
    Long finishProductNumberByIntermediateNumberAndCompanyName(Long intermediateNumber,String companyName);

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

    @Query("Select a from Article a where a.article_number =?1 and a.company.name = ?2")
    Article findArticleByArticleNumberAndCompanyName(Long articleNumber, String companyName);

    @Query("Select a from Article a where CONCAT(a.article_number,'') like ?1 and a.volume > ?2 and a.volume < ?3 and a.width > ?4 and a.width < ?5 and a.depth > ?6 and a.depth < ?7 and a.height > ?8 and a.height < ?9 and a.weight > ?10 and a.weight < ?11 and a.changeBy like ?12 and a.created > ?13 and a.created < ?14 and a.last_update > ?15 and a.last_update < ?16 and a.company.name like ?17 and a.article_desc like ?18 and a.articleTypes.articleClass like ?19")
    List<Article> getArticleByCriteria(String article_number,double volumeBiggerThan,double volumeLowerThan,double widthBiggerThan,double widthLowerThan,double depthBiggerThan,double depthLowerThan,double heightBiggerThan,double heightLowerThan,double weightBiggerThan,double weightLowerThan,String createdBy,String creationDateFrom,String creationDateTo,String lastUpdateDateFrom,String lastUpdateDateTo, String company, String articleDescription, String articleTypes);

    @Query(value = "Select count(article_number) from article a inner join production_article pa on a.id = pa.article_id where a.article_number = ?1 and pa.production_article_type = 'finish product'",nativeQuery = true)
    int checkIfFinishProductExists(Long articleNumber);

    ////// TODO check the demand for it
//    @Query(value = "Select sum(quantity_for_finished_product) from article inner join production_article pa on article.id = pa.article_id inner join company c on article.company_id = c.id where production_article_connection = ?1 and production_article_type = 'intermediate' and c.name = ?2",nativeQuery = true)
//    int sumOfAssignedIntermediateArticlesQty(Long articleNumber,String companyName);

    @Query(value = "select case when sum(b.qty) is null then 0 else sum(b.qty) end from(Select a.article_number,pa.production_article_type,iapa.production_article_id pai,IF(quantity_for_finished_product is null, 0,quantity_for_finished_product) qty,c.name from article a inner join intermediate_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.intermediate_article_id union select a.article_number, production_article_type, pa.id, if(quantity_for_finished_product is null,0,quantity_for_finished_product), c.name from article a inner join production_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.production_article_id ) b join production_article p on  b.pai = p.id join article a2 on p.article_id = a2.id where b.production_article_type = 'intermediate' and a2.article_number = ?1 and b.name = ?2",nativeQuery = true)
    int sumOfAssignedIntermediateArticlesQty(Long articleNumber,String companyName);

    @Query(value = "select case when sum(b.qty) is null then 0 else sum(b.qty) end from(Select a.article_number,pa.production_article_type,iapa.production_article_id pai,IF(quantity_for_finished_product is null, 0,quantity_for_finished_product) qty,c.name from article a inner join intermediate_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.intermediate_article_id where article_number != ?3 union select a.article_number, production_article_type, pa.id, if(quantity_for_finished_product is null,0,quantity_for_finished_product), c.name from article a inner join production_article pa on a.id = pa.article_id inner join company c on a.company_id = c.id left join intermediate_article_production_article iapa on pa.id = iapa.production_article_id where article_number != ?3 ) b join production_article p on  b.pai = p.id join article a2 on p.article_id = a2.id where b.production_article_type = 'intermediate' and a2.article_number = ?1 and b.name = ?2",nativeQuery = true)
    int sumOfAssignedIntermediateArticlesQtyForEdition(Long articleNumber,String companyName, Long intermediateArticleNumber);

    @Query(value = "Select sum(quantity_for_finished_product) from article inner join production_article pa on article.id = pa.article_id inner join company c on article.company_id = c.id where article_number = ?1 and production_article_type = 'finish product' and c.name = ?2",nativeQuery = true)
    int qtyNeededToCreateFinishProduct(Long articleNumber,String companyName);

    @Query(value ="select IFNULL((Select quantity_for_finished_product from article inner join production_article pa on article.id = pa.article_id inner join company c on article.company_id = c.id where article_number = ?1 and c.name = ?2 ),0)",nativeQuery = true)
    int qtyNeededToCreateFinishProductFromSingleIntermediateArticle(Long article_number, String companyName);

    @Query("Select a from Article a where a.production = true and a.active = true and a.company = ?1 and a.productionArticle.warehouse.name = ?2 and a.productionArticle.productionArticleType = 'finish product'")
    List<Article> finishProductList(Company company,String warehouse);

    @Query("Select a from Article a where a.production = true and a.active = true and a.company = ?1 and a.productionArticle.warehouse.name = ?2")
    List<Article> articleListByCompanyAndWarehouse(Company company,String warehouse);

    @Query("Select a from Article a where a.production = true and a.active = true and a.id = ?1 and a.company = ?2 and a.productionArticle.warehouse.name = ?3")
    Article articleForProduction(Long id,Company company, String warehouse);

    @Query("Select a from Article a where a.production = true and a.active = true and a.id = ?1 and a.company = ?2 and a.productionArticle.warehouse.name = ?3")
    List<Article> articlesListForProduction(Long id,Company company, String warehouse);

    @Query(value = "SELECT a.article_number article_number,a.article_desc,case when pieces_qty is null then 0 else sum(if(s2.status = 'on_hand',s.pieces_qty,0)) end volume,case when pa.quantity_for_finished_product is null then ia.quantity_for_finished_product else pa.quantity_for_finished_product end depth,case when ia.production_article_type is null then pa.production_article_type else ia.production_article_type end changeBy FROM article a LEFT JOIN storage s ON a.id = s.article_id left join intermediate_article ia on a.id = ia.article_id left join production_article pa on a.id = pa.article_id join intermediate_article_production_article iapa on pa.id = iapa.production_article_id join company c on a.company_id = c.id left join warehouse w on pa.warehouse_id = w.id left join status s2 on s.status_id = s2.id where a.production = true and a.active = true and c.name = ?1 and w.name = ?2  and iapa.production_article_id = ( select distinct iapa.production_article_id from article a inner join production_article ia on a.id = ia.article_id inner join intermediate_article_production_article iapa on ia.id = iapa.production_article_id where a.id = ?3) group by a.article_number,case when pa.quantity_for_finished_product = null then ia.quantity_for_finished_product else pa.quantity_for_finished_product end ,a.article_desc UNION SELECT a.article_number,a.article_desc, case when pieces_qty is null then 0 else sum(if(s2.status = 'on_hand',s.pieces_qty,0)) end, case when pa.quantity_for_finished_product is null then ia.quantity_for_finished_product else pa.quantity_for_finished_product end , case when ia.production_article_type is null then pa.production_article_type else ia.production_article_type end FROM article a RIGHT JOIN storage s ON a.id = s.article_id right join status s2 on s.status_id = s2.id left join intermediate_article ia on a.id = ia.article_id left join production_article pa on a.id = pa.article_id join intermediate_article_production_article iapa on ia.id = iapa.intermediate_article_id join company c on a.company_id = c.id right outer join warehouse w on ia.warehouse_id = w.id where a.production = true and a.active = true and c.name = ?1 and w.name = ?2  and iapa.production_article_id = ( select distinct iapa.production_article_id from article a inner join production_article ia on a.id = ia.article_id inner join intermediate_article_production_article iapa on ia.id = iapa.production_article_id where a.id = ?3 ) group by a.article_number,case when pa.quantity_for_finished_product = null then ia.quantity_for_finished_product else pa.quantity_for_finished_product end ,a.article_desc order by 1",nativeQuery = true)
    List<ProductionArticleOnStock> articlesNeededForProductionOnStock(String company,String warehouse,Long id);

    public static interface ProductionArticleOnStock {
        Long getArticle_number();
        String getArticle_desc();
        Long getVolume();
        Long getDepth();
        String getChangeBy();
    }


}
