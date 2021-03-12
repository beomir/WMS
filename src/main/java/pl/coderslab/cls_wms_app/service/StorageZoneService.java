package pl.coderslab.cls_wms_app.service;


import pl.coderslab.cls_wms_app.entity.StorageZone;

import java.util.List;

public interface StorageZoneService {

    void add(StorageZone storageZone);

    List<StorageZone> getStorageZonesByWarehouseName(String warehouseName);

    List<StorageZone> getStorageZones(); //for fixtures

    List<StorageZone> getDeactivatedStorageZones();

    StorageZone findById(Long id);

    void deactivate(Long id);

    void activate(Long id);

    void remove(Long id);

}
