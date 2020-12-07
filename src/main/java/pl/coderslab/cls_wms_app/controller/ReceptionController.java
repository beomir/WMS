package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
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

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService, CompanyService companyService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
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
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Reception> openedReceptions = receptionService.openedReceptions(warehouseId,SecurityUtils.username());
        model.addAttribute("lastReceptionNumber", receptionService.lastReception());
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("pallets", pallets);
        model.addAttribute("openedReception", openedReceptions);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formReception";
    }

}
