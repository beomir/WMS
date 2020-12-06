package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.*;


import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class StockFixture {
    private StockService stockService;
    private CompanyService companyService;
    private WarehouseService warehouseService;
    private StatusService statusService;
    private ArticleService articleService;
    private UnitService unitService;

    private List<Stock> stockList = Arrays.asList(
            new Stock(null, 202000000000000000L, null, 24L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null),
            new Stock(null, 202000000000000001L, null, 10L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null),
            new Stock(null, 202000000000000002L, null,  18L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null),
            new Stock(null, 202000000000000003L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null),
            new Stock(null, 202000000000000004L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null)
    );


    @Autowired
    public StockFixture(StockService stockService, CompanyService companyService, WarehouseService warehouseService, StatusService statusService, ArticleService articleService, UnitService unitService) {
        this.stockService = stockService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Status> statuses = statusService.getStatus();
        List<Article> articles = articleService.getArticle();
        List<Unit> unit = unitService.getUnit();
        Random rand = new Random();

        for (Stock storage : stockList) {
            storage.setWarehouse(warehouses.get(rand.nextInt(warehouses.size())));
            storage.setStatus(statuses.get(rand.nextInt(statuses.size())));
            stockService.add(storage);
        }

        Stock stock1 = stockList.get(0);
        Stock stock2 = stockList.get(1);
        Stock stock3 = stockList.get(2);
        Stock stock4 = stockList.get(3);
        Stock stock5 = stockList.get(4);

        stock1.setArticle(articles.get(0));
        stock2.setArticle(articles.get(1));
        stock3.setArticle(articles.get(2));
        stock4.setArticle(articles.get(3));
        stock5.setArticle(articles.get(3));

        stock1.setCompany(companies.get(0));
        stock2.setCompany(companies.get(1));
        stock3.setCompany(companies.get(2));
        stock4.setCompany(companies.get(3));
        stock5.setCompany(companies.get(3));

        stock1.setUnit(unit.get(0));
        stock2.setUnit(unit.get(0));
        stock3.setUnit(unit.get(0));
        stock4.setUnit(unit.get(0));
        stock5.setUnit(unit.get(0));

        stockService.add(stock1);
        stockService.add(stock2);
        stockService.add(stock3);
        stockService.add(stock4);
        stockService.add(stock5);
    }


}
