package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;

import java.util.List;

@Repository
public interface StorageZoneRepository extends JpaRepository<StorageZone, Long> {

    @Query("Select s from StorageZone s join Warehouse w where w.name = ?1 order by s.storageZoneName")
    List<StorageZone> getStorageZoneByWarehouseName(String warehouseName);

    @Query("Select s from StorageZone s where s.active = false")
    List<StorageZone> getDeactivatedStorageZones();

    //for fixtures
    @Query("Select s from StorageZone s")
    List<StorageZone> getStorageZones();

    @Query("Select s from StorageZone s where s.storageZoneName = ?1 and s.warehouse.name = ?2")
    StorageZone findStorageZoneByStorageZoneName(String storageZoneName,String warehouseName);

}
