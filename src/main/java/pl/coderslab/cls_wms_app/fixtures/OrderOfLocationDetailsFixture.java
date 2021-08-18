package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.OrderOfLocationsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class OrderOfLocationDetailsFixture {

    private final LocationService locationService;
    private final OrderOfLocationsService orderOfLocationsService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;

    private List<OrderOfLocationsDetails> orderOfLocationsDetailsList = Arrays.asList(
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null),
            new OrderOfLocationsDetails(null, null,null,null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),"system",null)

    );

    @Autowired
    public OrderOfLocationDetailsFixture(OrderOfLocationsService orderOfLocationsService, LocationService locationService, WarehouseService warehouseService, CompanyService companyService) {
        this.orderOfLocationsService = orderOfLocationsService;
        this.locationService = locationService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouseList = warehouseService.getWarehouse();
        List<Company> companyList = companyService.getCompany();
        List<Location> locationListWarehouseWro = locationService.getLocationsByWarehouse("WRO1");
        List<OrderOfLocationsHeader> orderOfLocationsHeadersList = orderOfLocationsService.orderOfLocationsHeaderList();

        for (OrderOfLocationsDetails orderOfLocationsDetails : orderOfLocationsDetailsList) {
            orderOfLocationsService.addDetails(orderOfLocationsDetails);
        }


        for (OrderOfLocationsDetails orderOfLocationsDetails : orderOfLocationsDetailsList) {
            orderOfLocationsDetails.setCompany(companyList.get(0));
            orderOfLocationsDetails.setWarehouse(warehouseList.get(0));
            orderOfLocationsDetails.setOrderOfLocationsHeader(orderOfLocationsHeadersList.get(0));
        }

        for (int i = 0; i < orderOfLocationsDetailsList.size(); i++) {
            orderOfLocationsDetailsList.get(i).setLocation(locationListWarehouseWro.get(i));
            orderOfLocationsDetailsList.get(i).setLocationNumberInSequence(Long.parseLong(String.valueOf(i+1)));
            orderOfLocationsService.addDetails(orderOfLocationsDetailsList.get(i));
        }


    }
}
