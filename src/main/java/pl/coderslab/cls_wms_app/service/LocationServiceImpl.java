package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.repository.LocationRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LocationServiceImpl implements LocationService{
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public void add(Location location) {
        locationRepository.save(location);
    }


    @Override
    public List<Location> getLocationByWarehouseName(String warehouseName) {
        return locationRepository.getLocationByWarehouseName(warehouseName);
    }

    //for fixtures
    @Override
    public List<Location> getLocations() {
        return locationRepository.getLocations();
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.getOne(id);
    }


    @Override
    public void deactivate(Long id) {
        Location location = locationRepository.getOne(id);
        location.setActive(false);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    @Override
    public void activate(Long id) {
        Location location = locationRepository.getOne(id);
        location.setActive(true);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    @Override
    public void remove(Long id) {
        locationRepository.deleteById(id);
    }

    @Override
    public List<Location> getDeactivatedLocations() {
        return locationRepository.getDeactivatedLocation();
    }


}
