package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.ShipMethodService;
import pl.coderslab.cls_wms_app.service.ShipmentService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("")
public class ShipmentController {

    private ShipmentService shipmentService;
    private ShipMethodService shipMethodService;
    private WarehouseService warehouseService;
    private CompanyService companyService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ShipMethodService shipMethodService, WarehouseService warehouseService, CompanyService companyService) {
        this.shipmentService = shipmentService;
        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
    }

    @GetMapping("/shipment")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Shipment> shipments = shipmentService.getShipment(warehouseId);
        List<ShipMethod> shipMethod = shipMethodService.getShipMethod();
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("shipments", shipments);
        model.addAttribute("shipMethod", shipMethod);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "shipment";
    }

    @GetMapping("/finishedShipment/{id}")
    public String finishShipment(@PathVariable Long id) {
        Long getShipmentById = shipmentService.findById(id).getShipmentNumber();
        shipmentService.finishShipment(getShipmentById);
        return "redirect:/shipment";
    }

}
