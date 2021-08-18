package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.OrderOfLocationsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class OrderOfLocationHeaderFixture {

    private final OrderOfLocationsService orderOfLocationsService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;

    private List<OrderOfLocationsHeader> orderOfLocationsHeaderList = Arrays.asList(
            new OrderOfLocationsHeader(null, null,null,"StrojemPrimaryLocationsSequence",TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new OrderOfLocationsHeader(null, null,null,"StrojemSecondaryLocationsSequence",TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),false,"system")

    );

    @Autowired
    public OrderOfLocationHeaderFixture(OrderOfLocationsService orderOfLocationsService, WarehouseService warehouseService, CompanyService companyService) {
        this.orderOfLocationsService = orderOfLocationsService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouseList = warehouseService.getWarehouse();
        List<Company> companyList = companyService.getCompany();

        for (OrderOfLocationsHeader orderOfLocationsHeader : orderOfLocationsHeaderList) {
            orderOfLocationsService.addHeader(orderOfLocationsHeader);
        }

        for (OrderOfLocationsHeader orderOfLocationsHeader : orderOfLocationsHeaderList) {
            orderOfLocationsHeader.setCompany(companyList.get(0));
            orderOfLocationsHeader.setWarehouse(warehouseList.get(0));
        }

        for (int i = 0; i < orderOfLocationsHeaderList.size(); i++) {
            orderOfLocationsService.addHeader(orderOfLocationsHeaderList.get(i));
        }


    }
}
