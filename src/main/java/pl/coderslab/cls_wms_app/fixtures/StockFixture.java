package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.StatusService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
//@Profile("local")
public class StockFixture {
    private StockService stockService;
    private CompanyService companyService;
    private WarehouseService warehouseService;
    private StatusService statusService;
    private ArticleService articleService;
    private UnitService unitService;
    private LocationService locationService;

    private List<Stock> stockList = Arrays.asList(
            new Stock(null, 202000000000000000L, null, 10L,null,null,"EU1",null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000001L, null, 10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000002L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000003L, null,  20L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000004L, null,  20L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000005L, null,  5L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000006L, null,  20L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000007L, null,  44L,null,null,"BW1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000008L, null,  20L,null,null,"BW1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000009L, null,  75L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000010L, null,  21L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000011L, null,  20L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000012L, null,  43L,null,null,"EU2",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000013L, null,  123L,null,null,"EU2",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190001L,"system",null),
            new Stock(null, 202000000000000014L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),

            new Stock(null, 202000000000000015L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000016L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000017L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000018L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000019L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000020L, null,  1L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000021L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null),
            new Stock(null, 202000000000000022L, null,  10L,null,null,"EU1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,null,null,20190002L,"system",null)

    );


    @Autowired
    public StockFixture(StockService stockService, CompanyService companyService, WarehouseService warehouseService, StatusService statusService, ArticleService articleService, UnitService unitService, LocationService locationService) {
        this.stockService = stockService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.locationService = locationService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Status> status = statusService.getStatus();
        List<Article> articles = articleService.getArticles();
        List<Unit> unit = unitService.getUnit();
        List<Location> locations = locationService.getLocations();


        Stock stock1 = stockList.get(0);
        Stock stock2 = stockList.get(1);
        Stock stock3 = stockList.get(2);
        Stock stock4 = stockList.get(3);
        Stock stock5 = stockList.get(4);
        Stock stock6 = stockList.get(5);
        Stock stock7 = stockList.get(6);
        Stock stock8 = stockList.get(7);
        Stock stock9 = stockList.get(8);
        Stock stock10 = stockList.get(9);
        Stock stock11 = stockList.get(10);
        Stock stock12 = stockList.get(11);
        Stock stock13 = stockList.get(12);
        Stock stock14 = stockList.get(13);
        Stock stock15 = stockList.get(14);

        Stock stock16 = stockList.get(15);
        Stock stock17 = stockList.get(16);
        Stock stock18 = stockList.get(17);
        Stock stock19 = stockList.get(18);
        Stock stock20 = stockList.get(19);
        Stock stock21 = stockList.get(20);
        Stock stock22 = stockList.get(21);
        Stock stock23 = stockList.get(22);


        stock1.setArticle(articles.get(0));
        stock2.setArticle(articles.get(1));
        stock3.setArticle(articles.get(2));
        stock4.setArticle(articles.get(3));
        stock5.setArticle(articles.get(3));
        stock6.setArticle(articles.get(0));
        stock7.setArticle(articles.get(5));
        stock8.setArticle(articles.get(5));
        stock9.setArticle(articles.get(1));
        stock10.setArticle(articles.get(8));
        stock11.setArticle(articles.get(5));
        stock12.setArticle(articles.get(3));
        stock13.setArticle(articles.get(3));
        stock14.setArticle(articles.get(3));
        stock15.setArticle(articles.get(3));

        stock16.setArticle(articles.get(16));
        stock17.setArticle(articles.get(17));
        stock18.setArticle(articles.get(18));
        stock19.setArticle(articles.get(19));
        stock20.setArticle(articles.get(20));
        stock21.setArticle(articles.get(21));
        stock22.setArticle(articles.get(23));
        stock23.setArticle(articles.get(24));

        stock1.setWarehouse(warehouses.get(0));
        stock2.setWarehouse(warehouses.get(0));
        stock3.setWarehouse(warehouses.get(0));
        stock4.setWarehouse(warehouses.get(0));
        stock5.setWarehouse(warehouses.get(0));
        stock6.setWarehouse(warehouses.get(0));
        stock7.setWarehouse(warehouses.get(0));
        stock8.setWarehouse(warehouses.get(0));
        stock9.setWarehouse(warehouses.get(0));
        stock10.setWarehouse(warehouses.get(0));
        stock11.setWarehouse(warehouses.get(0));
        stock12.setWarehouse(warehouses.get(0));
        stock13.setWarehouse(warehouses.get(0));
        stock14.setWarehouse(warehouses.get(0));
        stock15.setWarehouse(warehouses.get(1));

        stock16.setWarehouse(warehouses.get(0));
        stock17.setWarehouse(warehouses.get(0));
        stock18.setWarehouse(warehouses.get(0));
        stock19.setWarehouse(warehouses.get(0));
        stock20.setWarehouse(warehouses.get(0));
        stock21.setWarehouse(warehouses.get(0));
        stock22.setWarehouse(warehouses.get(0));
        stock23.setWarehouse(warehouses.get(0));

        stock1.setLocation(locations.get(0));
        stock2.setLocation(locations.get(1));
        stock3.setLocation(locations.get(2));
        stock4.setLocation(locations.get(3));
        stock5.setLocation(locations.get(6));
        stock6.setLocation(locations.get(7));
        stock7.setLocation(locations.get(8));
        stock8.setLocation(locations.get(9));
        stock9.setLocation(locations.get(10));
        stock10.setLocation(locations.get(11));
        stock11.setLocation(locations.get(12));
        stock12.setLocation(locations.get(13));
        stock13.setLocation(locations.get(14));
        stock14.setLocation(locations.get(15));
        stock15.setLocation(locations.get(16));

        stock16.setLocation(locations.get(17));
        stock17.setLocation(locations.get(17));
        stock18.setLocation(locations.get(17));
        stock19.setLocation(locations.get(17));
        stock20.setLocation(locations.get(17));
        stock21.setLocation(locations.get(17));
        stock22.setLocation(locations.get(17));
        stock23.setLocation(locations.get(17));

        stock1.setCompany(companies.get(0));
        stock2.setCompany(companies.get(1));
        stock3.setCompany(companies.get(2));
        stock4.setCompany(companies.get(3));
        stock5.setCompany(companies.get(3));
        stock6.setCompany(companies.get(0));
        stock7.setCompany(companies.get(4));
        stock8.setCompany(companies.get(4));
        stock9.setCompany(companies.get(1));
        stock10.setCompany(companies.get(1));
        stock11.setCompany(companies.get(2));
        stock12.setCompany(companies.get(3));
        stock13.setCompany(companies.get(3));
        stock14.setCompany(companies.get(3));
        stock15.setCompany(companies.get(3));

        stock16.setCompany(companies.get(0));
        stock17.setCompany(companies.get(0));
        stock18.setCompany(companies.get(0));
        stock19.setCompany(companies.get(0));
        stock20.setCompany(companies.get(0));
        stock21.setCompany(companies.get(0));
        stock22.setCompany(companies.get(0));
        stock23.setCompany(companies.get(0));

        stock1.setUnit(unit.get(0));
        stock2.setUnit(unit.get(0));
        stock3.setUnit(unit.get(0));
        stock4.setUnit(unit.get(0));
        stock5.setUnit(unit.get(0));
        stock6.setUnit(unit.get(0));
        stock7.setUnit(unit.get(0));
        stock8.setUnit(unit.get(0));
        stock9.setUnit(unit.get(0));
        stock10.setUnit(unit.get(0));
        stock11.setUnit(unit.get(0));
        stock12.setUnit(unit.get(0));
        stock13.setUnit(unit.get(0));
        stock14.setUnit(unit.get(0));
        stock15.setUnit(unit.get(0));

        stock16.setUnit(unit.get(0));
        stock17.setUnit(unit.get(0));
        stock18.setUnit(unit.get(0));
        stock19.setUnit(unit.get(0));
        stock20.setUnit(unit.get(0));
        stock21.setUnit(unit.get(0));
        stock22.setUnit(unit.get(0));
        stock23.setUnit(unit.get(0));

        stock1.setStatus(status.get(0));
        stock2.setStatus(status.get(0));
        stock3.setStatus(status.get(0));
        stock4.setStatus(status.get(0));
        stock5.setStatus(status.get(0));
        stock6.setStatus(status.get(0));
        stock7.setStatus(status.get(0));
        stock8.setStatus(status.get(0));
        stock9.setStatus(status.get(0));
        stock10.setStatus(status.get(0));
        stock11.setStatus(status.get(0));
        stock12.setStatus(status.get(0));
        stock13.setStatus(status.get(0));
        stock14.setStatus(status.get(0));
        stock15.setStatus(status.get(0));

        stock16.setStatus(status.get(0));
        stock17.setStatus(status.get(0));
        stock18.setStatus(status.get(0));
        stock19.setStatus(status.get(0));
        stock20.setStatus(status.get(0));
        stock21.setStatus(status.get(0));
        stock22.setStatus(status.get(0));
        stock23.setStatus(status.get(0));

        stockService.add(stock1);
        stockService.add(stock2);
        stockService.add(stock3);
        stockService.add(stock4);
        stockService.add(stock5);
        stockService.add(stock6);
        stockService.add(stock7);
        stockService.add(stock8);
        stockService.add(stock9);
        stockService.add(stock10);
        stockService.add(stock11);
        stockService.add(stock12);
        stockService.add(stock13);
        stockService.add(stock14);
        stockService.add(stock15);

        stockService.add(stock16);
        stockService.add(stock17);
        stockService.add(stock18);
        stockService.add(stock19);
        stockService.add(stock20);
        stockService.add(stock21);
        stockService.add(stock22);
        stockService.add(stock23);
    }


}
