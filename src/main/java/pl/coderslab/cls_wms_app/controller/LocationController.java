package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.LocationService;
import pl.coderslab.cls_wms_app.service.UsersService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class LocationController {

    private final LocationService locationService;
    private final UsersService usersService;
    private final WarehouseService warehouseService;

    @Autowired
    public LocationController(LocationService locationService, UsersService usersService, WarehouseService warehouseService) {
        this.locationService = locationService;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
    }


    @GetMapping("location")
    public String locationList(Model model) {
        List<Location> locations = locationService.getLocations();
        model.addAttribute("locations", locations);
        usersService.loggedUserData(model);
        return "location";
    }

    @GetMapping("/config/locationDeactivatedList")
    public String locationDeactivatedList(Model model) {
        List<Location> locations = locationService.getDeactivatedLocations();
        model.addAttribute("locations", locations);
        return "locationDeactivatedList";
    }


    @GetMapping("formLocation")
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
        return "redirect:/location";
    }

    @GetMapping("/removeLocation/{id}")
    public String removeLocation(@PathVariable Long id) {
        locationService.remove(id);
        return "redirect:/location";
    }

    @GetMapping("/config/activateLocation/{id}")
    public String activateLocation(@PathVariable Long id) {
        locationService.activate(id);
        return "redirect:/config/locationDeactivatedList";
    }

    @GetMapping("/formEditLocation/{id}")
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
        return "redirect:/location";
    }

}
