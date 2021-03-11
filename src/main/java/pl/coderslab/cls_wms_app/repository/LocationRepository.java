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

    @Query("Select l from Location l where l.locationName = ?1")
    Location findLocationByLocationName(String locationName);

    @Query("Select l from Location l where l.locationName like ?1 and l.locationType like ?2 and l.storageZone.storageZoneName like ?3 and l.warehouse.name like ?4 and l.active = true")
    List<Location> findLocationsByCriteria(String locationName, String locationType, String storageZoneName, String warehouseName);
}
