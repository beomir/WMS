package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Production;
import pl.coderslab.cls_wms_app.repository.ProductionRepository;

import java.util.List;

public interface ProductionService {

    void add(Production production);

    Production findById(Long id);

    void save(Production production);

    void delete(Long id);

    void edit(Production production);

    List<Production> getProductionByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status);

    void createProduction(Production production, String articleId, String chosenWarehouse);

    List<ProductionRepository.ProductionHeader> getProductionHeaderByCriteria(String productionCompany,String chosenWarehouse,String productionFinishProductNumber, String productionIntermediateArticleNumber,String productionCreated,String productionLastUpdate,String productionStatus);
}
