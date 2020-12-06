package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.UsersService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("")
public class WarehouseController {

    private WarehouseService warehouseService;
    private UsersService usersService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService, UsersService usersService) {
        this.warehouseService = warehouseService;
        this.usersService = usersService;
    }

    @GetMapping("/warehouse")
    public String list(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Users> users = usersService.getUsers();
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("users", users);
        return "warehouse";
    }


    @PostMapping("/stock")
    public String get(@RequestParam Long id, HttpSession session) {
        session.setAttribute("warehouseId", id);
        return "redirect:/stock";
    }




    @GetMapping("/warehouseList")
    public String warehousesList(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        return "/warehouseList";
    }

    @GetMapping("/warehouseDeactivatedList")
    public String warehouseDeactivatedList(Model model) {
        List<Warehouse> warehouseDeactivated = warehouseService.getDeactivatedWarehouse();
        model.addAttribute("warehouseDeactivated", warehouseDeactivated);
        return "/warehouseDeactivatedList";
    }


    @GetMapping("/formWarehouseCreation")
    public String warehouseForm(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("warehouse", new Warehouse());
        return "formWarehouseCreation";
    }

    @PostMapping("formWarehouseCreation")
    public String warehouseAdd(Warehouse warehouse) {
        warehouseService.add(warehouse);
        return "redirect:/warehouseList";
    }

    @GetMapping("/deleteWarehouse/{id}")
    public String removeWarehouse(@PathVariable Long id) {
        warehouseService.delete(id);
        return "redirect:/warehouseList";
    }

    @GetMapping("/activateWarehouse/{id}")
    public String activateWarehouse(@PathVariable Long id) {
        warehouseService.activate(id);
        return "redirect:/warehouseDeactivatedList";
    }

    @GetMapping("/formEditWarehouse/{id}")
    public String updateWarehouse(@PathVariable Long id, Model model) {
        Warehouse warehouse = warehouseService.findById(id);
        model.addAttribute(warehouse);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formEditWarehouse";
    }

    @PostMapping("formEditWarehouse")
    public String edit(Warehouse warehouse) {
        warehouseService.add(warehouse);
        return "redirect:/warehouseList";
    }




}
