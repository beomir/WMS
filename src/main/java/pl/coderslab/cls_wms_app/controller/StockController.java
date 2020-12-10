package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.*;


import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("")
public class StockController {
    private StockService stockService;
    private ShipmentService shipmentService;
    private ReceptionService receptionService;
    private WarehouseService warehouseService;
    private CompanyService companyService;
    private StatusService statusService;
    private ArticleService articleService;
    private UnitService unitService;


    @Autowired
    public StockController(StockService stockService, ShipmentService shipmentService, ReceptionService receptionService, WarehouseService warehouseService, CompanyService companyService, StatusService statusService, ArticleService articleService, UnitService unitService) {
        this.stockService = stockService;
        this.shipmentService = shipmentService;
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
    }

    @GetMapping("/stock")
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

//change status
    @GetMapping("/formChangeStatus/{id}")
    public String updateStockChangeStatus(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        List<Status> statuses = statusService.getStatus();
        model.addAttribute("statuses", statuses);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formChangeStatus";
    }

    @PostMapping("formChangeStatus")
    public String updateStockChangeStatusPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }
    //change article number
    @GetMapping("/formChangeArticleNumber/{id}")
    public String updateStockChangeArticleNumber(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formChangeArticleNumber";
    }

    @PostMapping("formChangeArticleNumber")
    public String updateStockChangeArticleNumberPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }

    //Change Quantity

    @GetMapping("/formChangeQty/{id}")
    public String updateStockChangeQuantity(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formChangeQty";
    }

    @PostMapping("formChangeQty")
    public String updateStockChangeQuantityPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }

    //Change Quality

    @GetMapping("/formChangeQuality/{id}")
    public String updateStockChangeQuality(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formChangeQuality";
    }

    @PostMapping("formChangeQuality")
    public String updateStockChangeQualityPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }

    //Change Unit

    @GetMapping("/formChangeUnit/{id}")
    public String updateStockChangeUnit(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formChangeUnit";
    }

    @PostMapping("formChangeUnit")
    public String updateStockChangeUnitPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }

    //Add Comment

    @GetMapping("/formAddComment/{id}")
    public String updateStockAddComment(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formAddComment";
    }

    @PostMapping("formAddComment")
    public String updateStockAddCommentPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }


    //DeleteStock

//    @GetMapping("/delete/{id}")
//    public String removeStock(@PathVariable Long id) {
//        stockService.delete(id);
//        return "redirect:/stock";
//    }

    //Create Stock

    @GetMapping("formStock")
    public String stockForm(Model model,@SessionAttribute Long warehouseId){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Unit> units = unitService.getUnit();
        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("articles", articles);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("stock", new Stock());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("companys", companys);
        return "formStock";
    }

    @PostMapping("formStock")
    public String stockFormPost(Stock stock) {
        stockService.add(stock);
        return "redirect:/stock";
    }

}