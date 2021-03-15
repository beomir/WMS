package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class WarehouseFixture {
    private WarehouseService warehouseService;
    private List<Warehouse> warehouseList = Arrays.asList(
            new Warehouse(null,"WRO1" ,"Pantatoni","Wrocław","Sławka 13","83-444", "Poland",true, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Warehouse(null,"MIE1", "Pantatoni","Mielec","Rydza-Śmigłego 15","813-543", "Poland",true,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system")

    );

    @Autowired
    public WarehouseFixture(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public void loadIntoDB() {
        for (Warehouse warehouse : warehouseList) {
            warehouseService.add(warehouse);
        }
    }
}
