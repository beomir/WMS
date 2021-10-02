package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class WarehouseController {

    private final WarehouseService warehouseService;
    private final UsersService usersService;
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WarehouseController(WarehouseService warehouseService, UsersService usersService, WarehouseRepository warehouseRepository) {
        this.warehouseService = warehouseService;
        this.usersService = usersService;
        this.warehouseRepository = warehouseRepository;
    }

    @GetMapping("/warehouse")
    public String list(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Users> users = usersService.getUsers();

        model.addAttribute("warehouses", warehouses);
        model.addAttribute("users", users);
        usersService.loggedUserData(model,session);
        return "wmsValues/warehouse/warehouse";
    }


    @PostMapping("/stock")
    public String get(@RequestParam Long id, HttpSession session) {
        session.setAttribute("warehouseId", id);
        session.setAttribute("chosenWarehouse",warehouseRepository.getOneWarehouse(id).getName());
        //saved session value to variable
        Warehouse warehouse = warehouseRepository.getOne(id);
        session.setAttribute("chosenWarehouse", warehouse.getName());
        return "redirect:/stock";
    }


    @GetMapping("/config/warehouseList")
    public String warehousesList(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);

        usersService.loggedUserData(model,session);
        return "wmsValues/warehouse/warehouseList";
    }

    @GetMapping("/config/warehouseDeactivatedList")
    public String warehouseDeactivatedList(Model model,HttpSession session) {
        List<Warehouse> warehouseDeactivated = warehouseService.getDeactivatedWarehouse();
        model.addAttribute("warehouseDeactivated", warehouseDeactivated);

        usersService.loggedUserData(model,session);
        return "wmsValues/warehouse/warehouseDeactivatedList";
    }


    @GetMapping("/config/formWarehouseCreation")
    public String warehouseForm(Model model,HttpSession session){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("warehouse", new Warehouse());

        usersService.loggedUserData(model,session);
        return "wmsValues/warehouse/formWarehouseCreation";
    }

    @PostMapping("/config/formWarehouseCreation")
    public String warehouseAdd(Warehouse warehouse) {
        warehouseService.add(warehouse);
        return "redirect:/config/warehouseList";
    }

    @GetMapping("/config/deleteWarehouse/{id}")
    public String removeWarehouse(@PathVariable Long id) {
        warehouseService.delete(id);
        return "redirect:/config/warehouseList";
    }

    @GetMapping("/config/activateWarehouse/{id}")
    public String activateWarehouse(@PathVariable Long id) {
        warehouseService.activate(id);
        return "redirect:/config/warehouseDeactivatedList";
    }

    @GetMapping("/config/formEditWarehouse/{id}")
    public String updateWarehouse(@PathVariable Long id, Model model, HttpSession session) {
        Warehouse warehouse = warehouseService.findById(id);
        model.addAttribute(warehouse);
        model.addAttribute("localDateTime", LocalDateTime.now());

        usersService.loggedUserData(model,session);
        return "wmsValues/warehouse/formEditWarehouse";
    }

    @PostMapping("/config/formEditWarehouse")
    public String edit(Warehouse warehouse) {
        warehouseService.add(warehouse);
        return "redirect:/config/warehouseList";
    }




}
