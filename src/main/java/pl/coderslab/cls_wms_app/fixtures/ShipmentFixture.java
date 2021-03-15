package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipMethodService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.wmsValues.*;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
//@Profile("local")
public class ShipmentFixture {

    private ShipmentService shipmentService;
    private CompanyService companyService;
    private ArticleService articleService;
    private CustomerService customerService;
    private ShipMethodService shipMethodService;
    private WarehouseService warehouseService;
    private UnitService unitService;
    private StatusService statusService;


    private List<Shipment> shipmentList = Arrays.asList(
            new Shipment(null, null,null,10L, null,202000000000000005L,"EU1",null,null,null, true,20200001L,true,null, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Shipment(null, null,null,12L, null,202000000000000006L,"BW1",null,null,null, true,20200002L,true,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Shipment(null, null,null,12L, null,202000000000000007L,"EU1",null,null,null, true,20200003L,true,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Shipment(null, null,null,12L, null,202000000000000008L,"EU1",null,null,null, true,20200004L,true,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")

    );

    @Autowired
    public ShipmentFixture(ShipmentService shipmentService, ArticleService articleService, CustomerService customerService, CompanyService companyService, ShipMethodService shipMethodService, WarehouseService warehouseService, UnitService unitService, StatusService statusService) {
        this.shipmentService = shipmentService;
        this.articleService = articleService;
        this.customerService = customerService;
        this.companyService = companyService;
        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.unitService = unitService;
        this.statusService = statusService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();
        List<Customer> customers = customerService.getCustomers();
        List<Article> articles = articleService.getArticles();
        List<ShipMethod> shipMethods = shipMethodService.getShipMethod();
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Unit> unit = unitService.getUnit();
        List<Status> status = statusService.getStatus();
        Random rand = new Random();


        for (Shipment shipment : shipmentList) {
            shipment.setShipMethod(shipMethods.get(rand.nextInt(shipMethods.size())));
            shipment.setWarehouse(warehouses.get(rand.nextInt(warehouses.size())));
            shipmentService.add(shipment);

        }
        Shipment shipment1 = shipmentList.get(0);
        Shipment shipment2 = shipmentList.get(1);
        Shipment shipment3 = shipmentList.get(2);
        Shipment shipment4 = shipmentList.get(3);

        shipment1.setCompany(companies.get(0));
        shipment2.setCompany(companies.get(1));
        shipment3.setCompany(companies.get(2));
        shipment4.setCompany(companies.get(3));

        shipment1.setArticle(articles.get(0));
        shipment2.setArticle(articles.get(1));
        shipment3.setArticle(articles.get(2));
        shipment4.setArticle(articles.get(3));

        shipment1.setCustomer(customers.get(0));
        shipment2.setCustomer(customers.get(1));
        shipment3.setCustomer(customers.get(2));
        shipment4.setCustomer(customers.get(3));

        shipment1.setUnit(unit.get(0));
        shipment2.setUnit(unit.get(0));
        shipment3.setUnit(unit.get(0));
        shipment4.setUnit(unit.get(0));

        shipment1.setStatus(status.get(0));
        shipment2.setStatus(status.get(0));
        shipment3.setStatus(status.get(0));
        shipment4.setStatus(status.get(0));

        shipmentService.add(shipment1);
        shipmentService.add(shipment2);
        shipmentService.add(shipment3);
        shipmentService.add(shipment4);
    }
}
