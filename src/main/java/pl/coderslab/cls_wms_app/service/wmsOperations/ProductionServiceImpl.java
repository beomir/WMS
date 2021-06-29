package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
public class ProductionServiceImpl implements ProductionService{
    private final ProductionRepository productionRepository;
    private final ExtremelyRepository extremelyRepository;
    private final CompanyService companyService;
    private final WarehouseService warehouseService;
    private final ArticleRepository articleRepository;
    private final WorkDetailsRepository workDetailsRepository;
    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;

    @Autowired
    public ProductionServiceImpl(ProductionRepository productionRepository, ExtremelyRepository extremelyRepository, CompanyService companyService, WarehouseService warehouseService, ArticleRepository articleRepository, WorkDetailsRepository workDetailsRepository, LocationRepository locationRepository, StockRepository stockRepository) {
        this.productionRepository = productionRepository;
        this.extremelyRepository = extremelyRepository;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.articleRepository = articleRepository;
        this.workDetailsRepository = workDetailsRepository;
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
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

    @Override
    public void createProduction(Production production, String articleId, String chosenWarehouse) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Long newProductionNumber =  Long.parseLong(LocalDateTime.now().format(formatter) + "001");
        productionSetters(production, chosenWarehouse,articleId);
        boolean workProductionStatus = false;
        // check extremely information
        if(extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),"Production_after_creation").getExtremelyValue().equals("2")){
            production.setStatus("Created");
        }
        if(extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),"Production_after_creation").getExtremelyValue().equals("1")){
            production.setStatus("Work pending");
            workProductionStatus = true;
        }
        // check if productionNumber exists
        if(productionRepository.getProductionNumberByProductionNumber(newProductionNumber) != null){
            production.setProductionNumber(productionRepository.lastProductionNumberForToday(newProductionNumber) + 1L);
        }
        if (productionRepository.getProductionNumberByProductionNumber(newProductionNumber) == null){
            production.setProductionNumber(newProductionNumber);
        }

        List<Article> intermediateArticles = articleRepository.articlesListForProduction(Long.parseLong(articleId),companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()),chosenWarehouse);
        for (Article intermediateArticle: intermediateArticles) {
            List<IntermediateArticle> intermediateArticleList = intermediateArticle.getProductionArticle().getIntermediateArticle();
            if(workProductionStatus){
                // Work & production creation
                for (IntermediateArticle ia : intermediateArticleList){
                    saveProductionInLoop(production, ia);
                    workCreationInLoop(production,ia);
                }
            }
             else{
                // only production creation
                for (IntermediateArticle ia : intermediateArticleList){
                    saveProductionInLoop(production, ia);
                }
            }
        }

    }

    public void saveProductionInLoop(Production production, IntermediateArticle ia){
        Production productionInLoop = new Production();
        productionInLoop.setIntermediateArticleNumber(ia.getArticle().getArticle_number());
        productionInLoop.setIntermediateArticlePieces(ia.getQuantityForFinishedProduct());
        productionInLoop.setProductionNumber(production.getProductionNumber());
        productionInLoop.setCompany(production.getCompany());
        productionInLoop.setFinishProductPieces(production.getFinishProductPieces());
        productionInLoop.setFinishProductNumber(production.getFinishProductNumber());
        productionInLoop.setWarehouse(production.getWarehouse());
        productionInLoop.setCreated(production.getCreated());
        productionInLoop.setStatus(production.getStatus());
        productionInLoop.setChangeBy(production.getChangeBy());
        productionInLoop.setLast_update(production.getLast_update());
        productionRepository.save(productionInLoop);
    }
    public void workCreationInLoop(Production production, IntermediateArticle ia){
        WorkDetails work = new WorkDetails();
        work.setPiecesQty(ia.getQuantityForFinishedProduct());
        work.setArticle(ia.getArticle());
        work.setCompany(ia.getCompany());
        work.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        work.setChangeBy(SecurityUtils.usernameForActivations());
        work.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        work.setWarehouse(ia.getWarehouse());
        work.setHdNumber(stockRepository.stockForProduction(ia.getArticle().getArticle_number()).getHd_number());
        work.setStatus(false);
        work.setFromLocation(locationRepository.findLocationByLocationName(stockRepository.stockForProduction(ia.getArticle().getArticle_number()).getChangeBy(),production.getWarehouse().getName()));
        work.setToLocation(locationRepository.findLocationByLocationName(ia.getLocation().getLocationName(),production.getWarehouse().getName()));
        work.setHandle(production.getProductionNumber().toString());
        work.setWorkDescription("Production picking");
        work.setWorkType("Production");
        work.setWorkNumber(production.getProductionNumber());
        workDetailsRepository.save(work);
    }

    public Production productionSetters(Production production, String chosenWarehouse, String articleId){
        production.setFinishProductNumber(articleRepository.getOne(Long.parseLong(articleId)).getArticle_number());
        production.setCreated(TimeUtils.timeNowLong());
        production.setChangeBy(SecurityUtils.usernameForActivations());
        production.setLast_update(TimeUtils.timeNowLong());
        production.setCompany(companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
        production.setWarehouse(warehouseService.getWarehouseByName(chosenWarehouse));
        return production;
    }
}
