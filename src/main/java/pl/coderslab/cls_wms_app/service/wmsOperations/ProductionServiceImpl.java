package pl.coderslab.cls_wms_app.service.wmsOperations;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.servlet.http.HttpSession;
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
    private final StatusRepository statusRepository;

    @Autowired
    public ProductionServiceImpl(ProductionRepository productionRepository, ExtremelyRepository extremelyRepository, CompanyService companyService, WarehouseService warehouseService, ArticleRepository articleRepository, WorkDetailsRepository workDetailsRepository, LocationRepository locationRepository, StockRepository stockRepository, StatusRepository statusRepository) {
        this.productionRepository = productionRepository;
        this.extremelyRepository = extremelyRepository;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.articleRepository = articleRepository;
        this.workDetailsRepository = workDetailsRepository;
        this.locationRepository = locationRepository;
        this.stockRepository = stockRepository;
        this.statusRepository = statusRepository;
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
    public void createProduction(Production production, String articleId, String chosenWarehouse, HttpSession session) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
        Long newProductionNumber =  Long.parseLong(LocalDateTime.now().format(formatter) + "001");
        productionSetters(production, chosenWarehouse,articleId);
        boolean workProductionStatus = false;
        // check extremely information
        if(extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),"Production_after_creation").getExtremelyValue().equals("2")){
            production.setStatus("Created");
            session.setAttribute("productionMessage","Production for finish product: " + articleRepository.getOne(Long.parseLong(articleId)).getArticle_number() + " was successfully created. Extremely flag set on 2, so works were not created");
        }
        if(extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),"Production_after_creation").getExtremelyValue().equals("1")){
            production.setStatus("Work pending");
            workProductionStatus = true;
            session.setAttribute("productionMessage","Production and works for finish product: " + articleRepository.getOne(Long.parseLong(articleId)).getArticle_number() + " was successfully created.");
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
                    log.error("Qty on stock: " + stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(),ia.getWarehouse().getName(),ia.getArticle().getArticle_number()).getPieces_qty());
                    log.error("Qty from form: " + ia.getQuantityForFinishedProduct());
                    if(stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(),ia.getWarehouse().getName(),ia.getArticle().getArticle_number()).getPieces_qty() - ia.getQuantityForFinishedProduct() >= 0) {
                        Stock stock = stockRepository.getStockById(stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(),ia.getWarehouse().getName(),ia.getArticle().getArticle_number()).getId());
                        saveProductionInLoop(production, ia);
                        workCreationInLoop(production, ia,stock);
                        changesOnStock(stock,ia);
                    }
                }
            }
             else{
                // only production creation
                for (IntermediateArticle ia : intermediateArticleList){
                    log.error("Qty on stock: " + stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(), ia.getWarehouse().getName(), ia.getArticle().getArticle_number()).getPieces_qty());
                    log.error("Qty from form: " + ia.getQuantityForFinishedProduct());
                    if(stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(),ia.getWarehouse().getName(),ia.getArticle().getArticle_number()).getPieces_qty() - ia.getQuantityForFinishedProduct() >= 0) {
                        Stock stock = stockRepository.getStockById(stockRepository.smallStockDataLimitedToOne(ia.getCompany().getName(),ia.getWarehouse().getName(),ia.getArticle().getArticle_number()).getId());
                        saveProductionInLoop(production, ia);
                        changesOnStock(stock,ia);
                    }
                }
            }
        }

    }

    @Override
    public List<ProductionRepository.ProductionHeader> getProductionHeaderByCriteria(String productionCompany, String chosenWarehouse, String productionFinishProductNumber, String productionIntermediateArticleNumber, String productionCreated, String productionLastUpdate, String productionStatus) {

        if(chosenWarehouse == null || chosenWarehouse.equals("")){
            chosenWarehouse = "%";
        }

        if(productionCompany == null || productionCompany.equals("")){
            productionCompany = "%";
        }

        if(productionFinishProductNumber == null || productionFinishProductNumber.equals("")){
            productionFinishProductNumber = "%";
        }
        if(productionIntermediateArticleNumber == null || productionIntermediateArticleNumber.equals("")){
            productionIntermediateArticleNumber = "%";
        }
        if(productionCreated ==null || productionCreated.equals("")){
            productionCreated = "%";
        }
        if(productionStatus ==null || productionStatus.equals("")){
            productionStatus = "%";
        }
        if(productionLastUpdate ==null || productionLastUpdate.equals("")){
            productionLastUpdate = "%";
        }

        return productionRepository.getProductionHeaderByCriteria(productionCompany,chosenWarehouse,productionFinishProductNumber,productionIntermediateArticleNumber,productionCreated,productionLastUpdate,productionStatus);
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
    public void workCreationInLoop(Production production, IntermediateArticle ia,Stock stock){
        WorkDetails work = new WorkDetails();
        work.setPiecesQty(ia.getQuantityForFinishedProduct());
        work.setArticle(ia.getArticle());
        work.setCompany(ia.getCompany());
        work.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        work.setChangeBy(SecurityUtils.usernameForActivations());
        work.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        work.setWarehouse(ia.getWarehouse());
        work.setHdNumber(stock.getHd_number());
        work.setStatus("open");
        work.setFromLocation(stock.getLocation());
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

    public void changesOnStock(Stock stock, IntermediateArticle ia){
        Stock stockForProduction = new Stock();
        stockForProduction.setStatus(statusRepository.getStatusByStatusName("production_picking_pending","Production"));
        stockForProduction.setLocation(stock.getLocation());
        stockForProduction.setChangeBy(SecurityUtils.usernameForActivations());
        stockForProduction.setUnit(stock.getUnit());
        stockForProduction.setHd_number(stock.getHd_number() + 7000000000000L);
        stockForProduction.setCreated(TimeUtils.timeNowLong());
        stockForProduction.setArticle(stock.getArticle());
        stockForProduction.setCompany(stock.getCompany());
        stockForProduction.setWarehouse(stock.getWarehouse());
        stockForProduction.setPieces_qty(ia.getQuantityForFinishedProduct());
        stockForProduction.setReceptionNumber(stock.getReceptionNumber());
        stockForProduction.setComment("Original HD number: " + stock.getHd_number());
        stockForProduction.setShipmentNumber(stock.getShipmentNumber());
        stockForProduction.setLast_update(TimeUtils.timeNowLong());
        stockForProduction.setQuality(stock.getQuality());

        stock.setPieces_qty(stock.getPieces_qty() - ia.getQuantityForFinishedProduct());
        stock.setLast_update(TimeUtils.timeNowLong());
        stock.setChangeBy(SecurityUtils.usernameForActivations());
        stockRepository.save(stock);
        stockRepository.save(stockForProduction);
        //TODO to think about this functionality linked with deleting zeros
        if(stock.getPieces_qty() == 0){
            log.error("stock: " + stock.getId() + " " + stock.getHd_number() + " " + stock.getArticle().getArticle_number() + " " + " deleted");
            stockRepository.delete(stock);
        }
    }
}
