package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.StorageZoneService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class StorageZoneFixture {

    private final WarehouseService warehouseService;
    private final StorageZoneService storageZoneService;

    private List<StorageZone> storageZoneList = Arrays.asList(
            new StorageZone(null, "Picking Floor1","Picking floor zone1", "type1",null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Picking Floor2","Picking floor zone2", "type1",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Picking Rack","Picking floor zone2", "type1",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Reserve","Reserve zone", "type1",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Doors","Picking floor location", "type2",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system")
    );

    @Autowired
    public StorageZoneFixture(WarehouseService warehouseService, StorageZoneService storageZoneService) {
        this.warehouseService = warehouseService;
        this.storageZoneService = storageZoneService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouseList = warehouseService.getWarehouse();

        for (StorageZone storageZone : storageZoneList) {
            storageZoneService.add(storageZone);
        }

        StorageZone storageZone1 = storageZoneList.get(0);
        StorageZone storageZone2 = storageZoneList.get(1);
        StorageZone storageZone3 = storageZoneList.get(2);
        StorageZone storageZone4 = storageZoneList.get(3);
        StorageZone storageZone5 = storageZoneList.get(4);

        storageZone1.setWarehouse(warehouseList.get(0));
        storageZone2.setWarehouse(warehouseList.get(0));
        storageZone3.setWarehouse(warehouseList.get(0));
        storageZone4.setWarehouse(warehouseList.get(0));
        storageZone5.setWarehouse(warehouseList.get(0));

        storageZoneService.add(storageZone1);
        storageZoneService.add(storageZone2);
        storageZoneService.add(storageZone3);
        storageZoneService.add(storageZone4);
        storageZoneService.add(storageZone5);

    }
}
