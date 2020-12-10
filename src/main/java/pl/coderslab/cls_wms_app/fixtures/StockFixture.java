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
            new Stock(null, 202000000000000000L, null, 24L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000001L, null, 10L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000002L, null,  18L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000003L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000004L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000005L, null,  40L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000006L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000007L, null,  44L,null,null,"BW1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000008L, null,  20L,null,null,"BW1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000009L, null,  75L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000010L, null,  21L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000011L, null,  20L,null,null,"EU1",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000012L, null,  43L,null,null,"EU2",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null),
            new Stock(null, 202000000000000013L, null,  123L,null,null,"EU2",null,"2020-11-28:T10:00:00","2020-11-28:T10:00:00",null,null,null,null)
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
        List<Status> status = statusService.getStatus();
        List<Article> articles = articleService.getArticles();
        List<Unit> unit = unitService.getUnit();
        Random rand = new Random();

        for (Stock storage : stockList) {
            storage.setWarehouse(warehouses.get(rand.nextInt(warehouses.size())));
            stockService.add(storage);
        }

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
    }


}
