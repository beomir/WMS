package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Production;

import java.util.List;

public interface ProductionService {

    void add(Production production);

    Production findById(Long id);

    void save(Production production);

    void delete(Long id);

    void edit(Production production);

    List<Production> getProductionByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status);

}
