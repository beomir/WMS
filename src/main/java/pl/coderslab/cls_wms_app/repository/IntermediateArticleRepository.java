package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

@Repository
public interface IntermediateArticleRepository extends JpaRepository<IntermediateArticle, Long> {

    @Query("Select distinct ia from IntermediateArticle ia join  ia.company c JOIN  Users u on u.company = c.name where u.username like ?1 order by ia.article.article_number")
    List<IntermediateArticle> getIntermediateArticlesByUsername(String username);

    @Query("Select ia from IntermediateArticle ia")
    List<IntermediateArticle> getIntermediateArticles();

    @Query("Select ia from IntermediateArticle ia where ia.article.id = ?1")
    IntermediateArticle getIntermediateArticleByArticleId(Long id);

    @Query("Select ia from IntermediateArticle ia where ia.article = ?1")
    IntermediateArticle getIntermediateArticleByArticle(Article article);

}
