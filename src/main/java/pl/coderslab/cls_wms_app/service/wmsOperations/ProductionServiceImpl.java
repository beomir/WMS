package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;


import java.util.List;

@Service
@Slf4j
public class ProductionServiceImpl implements ProductionService{
    private final ProductionRepository productionRepository;

    @Autowired
    public ProductionServiceImpl(ProductionRepository productionRepository) {
        this.productionRepository = productionRepository;
    }

    @Override
    public void add(Production production) {
        productionRepository.save(production);
    }



    @Override
    public Production findById(Long id) {
        return productionRepository.getOne(id);
    }

    @Override
    public void save(Production production) {
        productionRepository.save(production);
    }

    @Override
    public void delete(Long id) {
        Production production = productionRepository.getOne(id);
        productionRepository.deleteById(id);
    }


    @Override
    public void edit(Production Production) {
        productionRepository.save(Production);
    }



    @Override
    public List<Production> getProductionByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status){
        if(warehouseName == null || warehouseName.equals("")){
            warehouseName = "%";
        }
        if(companyName == null || companyName.equals("")){
            companyName = "%";
        }
        if(finishProductNumber == null || finishProductNumber.equals("")){
            finishProductNumber = "%";
        }
        if(intermediateArticleNumber == null || intermediateArticleNumber.equals("")){
            intermediateArticleNumber = "%";
        }
        if(created == null || created.equals("")){
            created = "%";
        }
        if(lastUpdate == null || lastUpdate.equals("")){
            lastUpdate = "%";
        }
        if(status == null || status.equals("")){
            status = "%";
        }

        return productionRepository.getProductionByCriteria(companyName,warehouseName,finishProductNumber,intermediateArticleNumber,created,lastUpdate,status);

    }
}
