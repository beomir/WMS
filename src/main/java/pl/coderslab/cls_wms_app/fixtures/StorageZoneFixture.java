package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.storage.StorageZoneService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

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
            new StorageZone(null, "Doors","Picking floor location", "type2",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Not assigned","Not assigned locations", "type0",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Production","Production put off", "type3",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Production Picking","Production picking", "type4",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Production","Production put off", "type3",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new StorageZone(null, "Production Picking","Production picking", "type4",null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system")

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
        StorageZone storageZone6 = storageZoneList.get(5);
        StorageZone storageZone7 = storageZoneList.get(6);
        StorageZone storageZone8 = storageZoneList.get(7);
        StorageZone storageZone9 = storageZoneList.get(8);
        StorageZone storageZone10 = storageZoneList.get(9);

        storageZone1.setWarehouse(warehouseList.get(0));
        storageZone2.setWarehouse(warehouseList.get(0));
        storageZone3.setWarehouse(warehouseList.get(0));
        storageZone4.setWarehouse(warehouseList.get(0));
        storageZone5.setWarehouse(warehouseList.get(0));
        storageZone6.setWarehouse(warehouseList.get(1));
        storageZone7.setWarehouse(warehouseList.get(0));
        storageZone8.setWarehouse(warehouseList.get(0));
        storageZone9.setWarehouse(warehouseList.get(1));
        storageZone10.setWarehouse(warehouseList.get(1));

        storageZoneService.add(storageZone1);
        storageZoneService.add(storageZone2);
        storageZoneService.add(storageZone3);
        storageZoneService.add(storageZone4);
        storageZoneService.add(storageZone5);
        storageZoneService.add(storageZone6);
        storageZoneService.add(storageZone7);
        storageZoneService.add(storageZone8);
        storageZoneService.add(storageZone9);
        storageZoneService.add(storageZone10);

    }
}
