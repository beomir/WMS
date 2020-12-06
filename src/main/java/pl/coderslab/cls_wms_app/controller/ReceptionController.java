package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.ArticleService;
import pl.coderslab.cls_wms_app.service.ReceptionService;
import pl.coderslab.cls_wms_app.service.VendorService;
import pl.coderslab.cls_wms_app.service.WarehouseService;

import java.util.List;

@Controller
@RequestMapping("/")
public class ReceptionController {

    private ReceptionService receptionService;
    private WarehouseService warehouseService;
    private ArticleService articleService;
    private VendorService vendorService;

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.articleService = articleService;
        this.vendorService = vendorService;
    }


    @GetMapping("reception")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Reception> receptions = receptionService.getReception(warehouseId);
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("receptions", receptions);
        model.addAttribute("warehouse", warehouse);
        return "reception";
    }

    @GetMapping("formReception")
    public String receptionForm(Model model){
        List<Article> articles = articleService.getArticle();
        List<Integer> pallets = receptionService.pallets();
        List<Vendor> vendors = vendorService.getVendor();
        model.addAttribute("lastReceptionNumber", receptionService.lastReception());
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("pallets", pallets);
        return "formReception";
    }

}
