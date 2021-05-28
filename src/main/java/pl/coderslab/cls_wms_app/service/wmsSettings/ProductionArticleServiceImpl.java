package pl.coderslab.cls_wms_app.service.wmsSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.ProductionArticle;
import pl.coderslab.cls_wms_app.repository.ProductionArticleRepository;

import java.util.List;


@Slf4j
@Service
public class ProductionArticleServiceImpl implements ProductionArticleService {
    private ProductionArticleRepository productionArticleRepository;


    @Autowired
    public ProductionArticleServiceImpl(ProductionArticleRepository productionArticleRepository) {
        this.productionArticleRepository = productionArticleRepository;
    }


    @Override
    public void add(ProductionArticle productionArticle) {
        productionArticleRepository.save(productionArticle);
    }

    @Override
    public void edit(ProductionArticle productionArticle) {

    }

    @Override
    public List<ProductionArticle> getProductionArticlesByUsername(String username) {
        return productionArticleRepository.getProductionArticlesByUsername(username);
    }

    @Override
    public List<ProductionArticle> getProductionArticles() {
        return productionArticleRepository.getProductionArticles();
    }

    @Override
    public ProductionArticle findById(Long id) {
        return productionArticleRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        productionArticleRepository.delete(productionArticleRepository.getOne(id));
    }

}
