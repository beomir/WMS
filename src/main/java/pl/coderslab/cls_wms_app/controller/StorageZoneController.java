package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.StorageZoneService;
import pl.coderslab.cls_wms_app.service.UsersService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class StorageZoneController {

    private final StorageZoneService storageZoneService;
    private final UsersService usersService;
    private final WarehouseService warehouseService;

    @Autowired
    public StorageZoneController(StorageZoneService storageZoneService, UsersService usersService, WarehouseService warehouseService) {
        this.storageZoneService = storageZoneService;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
    }


    @GetMapping("storageZones")
    public String storageZonesList(Model model) {
        List<StorageZone> storageZones = storageZoneService.getStorageZones();
        model.addAttribute("storageZones", storageZones);
        usersService.loggedUserData(model);
        return "storageZones";
    }

    @GetMapping("/config/storageZonesDeactivatedList")
    public String storageZonesDeactivatedList(Model model) {
        List<StorageZone> storageZones = storageZoneService.getDeactivatedStorageZones();
        model.addAttribute("storageZones", storageZones);
        return "storageZonesDeactivatedList";
    }


    @GetMapping("formStorageZones")
    public String storageZoneForm(Model model){
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("storageZones", new StorageZone());
        usersService.loggedUserData(model);
        return "formStorageZones";
    }

    @PostMapping("formStorageZones")
    public String storageZoneAdd(StorageZone storageZone) {
        storageZoneService.add(storageZone);
        return "redirect:/storageZones";
    }

    @GetMapping("/removeStorageZones/{id}")
    public String removeStorageZones(@PathVariable Long id) {
        storageZoneService.remove(id);
        return "redirect:/storageZones";
    }

    @GetMapping("/config/activateStorageZone/{id}")
    public String activateStorageZones(@PathVariable Long id) {
        storageZoneService.activate(id);
        return "redirect:/config/activateStorageZone";
    }

    @GetMapping("/formEditStorageZones/{id}")
    public String updateStorageZone(@PathVariable Long id, Model model) {
        StorageZone storageZone = storageZoneService.findById(id);
        model.addAttribute(storageZone);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "formEditStorageZones";
    }

    @PostMapping("formEditStorageZone")
    public String editStorageZone(StorageZone storageZone) {
        storageZoneService.add(storageZone);
        return "redirect:/storageZones";
    }

}
