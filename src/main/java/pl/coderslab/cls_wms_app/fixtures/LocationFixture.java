package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.LocationService;
import pl.coderslab.cls_wms_app.service.StorageZoneService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class LocationFixture {

    private final LocationService locationService;
    private final WarehouseService warehouseService;
    private final StorageZoneService storageZoneService;

    private List<Location> locationList = Arrays.asList(
            new Location(null, "FLP00101001","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Location(null, "FLP00101002","Picking floor location", "PFL",null,null,TimeUtils.timeNowLong(), TimeUtils.timeNowLong(),true,"system"),
            new Location(null, "RAP00101001","Picking rack location", "PRL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Location(null, "RAR00101001","Reserve rack location", "RRL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Location(null, "DOORREC01","Receiving door location", "RDL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Location(null, "DOORSHI01","Shipping door location", "SDL",null,null,TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system")
    );

    @Autowired
    public LocationFixture(LocationService locationService, WarehouseService warehouseService, StorageZoneService storageZoneService) {
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

        location1.setWarehouse(warehouseList.get(0));
        location2.setWarehouse(warehouseList.get(0));
        location3.setWarehouse(warehouseList.get(0));
        location4.setWarehouse(warehouseList.get(0));
        location5.setWarehouse(warehouseList.get(0));
        location6.setWarehouse(warehouseList.get(0));

        location1.setStorageZone(storageZoneList.get(0));
        location2.setStorageZone(storageZoneList.get(1));
        location3.setStorageZone(storageZoneList.get(2));
        location4.setStorageZone(storageZoneList.get(3));
        location4.setStorageZone(storageZoneList.get(4));
        location4.setStorageZone(storageZoneList.get(4));

        locationService.add(location1);
        locationService.add(location2);
        locationService.add(location3);
        locationService.add(location4);
    }
}
