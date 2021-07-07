package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {


    @Query("Select l from Location l join Warehouse w where w.name = ?1 order by l.locationName")
    List<Location> getLocationByWarehouseName(String warehouseName);

    @Query("Select l from Location l where l.active = false")
    List<Location> getDeactivatedLocation();

    //for fixtures
    @Query("Select l from Location l where l.active = true")
    List<Location> getLocations();

    @Query("Select l from Location l where l.locationName = ?1 and l.warehouse.name = ?2")
    Location findLocationByLocationName(String locationName,String warehouseName);

    @Query("Select l from Location l where l.storageZone.storageZoneName = ?1")
    Location findLocationByStorageZoneName(String storageZone);

    @Query("Select l from Location l where l.locationName like ?1 and l.locationType like ?2 and l.storageZone.storageZoneName like ?3 and l.warehouse.name like ?4 and l.active = true")
    List<Location> findLocationsByCriteria(String locationName, String locationType, String storageZoneName, String warehouseName);


    @Query("Select l from Location l")
    List<Location> LocationsPlusStorageZone();

    @Query("Select l from Location l left join fetch l.stockList where l.warehouse.id = ?1")
    List<Location> locations(Long id);

    @Query("Select l from Location l where l.warehouse.id = ?1 and l.locationType = 'RDL'")
    List<Location> receptionDoorLocations(Long warehouseId);

    @Query(value = "Select location_name location from location l join warehouse w on l.warehouse_id = w.id where l.free_space = l.volume and l.free_weight = l.max_weight and l.location_type <> 'RDL' and l.location_type <> 'SDL' and w.name = ?4 union Select location_name location from location l join storage s on l.id = s.location_id join article a on s.article_id = a.id  join article_types t on a.article_types_id = t.id   join warehouse w on s.warehouse_id = w.id where t.mixed like ?1 and l.free_weight > ?2 and l.free_space > ?3 and w.name = ?4 and l.multi_item = true and l.location_type <> 'RDL' and l.location_type <> 'SDL'  order by 1 limit 1",nativeQuery = true)
    AvailableLocations getAvailableLocation(String articleType, double articleWeight, double articleVolume, String warehouseName );

    public static interface AvailableLocations {
        String getLocation();
    }

    @Query(value = "Select location_name location from location l left outer join  storage_zone sz on l.storage_zone_id = sz.id join warehouse w on l.warehouse_id = w.id where l.free_space = l.volume and l.free_weight = l.max_weight and l.location_type <> 'RDL' and l.location_type <> 'SDL' and w.name = ?4 and sz.storage_zone_name = ?5 union Select location_name location from location l join storage s on l.id = s.location_id join storage_zone sz on l.storage_zone_id = sz.id join article a on s.article_id = a.id  join article_types t on a.article_types_id = t.id   join warehouse w on s.warehouse_id = w.id where t.mixed like ?1 and l.free_weight > ?2 and l.free_space > ?3 and w.name = ?4 and sz.storage_zone_name = ?5 and l.multi_item = true and l.location_type <> 'RDL' and l.location_type <> 'SDL'  order by 1 limit 1",nativeQuery = true)
    AvailableLocationsForStorageZone getAvailableLocationForStorageZone(String articleType, double articleWeight, double articleVolume, String warehouseName,String storageZoneName );

    public static interface AvailableLocationsForStorageZone {
        String getLocation();
    }

    @Query(value = "Select location_name location, w.name warehouse, l.id id from location l join warehouse w on l.warehouse_id = w.id where l.location_type = 'PPL'",nativeQuery = true)
    List<ProductionLocations> getProductionLocations();

    public static interface ProductionLocations {
        String getLocation();
        String getWarehouse();
        Long getId();
    }

    @Query(value = "Select count(location_name) from location l inner join warehouse w on l.warehouse_id = w.id where location_name = ?1 and w.name = ?2 and location_type = 'EQL'",nativeQuery = true)
    int checkEquipment(String locationName,String scannerChosenWarehouse);
}
