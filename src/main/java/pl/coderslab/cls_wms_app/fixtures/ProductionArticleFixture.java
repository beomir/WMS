package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StorageZoneService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ProductionArticleService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class ProductionArticleFixture {

    private ArticleService articleService;
    private WarehouseService warehouseService;
    private ProductionArticleService productionArticleService;
    private StorageZoneService storageZoneService;
    private CompanyService companyService;
    private LocationService locationService;


    private List<ProductionArticle> productionArticleList = Arrays.asList(
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"finish product","6",35L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",10L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",8L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",8L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",4L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",4L),
            new ProductionArticle(null, null, null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"System",null,null,"intermediate","1122334455747896",1L)
    );

    @Autowired
    public ProductionArticleFixture(ArticleService articleService, WarehouseService warehouseService, ProductionArticleService productionArticleService, StorageZoneService storageZoneService, CompanyService companyService, LocationService locationService) {
        this.articleService = articleService;
        this.warehouseService = warehouseService;
        this.productionArticleService = productionArticleService;
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


        for (ProductionArticle productionArticle : productionArticleList) {
            productionArticleService.add(productionArticle);
        }

        ProductionArticle productionArticle1 = productionArticleList.get(0);
        ProductionArticle productionArticle2 = productionArticleList.get(1);
        ProductionArticle productionArticle3 = productionArticleList.get(2);
        ProductionArticle productionArticle4 = productionArticleList.get(3);
        ProductionArticle productionArticle5 = productionArticleList.get(4);
        ProductionArticle productionArticle6 = productionArticleList.get(5);
        ProductionArticle productionArticle7 = productionArticleList.get(6);

        productionArticle1.setArticle(articleList.get(15));
        productionArticle2.setArticle(articleList.get(16));
        productionArticle3.setArticle(articleList.get(17));
        productionArticle4.setArticle(articleList.get(18));
        productionArticle5.setArticle(articleList.get(19));
        productionArticle6.setArticle(articleList.get(20));
        productionArticle7.setArticle(articleList.get(21));


        productionArticle1.setWarehouse(warehouses.get(0));
        productionArticle2.setWarehouse(warehouses.get(0));
        productionArticle3.setWarehouse(warehouses.get(0));
        productionArticle4.setWarehouse(warehouses.get(0));
        productionArticle5.setWarehouse(warehouses.get(0));
        productionArticle6.setWarehouse(warehouses.get(0));
        productionArticle7.setWarehouse(warehouses.get(0));

        productionArticle1.setStorageZone(storageZoneList.get(9));
        productionArticle2.setStorageZone(storageZoneList.get(9));
        productionArticle3.setStorageZone(storageZoneList.get(9));
        productionArticle4.setStorageZone(storageZoneList.get(9));
        productionArticle5.setStorageZone(storageZoneList.get(9));
        productionArticle6.setStorageZone(storageZoneList.get(9));
        productionArticle7.setStorageZone(storageZoneList.get(9));

        productionArticle1.setCompany(companies.get(0));
        productionArticle2.setCompany(companies.get(0));
        productionArticle3.setCompany(companies.get(0));
        productionArticle4.setCompany(companies.get(0));
        productionArticle5.setCompany(companies.get(0));
        productionArticle6.setCompany(companies.get(0));
        productionArticle7.setCompany(companies.get(0));

        productionArticle1.setLocation(locationList.get(23));
        productionArticle2.setLocation(locationList.get(23));
        productionArticle3.setLocation(locationList.get(23));
        productionArticle4.setLocation(locationList.get(23));
        productionArticle5.setLocation(locationList.get(23));
        productionArticle6.setLocation(locationList.get(23));
        productionArticle7.setLocation(locationList.get(23));

        productionArticleService.add(productionArticle1);
        productionArticleService.add(productionArticle2);
        productionArticleService.add(productionArticle3);
        productionArticleService.add(productionArticle4);
        productionArticleService.add(productionArticle5);
        productionArticleService.add(productionArticle6);
        productionArticleService.add(productionArticle7);

    }
}
