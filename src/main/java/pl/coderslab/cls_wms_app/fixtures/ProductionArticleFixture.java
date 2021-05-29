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
            new ProductionArticle(null, null, null,null,"system", TimeUtils.timeNowLong(),"System",null,null)
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

        productionArticle1.setArticle(articleList.get(15));
        productionArticle1.setWarehouse(warehouses.get(0));
        productionArticle1.setStorageZone(storageZoneList.get(9));
        productionArticle1.setCompany(companies.get(0));
        productionArticle1.setLocation(locationList.get(23));

        productionArticleService.add(productionArticle1);

    }
}
