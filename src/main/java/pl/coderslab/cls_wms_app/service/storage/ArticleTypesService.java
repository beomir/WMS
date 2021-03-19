package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;

import java.util.List;

public interface ArticleTypesService {

    void add(ArticleTypes articleTypes);

    List<ArticleTypes> getArticleTypes();

    List<ArticleTypes> getArticleTypesForFixture(); //for fixtures

    List<ArticleTypes> getDeactivatedArticleTypes();

    ArticleTypes findById(Long id);

    void deactivate(Long id);

    void activate(Long id);

    void edit(Long id);

}
