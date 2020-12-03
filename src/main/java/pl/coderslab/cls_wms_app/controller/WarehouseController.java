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

}
