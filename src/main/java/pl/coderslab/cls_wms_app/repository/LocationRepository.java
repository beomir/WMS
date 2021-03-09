package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Location;

import java.util.List;

@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

    @Query("Select l from Location l join Warehouse w where w.name = ?1 order by l.locationName")
    List<Location> getLocationByWarehouseName(String warehouseName);

    @Query("Select l from Location l where l.active = false")
    List<Location> getDeactivatedLocation();

    //for fixtures
    @Query("Select l from Location l")
    List<Location> getLocations();

    @Query("Select l from Location l where l.locationName = ?1")
    Location findLocationByLocationName(String locationName);

}
