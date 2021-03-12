package pl.coderslab.cls_wms_app.service;


import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;

import java.util.List;

public interface LocationService {

    void add(Location location);

    void addLocation(Location location, LocationNameConstruction locationNameConstruction);

    void editLocation(Location location, LocationNameConstruction locationNameConstruction);

    void createLocationPack(Location location, LocationNameConstruction locationNameConstruction);

    List<Location> getLocationByWarehouseName(String warehouseName);

    List<Location> getLocations(); //for fixtures

    List<Location> getDeactivatedLocations();

    Location findById(Long id);

    void deactivate(Long id);

    void activate(Long id);

    void remove(Long id);

    void save(LocationSearch locationSearching);

    List<Location> getLocationsByAllCriteria(String locationName, String locationType, String storageZoneName, String warehouseName);

    LocationNameConstruction lCN(Location location);

}
