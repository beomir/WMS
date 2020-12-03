package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.ReceptionService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.List;

@Controller
@RequestMapping("/reception")
public class ReceptionController {

    private ReceptionService receptionService;
    private WarehouseService warehouseService;

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
    }


    @GetMapping("")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Reception> receptions = receptionService.getReception(warehouseId);
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("receptions", receptions);
        model.addAttribute("warehouse", warehouse);
        return "reception";
    }

}
