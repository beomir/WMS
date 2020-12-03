package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.ReceptionService;
import pl.coderslab.cls_wms_app.service.ShipmentService;
import pl.coderslab.cls_wms_app.service.StockService;
import pl.coderslab.cls_wms_app.service.WarehouseService;


import java.util.List;

@Controller
@RequestMapping("/stock")
public class StockController {
    private StockService stockService;
    private ShipmentService shipmentService;
    private ReceptionService receptionService;
    private WarehouseService warehouseService;


    @Autowired
    public StockController(StockService stockService, ShipmentService shipmentService, ReceptionService receptionService, WarehouseService warehouseService) {
        this.stockService = stockService;
        this.shipmentService = shipmentService;
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
    }

    @GetMapping("")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Stock> storage = stockService.getStorage(warehouseId);
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("stock", storage);
        model.addAttribute("warehouse", warehouse);
        return "stock";
    }

    @GetMapping("/receptionForm")
    public String receptionForm(Model model) {
        model.addAttribute("reception", new Reception());
        return "formReception";
    }

    @PostMapping("/receptionForm")
    public String addReception(Reception reception, Model model) {
        receptionService.add(reception);
        return "redirect:/reception";
    }

    @GetMapping("/shipmentForm")
    public String formShipment(Model model) {
        model.addAttribute("shipment", new Shipment());
        return "formShipment";
    }

    @PostMapping("/shipmentForm")
    public String addShipment(Shipment shipment, Model model) {
        shipmentService.add(shipment);
        return "redirect:/shipment";
    }


    @GetMapping("/form/{id}")
    public String updateStock(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);
        return "form";
    }

    @GetMapping("/delete/{id}")
    public String removeStock(@PathVariable Long id) {
        stockService.delete(id);
        return "redirect:/stock";
    }
}