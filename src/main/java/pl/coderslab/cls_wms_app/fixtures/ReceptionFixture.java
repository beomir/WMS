package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionService;
import pl.coderslab.cls_wms_app.service.wmsValues.*;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class ReceptionFixture {

    private ReceptionService receptionService;
    private CompanyService companyService;
    private ArticleService articleService;
    private VendorService vendorService;
    private WarehouseService warehouseService;
    private UnitService unitService;
    private StatusService statusService;
    private LocationService locationService;


    private List<Reception> receptionList = Arrays.asList(
            new Reception(null, null,null,12L, null,202100000000000006L,"BW1",null,null, 20200001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,12L, null,202100000000000007L,"EU1",null,null, 20200002L,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,12L, null,202100000000000008L,"EU1",null,null, 20200003L,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,10L, null,202100000000000005L,"EU1",null,null,20200004L,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,10L, null,202000000000000000L,"EU1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,12L, null,202000000000000001L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,20L, null,202000000000000002L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,33L, null,202000000000000003L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,55L, null,202000000000000004L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,5L, null,202000000000000005L,"EU1",null,null, 20190002L ,null, TimeUtils.timeNowLong() + 1,TimeUtils.timeNowLong() + 1,"system",null),
            new Reception(null, null,null,64L, null,202000000000000006L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,31L, null,202000000000000007L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,2L, null,202000000000000008L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,17L, null,202000000000000009L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,35L, null,202000000000000010L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,19L, null,202000000000000011L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,21L, null,202000000000000012L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,88L, null,202000000000000013L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,77L, null,202000000000000014L,"BW1",null,null, 20190001L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000015L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000016L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000017L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000018L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000019L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000020L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000021L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null),
            new Reception(null, null,null,65L, null,202000000000000022L,"BW1",null,null, 20190002L ,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null)
    );

    @Autowired
    public ReceptionFixture(ReceptionService receptionService, ArticleService articleService, VendorService vendorService, CompanyService companyService, WarehouseService warehouseService, UnitService unitService, StatusService statusService, LocationService locationService) {
        this.receptionService = receptionService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.warehouseService = warehouseService;
        this.unitService = unitService;
        this.statusService = statusService;
        this.locationService = locationService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Vendor> vendors = vendorService.getVendors();
        List<Article> articles = articleService.getArticles();
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Unit> unit = unitService.getUnit();
        List<Status> status = statusService.getStatus();
        List<Location> locationList = locationService.getLocations();

        for (Reception reception : receptionList) {
            reception.setUnit(unit.get(0));
            receptionService.add(reception);
        }

        for (int i = 4; i < receptionList.size();i++) {
            receptionList.get(i).setLocation(locationList.get(4));
            receptionService.add(receptionList.get(i));
        }

        Reception reception1 = receptionList.get(0);
        Reception reception2 = receptionList.get(1);
        Reception reception3 = receptionList.get(2);
        Reception reception4 = receptionList.get(3);
        Reception reception5 = receptionList.get(4);
        Reception reception6 = receptionList.get(5);
        Reception reception7 = receptionList.get(6);
        Reception reception8 = receptionList.get(7);
        Reception reception9 = receptionList.get(8);
        Reception reception10 = receptionList.get(9);
        Reception reception11 = receptionList.get(10);
        Reception reception12 = receptionList.get(11);
        Reception reception13 = receptionList.get(12);
        Reception reception14 = receptionList.get(13);
        Reception reception15 = receptionList.get(14);
        Reception reception16 = receptionList.get(15);
        Reception reception17 = receptionList.get(16);
        Reception reception18 = receptionList.get(17);
        Reception reception19 = receptionList.get(18);

        reception1.setCompany(companies.get(0));
        reception2.setCompany(companies.get(1));
        reception3.setCompany(companies.get(2));
        reception4.setCompany(companies.get(3));
        reception5.setCompany(companies.get(0));
        reception6.setCompany(companies.get(0));
        reception7.setCompany(companies.get(0));
        reception8.setCompany(companies.get(0));
        reception9.setCompany(companies.get(0));
        reception10.setCompany(companies.get(0));
        reception11.setCompany(companies.get(0));
        reception12.setCompany(companies.get(0));
        reception13.setCompany(companies.get(0));
        reception14.setCompany(companies.get(0));
        reception15.setCompany(companies.get(0));
        reception16.setCompany(companies.get(0));
        reception17.setCompany(companies.get(0));
        reception18.setCompany(companies.get(0));
        reception19.setCompany(companies.get(0));

        reception1.setArticle(articles.get(0));
        reception2.setArticle(articles.get(1));
        reception3.setArticle(articles.get(2));
        reception4.setArticle(articles.get(3));
        reception5.setArticle(articles.get(0));
        reception6.setArticle(articles.get(0));
        reception7.setArticle(articles.get(0));
        reception8.setArticle(articles.get(0));
        reception9.setArticle(articles.get(0));
        reception10.setArticle(articles.get(0));
        reception11.setArticle(articles.get(0));
        reception12.setArticle(articles.get(0));
        reception13.setArticle(articles.get(0));
        reception14.setArticle(articles.get(0));
        reception15.setArticle(articles.get(0));
        reception16.setArticle(articles.get(0));
        reception17.setArticle(articles.get(0));
        reception18.setArticle(articles.get(0));
        reception19.setArticle(articles.get(0));

        reception1.setVendor(vendors.get(0));
        reception2.setVendor(vendors.get(1));
        reception3.setVendor(vendors.get(2));
        reception4.setVendor(vendors.get(3));
        reception5.setVendor(vendors.get(0));
        reception6.setVendor(vendors.get(0));
        reception7.setVendor(vendors.get(0));
        reception8.setVendor(vendors.get(0));
        reception9.setVendor(vendors.get(0));
        reception10.setVendor(vendors.get(0));
        reception11.setVendor(vendors.get(0));
        reception12.setVendor(vendors.get(0));
        reception13.setVendor(vendors.get(0));
        reception14.setVendor(vendors.get(0));
        reception15.setVendor(vendors.get(0));
        reception16.setVendor(vendors.get(0));
        reception17.setVendor(vendors.get(0));
        reception18.setVendor(vendors.get(0));
        reception19.setVendor(vendors.get(0));

        reception1.setWarehouse(warehouses.get(0));
        reception2.setWarehouse(warehouses.get(0));
        reception3.setWarehouse(warehouses.get(0));
        reception4.setWarehouse(warehouses.get(0));
        reception5.setWarehouse(warehouses.get(0));
        reception6.setWarehouse(warehouses.get(0));
        reception7.setWarehouse(warehouses.get(0));
        reception8.setWarehouse(warehouses.get(0));
        reception9.setWarehouse(warehouses.get(0));
        reception10.setWarehouse(warehouses.get(0));
        reception11.setWarehouse(warehouses.get(0));
        reception12.setWarehouse(warehouses.get(0));
        reception13.setWarehouse(warehouses.get(0));
        reception14.setWarehouse(warehouses.get(0));
        reception15.setWarehouse(warehouses.get(0));
        reception16.setWarehouse(warehouses.get(0));
        reception17.setWarehouse(warehouses.get(0));
        reception18.setWarehouse(warehouses.get(0));
        reception19.setWarehouse(warehouses.get(0));

        reception1.setStatus(status.get(5));
        reception2.setStatus(status.get(5));
        reception3.setStatus(status.get(5));
        reception4.setStatus(status.get(5));
        reception5.setStatus(status.get(8));
        reception6.setStatus(status.get(8));
        reception7.setStatus(status.get(8));
        reception8.setStatus(status.get(8));
        reception9.setStatus(status.get(8));
        reception10.setStatus(status.get(8));
        reception11.setStatus(status.get(8));
        reception12.setStatus(status.get(8));
        reception13.setStatus(status.get(8));
        reception14.setStatus(status.get(8));
        reception15.setStatus(status.get(8));
        reception16.setStatus(status.get(8));
        reception17.setStatus(status.get(8));
        reception18.setStatus(status.get(8));
        reception19.setStatus(status.get(8));

        receptionService.add(reception1);
        receptionService.add(reception2);
        receptionService.add(reception3);
        receptionService.add(reception4);
        receptionService.add(reception5);
        receptionService.add(reception6);
        receptionService.add(reception7);
        receptionService.add(reception8);
        receptionService.add(reception9);
        receptionService.add(reception10);
        receptionService.add(reception11);
        receptionService.add(reception12);
        receptionService.add(reception13);
        receptionService.add(reception14);
        receptionService.add(reception15);
        receptionService.add(reception16);
        receptionService.add(reception17);
        receptionService.add(reception18);
        receptionService.add(reception19);
    }
}
