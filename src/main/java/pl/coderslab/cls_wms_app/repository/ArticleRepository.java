package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;

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

}
