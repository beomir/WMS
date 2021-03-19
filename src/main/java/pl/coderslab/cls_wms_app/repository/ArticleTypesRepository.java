package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;

import java.util.List;

@Repository
public interface ArticleTypesRepository extends JpaRepository<ArticleTypes, Long> {

    @Query("Select  a from ArticleTypes a where a.active = true")
    List<ArticleTypes> getArticleTypes();

    @Query("Select a from ArticleTypes a where a.active = false")
    List<ArticleTypes> getDeactivatedArticleTypes();

    //for fixtures
    @Query("Select a from ArticleTypes a")
    List<ArticleTypes> getArticleTypesForFixture();

}
