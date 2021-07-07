package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.IntermediateArticle;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;

import java.util.List;

@Repository
public interface IntermediateArticleRepository extends JpaRepository<IntermediateArticle, Long> {

    @Query("Select distinct ap from IntermediateArticle ap join  ap.company c JOIN  Users u on u.company = c.name where u.username like ?1 order by ap.article.article_number")
    List<IntermediateArticle> getIntermediateArticlesByUsername(String username);

    @Query("Select ap from IntermediateArticle ap")
    List<IntermediateArticle> getIntermediateArticles();

    @Query("Select ap from IntermediateArticle ap where ap.article.id = ?1")
    IntermediateArticle getIntermediateArticleByArticleId(Long id);

}
