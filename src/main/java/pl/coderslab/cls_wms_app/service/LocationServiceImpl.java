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
    public LocationSearch locationSearch;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository, LocationSearch locationSearch) {
        this.locationRepository = locationRepository;
        this.locationSearch = locationSearch;
    }

    @Override
    public void add(Location location) {
        locationRepository.save(location);
    }

    @Override
    public void addLocation(Location location) {
        locationDescription(location);
        location.setActive(true);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setCreated(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    @Override
    public void editLocation(Location location) {
        locationDescription(location);
        location.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        location.setChangeBy(SecurityUtils.usernameForActivations());
        locationRepository.save(location);
    }

    private void locationDescription(Location location) {
        if(location.getLocationType().equals("PFL")){
            location.setLocationDesc("Picking floor location");
        }
        if(location.getLocationType().equals("PRL")){
            location.setLocationDesc("Picking rack location");
        }
        if(location.getLocationType().equals("RRL")){
            location.setLocationDesc("Reserve rack location");
        }
        if(location.getLocationType().equals("RDL")){
            location.setLocationDesc("Receiving door location");
        }
        if(location.getLocationType().equals("SDL")){
            location.setLocationDesc("Shipping door location");
        }
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
    public void save(LocationSearch locationSearching) {
        locationSearch.setLocationName(locationSearching.getLocationName());
        locationSearch.setStorageZoneName(locationSearching.getStorageZoneName());
        locationSearch.setWarehouse(locationSearching.getWarehouse());
        locationSearch.setLocationType(locationSearching.getLocationType());
    }

    @Override
    public List<Location> getDeactivatedLocations() {
        return locationRepository.getDeactivatedLocation();
    }

    @Override
    public List<Location> getLocationsByAllCriteria(String locationName, String locationType, String storageZoneName, String warehouseName) {
        if(locationName.equals("")){
            locationName = "%";
        }
        if(storageZoneName.equals("")){
            storageZoneName = "%";
        }
        return locationRepository.findLocationsByCriteria(locationName,locationType,storageZoneName,warehouseName);
    }


}
