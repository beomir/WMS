package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.storage.ArticleTypesService;
import pl.coderslab.cls_wms_app.service.storage.LocationService;
import pl.coderslab.cls_wms_app.service.storage.StorageZoneService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class LocationFixture {

    private final LocationService locationService;
    private final WarehouseService warehouseService;
    private final StorageZoneService storageZoneService;

    private List<Location> locationList = Arrays.asList(
            new Location(null, "FLP00101001","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,2171000,2850.0),
            new Location(null, "FLP00101002","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",true,true,200,240,120,5760000,3000.00,5040000,2700.0),
            new Location(null, "RAP00101001","Picking rack location", "PRL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",false,true,200,80,120,1920000,500.00,624000,150),
            new Location(null, "RAR00101001","Reserve rack location", "RRL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",false,true,200,80,120,1920000,500.00,1600000,100),
            new Location(null, "DOORREC01","Receiving door location", "RDL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",true,true,300,500,1000,150000000,3000.00,150000000,3000),
            new Location(null, "DOORSHI01","Shipping door location", "SDL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system",true,true,300,500,1000,150000000,3000.00,150000000,3000),
            new Location(null, "FLP00101003","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5440000,2600.0),
            new Location(null, "FLP00101004","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,3965500,2925.0),
            new Location(null, "FLP00101005","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5510000,2800.0),
            new Location(null, "FLP00101006","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5210000,2560.0),
            new Location(null, "FLP00101007","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,4320000,2400.0),
            new Location(null, "FLP00101008","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5759625,2977.5),
            new Location(null, "FLP00101009","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5497500,2790.0),
            new Location(null, "FLP00101010","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5440000,2600.0),
            new Location(null, "FLP00101011","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5072000,2140.0),
            new Location(null, "FLP00101012","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,3792000,540.0),
            new Location(null, "FLP00101013","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5760000,2800.0),
            new Location(null, "FLP00101014","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5760000,3000),
            new Location(null, "FLP00101015","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system",false,true,200,240,120,5760000,3000.00,5760000,3000)


    );

    @Autowired
    public LocationFixture(LocationService locationService, WarehouseService warehouseService, StorageZoneService storageZoneService, ArticleTypesService articleTypesService) {
        this.locationService = locationService;
        this.warehouseService = warehouseService;
        this.storageZoneService = storageZoneService;
    }

    public void loadIntoDB() {
        List<Warehouse> warehouseList = warehouseService.getWarehouse();
        List<StorageZone> storageZoneList = storageZoneService.getStorageZones();

        for (Location location : locationList) {
            locationService.add(location);
        }


        Location location1 = locationList.get(0);
        Location location2 = locationList.get(1);
        Location location3 = locationList.get(2);
        Location location4 = locationList.get(3);
        Location location5 = locationList.get(4);
        Location location6 = locationList.get(5);
        Location location7 = locationList.get(6);
        Location location8 = locationList.get(7);
        Location location9 = locationList.get(8);
        Location location10 = locationList.get(9);
        Location location11 = locationList.get(10);
        Location location12 = locationList.get(11);
        Location location13 = locationList.get(12);
        Location location14 = locationList.get(13);
        Location location15 = locationList.get(14);
        Location location16 = locationList.get(15);
        Location location17 = locationList.get(16);
        Location location18 = locationList.get(17);
        Location location19 = locationList.get(18);

        location1.setWarehouse(warehouseList.get(0));
        location2.setWarehouse(warehouseList.get(0));
        location3.setWarehouse(warehouseList.get(0));
        location4.setWarehouse(warehouseList.get(0));
        location5.setWarehouse(warehouseList.get(0));
        location6.setWarehouse(warehouseList.get(0));
        location7.setWarehouse(warehouseList.get(0));
        location8.setWarehouse(warehouseList.get(0));
        location9.setWarehouse(warehouseList.get(0));
        location10.setWarehouse(warehouseList.get(0));
        location11.setWarehouse(warehouseList.get(0));
        location12.setWarehouse(warehouseList.get(0));
        location13.setWarehouse(warehouseList.get(0));
        location14.setWarehouse(warehouseList.get(0));
        location15.setWarehouse(warehouseList.get(0));
        location16.setWarehouse(warehouseList.get(0));
        location17.setWarehouse(warehouseList.get(1));
        location18.setWarehouse(warehouseList.get(0));
        location19.setWarehouse(warehouseList.get(1));

        location1.setStorageZone(storageZoneList.get(0));
        location2.setStorageZone(storageZoneList.get(1));
        location3.setStorageZone(storageZoneList.get(2));
        location4.setStorageZone(storageZoneList.get(3));
        location5.setStorageZone(storageZoneList.get(4));
        location6.setStorageZone(storageZoneList.get(0));
        location7.setStorageZone(storageZoneList.get(0));
        location8.setStorageZone(storageZoneList.get(0));
        location9.setStorageZone(storageZoneList.get(0));
        location10.setStorageZone(storageZoneList.get(0));
        location11.setStorageZone(storageZoneList.get(0));
        location12.setStorageZone(storageZoneList.get(0));
        location13.setStorageZone(storageZoneList.get(0));
        location14.setStorageZone(storageZoneList.get(0));
        location15.setStorageZone(storageZoneList.get(0));
        location16.setStorageZone(storageZoneList.get(0));
        location17.setStorageZone(storageZoneList.get(0));
        location18.setStorageZone(storageZoneList.get(0));
        location19.setStorageZone(storageZoneList.get(0));


        int locationsFromFixtures = 19;
        for (int i = 0; i < locationsFromFixtures; i++) {
            locationService.add(locationList.get(i));
        }


    }
}
