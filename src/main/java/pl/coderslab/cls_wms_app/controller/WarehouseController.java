package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
public class WarehouseController {

    private WarehouseService warehouseService;
    private UsersService usersService;
    private CompanyService companyService;
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public WarehouseController(WarehouseService warehouseService, UsersService usersService, CompanyService companyService, CustomerUserDetailsService customerUserDetailsService) {
        this.warehouseService = warehouseService;
        this.usersService = usersService;
        this.companyService = companyService;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @GetMapping("/warehouse")
    public String list(Model model) {
        System.out.println(customerUserDetailsService.chosenWarehouse);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        List<Users> users = usersService.getUsers();
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("users", users);
        usersService.loggedUserData(model);
        return "warehouse";
    }


    @PostMapping("/stock")
    public String get(@RequestParam Long id, HttpSession session) {
        session.setAttribute("warehouseId", id);
        //saved session value to variable
        customerUserDetailsService.chosenWarehouse = id;
        return "redirect:/stock";
    }




    @GetMapping("/config/warehouseList")
    public String warehousesList(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "warehouseList";
    }

    @GetMapping("/config/warehouseDeactivatedList")
    public String warehouseDeactivatedList(Model model) {
        List<Warehouse> warehouseDeactivated = warehouseService.getDeactivatedWarehouse();
        model.addAttribute("warehouseDeactivated", warehouseDeactivated);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "warehouseDeactivatedList";
    }


    @GetMapping("/config/formWarehouseCreation")
    public String warehouseForm(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("warehouse", new Warehouse());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "formWarehouseCreation";
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
    public String updateWarehouse(@PathVariable Long id, Model model) {
        Warehouse warehouse = warehouseService.findById(id);
        model.addAttribute(warehouse);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "formEditWarehouse";
    }

    @PostMapping("/config/formEditWarehouse")
    public String edit(Warehouse warehouse) {
        warehouseService.add(warehouse);
        return "redirect:/config/warehouseList";
    }




}
