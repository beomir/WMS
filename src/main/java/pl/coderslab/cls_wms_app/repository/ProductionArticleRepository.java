package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

@Repository
public interface ProductionArticleRepository extends JpaRepository<ProductionArticle, Long> {

    @Query("Select distinct ap from ProductionArticle ap join  ap.company c JOIN  Users u on u.company = c.name where u.username like ?1 order by ap.article.article_number")
    List<ProductionArticle> getProductionArticlesByUsername(String username);

    @Query("Select ap from ProductionArticle ap")
    List<ProductionArticle> getProductionArticles();

    @Query("Select ap from ProductionArticle ap where ap.article.id = ?1")
    ProductionArticle getProductionArticleByArticleId(Long id);

}
