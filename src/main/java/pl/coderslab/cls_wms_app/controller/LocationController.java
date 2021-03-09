package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.StorageZoneRepository;
import pl.coderslab.cls_wms_app.service.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UsersService usersService;
    private final WarehouseService warehouseService;
    private final StorageZoneRepository storageZoneRepository;

    @Autowired
    public LocationController(LocationService locationService, UsersService usersService, WarehouseService warehouseService, StorageZoneRepository storageZoneRepository) {
        this.locationService = locationService;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
        this.storageZoneRepository = storageZoneRepository;
    }


    @GetMapping("/user/locations")
    public String locationList(Model model) {
        List<Location> locations = locationService.getLocations();
        model.addAttribute("locations", locations);
        usersService.loggedUserData(model);
        return "locations";
    }

    @GetMapping("/config/locationDeactivatedList")
    public String locationDeactivatedList(Model model) {
        List<Location> locations = locationService.getDeactivatedLocations();
        model.addAttribute("locations", locations);
        return "locationDeactivatedList";
    }


    @GetMapping("/user/formLocation")
    public String locationForm(Model model){
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("location", new Location());
        usersService.loggedUserData(model);
        return "formLocation";
    }

    @PostMapping("formLocation")
    public String locationAdd(Location location) {
        locationService.add(location);
        return "redirect:/user/locations";
    }

    @GetMapping("/removeLocation/{id}")
    public String removeLocation(@PathVariable Long id) {
        locationService.remove(id);
        return "redirect:/locations";
    }

    @GetMapping("/config/activateLocation/{id}")
    public String activateLocation(@PathVariable Long id) {
        locationService.activate(id);
        return "redirect:/config/locationDeactivatedList";
    }

    @GetMapping("/user/formEditLocation/{id}")
    public String updateLocation(@PathVariable Long id, Model model) {
        Location location = locationService.findById(id);
        model.addAttribute(location);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "formEditLocation";
    }

    @PostMapping("formEditLocation")
    public String edit(Location location) {
        locationService.add(location);
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
    public String findTransaction(LocationSearch locationSearching) {
        locationService.save(locationSearching);
        return "redirect:/user/locations";
    }

}
