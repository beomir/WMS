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
            new Extremely(null, "Location Scope", "Maximum of range locations creation by one time","50", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null),
            new Extremely(null, "Production_module", "WMS module which gives possibility to use functionality connected with production finish product from intermediate articles","on", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null),
            new Extremely(null, "Production_after_creation", "Flag which give possibility to create works exact after declare produce in producingHeader - 1. To add next step for create works in production after declaration in producingHeader - 2 ","1", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null),
            new Extremely(null, "max_capacity_on_equipment", "Flag which give possibility to overload equipment during pick goods on equipment: 1 - overload available, 2 - overload not allowed","1", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null),
            new Extremely(null, "Scan_SP_loc_num", "Scanner stock preview search article in number of locations","5", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system",null,null)
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
        Extremely extremely2 = extremelyList.get(1);
        Extremely extremely3 = extremelyList.get(2);
        Extremely extremely4 = extremelyList.get(3);
        Extremely extremely5 = extremelyList.get(4);

        extremely1.setCompany(companyList.get(0));
        extremely2.setCompany(companyList.get(0));
        extremely3.setCompany(companyList.get(0));
        extremely4.setCompany(companyList.get(0));
        extremely5.setCompany(companyList.get(0));

        extremely1.setWarehouse(warehouseList.get(0));
        extremely2.setWarehouse(warehouseList.get(0));
        extremely3.setWarehouse(warehouseList.get(0));
        extremely4.setWarehouse(warehouseList.get(0));
        extremely5.setWarehouse(warehouseList.get(0));

        extremelyService.save(extremely1);
        extremelyService.save(extremely2);
        extremelyService.save(extremely3);
        extremelyService.save(extremely4);
        extremelyService.save(extremely5);
    }
}
