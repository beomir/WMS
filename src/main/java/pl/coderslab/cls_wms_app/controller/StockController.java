package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.*;


import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;
    private ShipmentService shipmentService;
    private ReceptionService receptionService;
    private WarehouseService warehouseService;
    private CompanyService companyService;


    @Autowired
    public StockController(StockService stockService, ShipmentService shipmentService, ReceptionService receptionService, WarehouseService warehouseService, CompanyService companyService) {
        this.stockService = stockService;
        this.shipmentService = shipmentService;
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
    }

    @GetMapping("")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Stock> storage = stockService.getStorage(warehouseId,SecurityUtils.username());
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("stock", storage);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "stock";
    }

    @GetMapping("/receptionForm")
    public String receptionForm(Model model) {
        model.addAttribute("reception", new Reception());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formReception";
    }

    @PostMapping("/receptionForm")
    public String addReception(Reception reception) {
        receptionService.add(reception);
        return "redirect:/reception";
    }

    @GetMapping("/shipmentForm")
    public String formShipment(Model model) {
        model.addAttribute("shipment", new Shipment());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formShipment";
    }

    @PostMapping("/shipmentForm")
    public String addShipment(Shipment shipment) {
        shipmentService.add(shipment);
        return "redirect:/shipment";
    }


    @GetMapping("/form/{id}")
    public String updateStock(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String removeStock(@PathVariable Long id) {
        stockService.delete(id);
        return "redirect:/stock";
    }
}