package pl.coderslab.cls_wms_app.service.storage;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;
import pl.coderslab.cls_wms_app.repository.ArticleTypesRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ArticleTypesServiceImpl implements ArticleTypesService{
    private final ArticleTypesRepository articleTypesRepository;

    @Autowired
    public ArticleTypesServiceImpl(ArticleTypesRepository articleTypesRepository) {
        this.articleTypesRepository = articleTypesRepository;
    }

    @Override
    public void add(ArticleTypes articleTypes) {
        articleTypesRepository.save(articleTypes);
    }


    @Override
    public List<ArticleTypes> getArticleTypes() {
        return articleTypesRepository.getArticleTypes();
    }

    //for fixtures
    @Override
    public List<ArticleTypes> getArticleTypesForFixture() {
        return articleTypesRepository.getArticleTypes();
    }

    @Override
    public ArticleTypes findById(Long id) {
        return articleTypesRepository.getOne(id);
    }


    @Override
    public void deactivate(Long id) {
        ArticleTypes articleTypes = articleTypesRepository.getOne(id);
        articleTypes.setActive(false);
        articleTypes.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        articleTypes.setChangeBy(SecurityUtils.usernameForActivations());
        articleTypesRepository.save(articleTypes);
    }

    @Override
    public void activate(Long id) {
        ArticleTypes articleTypes = articleTypesRepository.getOne(id);
        articleTypes.setActive(true);
        articleTypes.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        articleTypes.setChangeBy(SecurityUtils.usernameForActivations());
        articleTypesRepository.save(articleTypes);
    }

    @Override
    public void edit(Long id) {
        articleTypesRepository.save(articleTypesRepository.getOne(id));
    }

    @Override
    public List<ArticleTypes> getDeactivatedArticleTypes() {
        return articleTypesRepository.getDeactivatedArticleTypes();
    }


}
