package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.StorageZoneRepository;
import pl.coderslab.cls_wms_app.service.*;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UsersService usersService;
    private final WarehouseService warehouseService;
    private final StorageZoneRepository storageZoneRepository;
    public LocationSearch locationSearch;
    private final LocationNameConstruction locationNameConstruction;

    @Autowired
    public LocationController(LocationService locationService, UsersService usersService, WarehouseService warehouseService, StorageZoneRepository storageZoneRepository, LocationSearch locationSearch, LocationNameConstruction locationNameConstruction) {
        this.locationService = locationService;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
        this.storageZoneRepository = storageZoneRepository;
        this.locationSearch = locationSearch;
        this.locationNameConstruction = locationNameConstruction;
    }


    @GetMapping("/user/locations")
    public String locationList(Model model) {
        List<Location> locations;
        if(locationSearch.getLocationName() == null){
            locations = locationService.getLocations();
        }
        else{
            locations = locationService.getLocationsByAllCriteria(locationSearch.getLocationName(), locationSearch.getLocationType(), locationSearch.getStorageZoneName(), locationSearch.getWarehouse());
        }
        model.addAttribute("locations", locations);
        model.addAttribute("locationSearch",locationSearch);
        model.addAttribute("locationExistsMessage",locationNameConstruction.message);
        String token = usersService.FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "locations";
    }

    @GetMapping("/config/locationsDeactivatedList")
    public String locationDeactivatedList(Model model) {
        List<Location> locations = locationService.getDeactivatedLocations();
        model.addAttribute("locations", locations);
        return "locationsDeactivatedList";
    }


    @GetMapping("/user/formLocation")
    public String locationForm(Model model){
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<StorageZone> storageZones = storageZoneRepository.getStorageZones();
        model.addAttribute("storageZones", storageZones);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("location", new Location());
        model.addAttribute("locationNameConstruction", new LocationNameConstruction());
        usersService.loggedUserData(model);
        return "formLocation";
    }

    @PostMapping("/user/formLocation")
    public String locationAdd(Location location, LocationNameConstruction locationNameConstruction) {
        locationService.addLocation(location, locationNameConstruction);
        return "redirect:/user/locations";
    }

    @GetMapping("/deactivateLocation/{id}")
    public String deactivateLocation(@PathVariable Long id) {
        locationService.deactivate(id);
        return "redirect:/user/locations";
    }

    @GetMapping("/config/activateLocation/{id}")
    public String activateLocation(@PathVariable Long id) {
        locationService.activate(id);
        return "redirect:/config/locationsDeactivatedList";
    }

    @GetMapping("/config/removeLocation/{id}")
    public String deleteLocation(@PathVariable Long id) {
        //TODO if location is empty
        locationService.remove(id);
        return "redirect:/config/locationsDeactivatedList";
    }

    @GetMapping("/user/formEditLocation/{id}")
    public String updateLocation(@PathVariable Long id, Model model) {
        Location location = locationService.findById(id);
        model.addAttribute(location);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<StorageZone> storageZones = storageZoneRepository.getStorageZones();
        LocationNameConstruction lCN = locationService.lCN(location);
        model.addAttribute("locationNameConstruction", lCN);
        model.addAttribute("storageZones", storageZones);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "formEditLocation";
    }

    @PostMapping("/user/formEditLocation")
    public String edit(Location location, LocationNameConstruction locationNameConstruction) {
        locationService.editLocation(location, locationNameConstruction);
        return "redirect:/user/locations";
    }

    @GetMapping("/user/locations-browser")
    public String browser(Model model) {
        model.addAttribute("locationSearching", new LocationSearch());
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<StorageZone> storageZones = storageZoneRepository.getStorageZones();
        model.addAttribute("storageZones", storageZones);
        usersService.loggedUserData(model);
        return "locations-browser";
    }


    @PostMapping("/user/locations-browser")
    public String findLocation(LocationSearch locationSearching) {
        locationService.save(locationSearching);
        return "redirect:/user/locations";
    }

    @GetMapping("/user/formLocationPack")
    public String locationPack(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<StorageZone> storageZones = storageZoneRepository.getStorageZones();
        model.addAttribute("storageZones", storageZones);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("location", new Location());
        model.addAttribute("locationNameConstruction", new LocationNameConstruction());
        usersService.loggedUserData(model);
        return "formLocationPack";
    }

    @PostMapping("/user/formLocationPack")
    public String formLocationPack(Location location, LocationNameConstruction locationNameConstruction) {
        locationService.createLocationPack(location, locationNameConstruction);
        return "redirect:/user/locations";
    }

    @GetMapping("/user/addLocToStorageZones")
    public String addLocationToStorageZone(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<StorageZone> storageZones = storageZoneRepository.getStorageZones();
        model.addAttribute("storageZones", storageZones);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("location", new Location());
        model.addAttribute("locationNameConstruction", new LocationNameConstruction());
        usersService.loggedUserData(model);
        return "addLocToStorageZones";
    }

}
