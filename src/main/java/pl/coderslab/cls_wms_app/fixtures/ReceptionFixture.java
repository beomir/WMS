package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class ReceptionFixture {

    private ReceptionService receptionService;
    private CompanyService companyService;
    private ArticleService articleService;
    private VendorService vendorService;
    private WarehouseService warehouseService;
    private UnitService unitService;


    private List<Reception> receptionList = Arrays.asList(
            new Reception(null, null,null,12L, null,202000000000000006L,"BW1",null,null, false,20200001L),
            new Reception(null, null,null,12L, null,202000000000000007L,"EU1",null,null, false,20200002L),
            new Reception(null, null,null,12L, null,202000000000000008L,"EU1",null,null, false,20200003L),
            new Reception(null, null,null,10L, null,202000000000000005L,"EU1",null,null,false,20200004L)
    );

    @Autowired
    public ReceptionFixture(ReceptionService receptionService, ArticleService articleService, VendorService vendorService, CompanyService companyService, WarehouseService warehouseService, UnitService unitService) {
        this.receptionService = receptionService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.unitService = unitService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Vendor> vendors = vendorService.getVendor();
        List<Article> articles = articleService.getArticle();
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Unit> unit = unitService.getUnit();
        Random rand = new Random();


        for (Reception reception : receptionList) {
            reception.setWarehouse(warehouses.get(rand.nextInt(warehouses.size())));
            receptionService.add(reception);
        }
        Reception reception1 = receptionList.get(0);
        Reception reception2 = receptionList.get(1);
        Reception reception3 = receptionList.get(2);
        Reception reception4 = receptionList.get(3);

        reception1.setCompany(companies.get(0));
        reception2.setCompany(companies.get(1));
        reception3.setCompany(companies.get(2));
        reception4.setCompany(companies.get(3));

        reception1.setArticle(articles.get(0));
        reception2.setArticle(articles.get(1));
        reception3.setArticle(articles.get(2));
        reception4.setArticle(articles.get(3));

        reception1.setVendor(vendors.get(0));
        reception2.setVendor(vendors.get(1));
        reception3.setVendor(vendors.get(2));
        reception4.setVendor(vendors.get(3));

        reception1.setUnit(unit.get(0));
        reception2.setUnit(unit.get(0));
        reception3.setUnit(unit.get(0));
        reception4.setUnit(unit.get(0));

        receptionService.add(reception1);
        receptionService.add(reception2);
        receptionService.add(reception3);
        receptionService.add(reception4);
    }
}
