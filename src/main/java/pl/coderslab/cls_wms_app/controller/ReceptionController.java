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
@RequestMapping("/")
public class ReceptionController {

    private ReceptionService receptionService;
    private WarehouseService warehouseService;
    private ArticleService articleService;
    private VendorService vendorService;
    private CompanyService companyService;
    private UnitService unitService;

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService, CompanyService companyService, UnitService unitService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.unitService = unitService;
    }


    @GetMapping("reception")
    public String list(Model model,@SessionAttribute Long warehouseId) {
        List<Reception> receptions = receptionService.getReceptions(warehouseId,SecurityUtils.username());
        List<Warehouse> warehouse = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("receptions", receptions);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "reception";
    }

    @GetMapping("formReception")
    public String receptionForm(Model model,@SessionAttribute Long warehouseId){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Reception> openedReceptions = receptionService.openedReceptions(warehouseId,SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        int qtyOfOpenedReceptions = receptionService.qtyOfOpenedReceptions(warehouseId,SecurityUtils.username());
        model.addAttribute("lastReceptionNumber", receptionService.lastReception());
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        model.addAttribute("qtyOfOpenedReceptions", qtyOfOpenedReceptions);
        model.addAttribute("openedReception", openedReceptions);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formReception";
    }

    @PostMapping("formReception")
    public String receptionAdd(Reception reception) {
        receptionService.add(reception);
        receptionService.getCreatedReceptionById(reception.getReceptionNumber());
        receptionService.updateCloseCreationValue(reception.getReceptionNumber());
        return "redirect:/reception";
    }

    @GetMapping("/finishedReception/{id}")
    public String finishedReception(@PathVariable Long id) {
        receptionService.finished(id);
        return "redirect:/reception";
    }

    @GetMapping("/closeCreationReception/{id}")
    public String closeCreationReception(@PathVariable Long id) {
        receptionService.closeCreation(id);
        return "redirect:/reception";
    }

    @GetMapping("/openCreationReception/{id}")
    public String openCreationReception(@PathVariable Long id) {
        receptionService.openCreation(id);
        return "redirect:/reception";
    }

    //edit

    @GetMapping("/editReceptionLine/{id}")
    public String updateReception(@PathVariable Long id, Model model,@SessionAttribute Long warehouseId) {
        Reception reception = receptionService.findById(id);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        model.addAttribute(reception);
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "editReceptionLine";
    }

    @PostMapping("editReceptionLine")
    public String updateReceptionPost(Reception reception) {
        receptionService.add(reception);
        receptionService.getCreatedReceptionById(reception.getReceptionNumber());
        receptionService.updateCloseCreationValue(reception.getReceptionNumber());
        return "redirect:/reception";
    }

}
