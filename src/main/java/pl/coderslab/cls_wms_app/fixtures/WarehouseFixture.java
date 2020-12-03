package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
public class WarehouseFixture {
    private WarehouseService warehouseService;
    private List<Warehouse> warehouseList = Arrays.asList(
            new Warehouse(null, "Pantatoni","Wrocław","Sławka 13","83-444", "Poland",true,"2020-11-28:T10:00:00","2020-11-28:T10:00:00"),
            new Warehouse(null, "Pantatoni","Mielec","Rydza-Śmigłego 15","813-543", "Poland",true,"2020-11-28:T10:00:00","2020-11-28:T10:00:00")

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
