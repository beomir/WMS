package pl.coderslab.cls_wms_app.fixtures;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class ExtremelyFixture {

    private final ExtremelyService extremelyService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;

    private List<Extremely> extremelyList = Arrays.asList(
            new Extremely(null, "Location Scope", "Maximum of range locations creation by one time","50", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null)

    );

    @Autowired
    public ExtremelyFixture(ExtremelyService extremelyService, WarehouseService warehouseService, CompanyService companyService) {
        this.extremelyService = extremelyService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouseList = warehouseService.getWarehouse();
        List<Company> companyList = companyService.getCompany();

        for (Extremely extremely : extremelyList) {
            extremelyService.save(extremely);
        }

        Extremely extremely1 = extremelyList.get(0);
        extremely1.setCompany(companyList.get(0));
        extremely1.setWarehouse(warehouseList.get(0));

        extremelyService.save(extremely1);
    }
}
