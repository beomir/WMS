package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StorageZoneService;
import pl.coderslab.cls_wms_app.service.wmsSettings.IntermediateArticleService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ProductionArticleService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class IntermediateArticleFixture {

    private ArticleService articleService;
    private WarehouseService warehouseService;
    private ProductionArticleService productionArticleService;
    private IntermediateArticleService intermediateArticleService;
    private StorageZoneService storageZoneService;
    private CompanyService companyService;
    private LocationService locationService;


    private List<IntermediateArticle> intermediateArticleList = Arrays.asList(
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",10L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System"),
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",8L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System"),
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",8L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System"),
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",4L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System"),
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",4L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System"),
            new IntermediateArticle(null, null, null,new ArrayList<>(),null,null,null,"intermediate",1L,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System")
    );

    @Autowired
    public IntermediateArticleFixture(ArticleService articleService, WarehouseService warehouseService, ProductionArticleService productionArticleService, IntermediateArticleService intermediateArticleService, StorageZoneService storageZoneService, CompanyService companyService, LocationService locationService) {
        this.articleService = articleService;
        this.warehouseService = warehouseService;
        this.productionArticleService = productionArticleService;
        this.intermediateArticleService = intermediateArticleService;
        this.storageZoneService = storageZoneService;
        this.companyService = companyService;
        this.locationService = locationService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Company> companies = companyService.getCompany();
        List<StorageZone> storageZoneList = storageZoneService.getStorageZones();
        List<Article> articleList = articleService.getArticles();
        List<Location> locationList = locationService.getLocations();
        List<ProductionArticle> productionArticleList = productionArticleService.getProductionArticles();


        for (IntermediateArticle intermediateArticle : intermediateArticleList) {
            intermediateArticleService.add(intermediateArticle);
        }

        IntermediateArticle intermediateArticle1 = intermediateArticleList.get(0);
        IntermediateArticle intermediateArticle2 = intermediateArticleList.get(1);
        IntermediateArticle intermediateArticle3 = intermediateArticleList.get(2);
        IntermediateArticle intermediateArticle4 = intermediateArticleList.get(3);
        IntermediateArticle intermediateArticle5 = intermediateArticleList.get(4);
        IntermediateArticle intermediateArticle6 = intermediateArticleList.get(5);


        intermediateArticle1.setArticle(articleList.get(16));
        intermediateArticle2.setArticle(articleList.get(17));
        intermediateArticle3.setArticle(articleList.get(18));
        intermediateArticle4.setArticle(articleList.get(19));
        intermediateArticle5.setArticle(articleList.get(20));
        intermediateArticle6.setArticle(articleList.get(21));

        intermediateArticle1.setWarehouse(warehouses.get(0));
        intermediateArticle2.setWarehouse(warehouses.get(0));
        intermediateArticle3.setWarehouse(warehouses.get(0));
        intermediateArticle4.setWarehouse(warehouses.get(0));
        intermediateArticle5.setWarehouse(warehouses.get(0));
        intermediateArticle6.setWarehouse(warehouses.get(0));

        intermediateArticle1.setStorageZone(storageZoneList.get(9));
        intermediateArticle2.setStorageZone(storageZoneList.get(9));
        intermediateArticle3.setStorageZone(storageZoneList.get(9));
        intermediateArticle4.setStorageZone(storageZoneList.get(9));
        intermediateArticle5.setStorageZone(storageZoneList.get(9));
        intermediateArticle6.setStorageZone(storageZoneList.get(9));

        intermediateArticle1.setCompany(companies.get(0));
        intermediateArticle2.setCompany(companies.get(0));
        intermediateArticle3.setCompany(companies.get(0));
        intermediateArticle4.setCompany(companies.get(0));
        intermediateArticle5.setCompany(companies.get(0));
        intermediateArticle6.setCompany(companies.get(0));

        intermediateArticle1.setLocation(locationList.get(23));
        intermediateArticle2.setLocation(locationList.get(23));
        intermediateArticle3.setLocation(locationList.get(23));
        intermediateArticle4.setLocation(locationList.get(23));
        intermediateArticle5.setLocation(locationList.get(23));
        intermediateArticle6.setLocation(locationList.get(23));


        intermediateArticle1.getProductionArticle().add(productionArticleList.get(0));
        intermediateArticle2.getProductionArticle().add(productionArticleList.get(0));
        intermediateArticle3.getProductionArticle().add(productionArticleList.get(0));
        intermediateArticle4.getProductionArticle().add(productionArticleList.get(0));
        intermediateArticle5.getProductionArticle().add(productionArticleList.get(0));
        intermediateArticle6.getProductionArticle().add(productionArticleList.get(0));


        intermediateArticleService.add(intermediateArticle1);
        intermediateArticleService.add(intermediateArticle2);
        intermediateArticleService.add(intermediateArticle3);
        intermediateArticleService.add(intermediateArticle4);
        intermediateArticleService.add(intermediateArticle5);
        intermediateArticleService.add(intermediateArticle6);
    }
}
