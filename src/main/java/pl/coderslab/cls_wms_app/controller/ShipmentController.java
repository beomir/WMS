package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.ShipMethodService;
import pl.coderslab.cls_wms_app.service.ShipmentService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.List;

@Controller
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipMethodService shipMethodService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;

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
        return "redirect:/shipment/shipment";
    }

}
