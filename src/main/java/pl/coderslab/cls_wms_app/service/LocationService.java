package pl.coderslab.cls_wms_app.service;


import pl.coderslab.cls_wms_app.entity.Location;

import java.util.List;

public interface LocationService {

    void add(Location location);

    List<Location> getLocationByWarehouseName(String warehouseName);

    List<Location> getLocations(); //for fixtures

    List<Location> getDeactivatedLocations();

    Location findById(Long id);

    void deactivate(Long id);

    void activate(Long id);

    void remove(Long id);

}
