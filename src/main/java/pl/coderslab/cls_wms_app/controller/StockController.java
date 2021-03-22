package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.ArticleTypesRepository;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.StatusService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;


import java.time.LocalDateTime;
import java.util.List;

@Controller
public class StockController {
    private final StockService stockService;
    private final UsersService usersService;
    private final ReceptionService receptionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final StatusService statusService;
    private final ArticleService articleService;
    private final UnitService unitService;
    private CustomerUserDetailsService customerUserDetailsService;
    private final LocationRepository locationRepository;
    private final StockRepository stockRepository;
    private final ArticleTypesRepository articleTypesRepository;


    @Autowired
    public StockController(StockService stockService, UsersService usersService, ReceptionService receptionService, WarehouseService warehouseService, CompanyService companyService, StatusService statusService, ArticleService articleService, UnitService unitService, CustomerUserDetailsService customerUserDetailsService, LocationRepository locationRepository, StockRepository stockRepository, ArticleTypesRepository articleTypesRepository) {
        this.stockService = stockService;
        this.usersService = usersService;
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.locationRepository = locationRepository;

        this.stockRepository = stockRepository;
        this.articleTypesRepository = articleTypesRepository;
    }

    @GetMapping("/stock")
    public String list(Model model) {
        List<Stock> storage = stockService.getStorage(customerUserDetailsService.chosenWarehouse,SecurityUtils.username());
        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("stock", storage);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
            else{
                return "stock";
        }
    }

//change status
    @GetMapping("/storage/formChangeStatus/{id}")
    public String updateStockChangeStatus(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        List<Status> statuses = statusService.getStatus();
        model.addAttribute("statuses", statuses);
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formChangeStatus";
    }

    @PostMapping("/storage/formChangeStatus")
    public String updateStockChangeStatusPost(Stock stock) {
        stockService.changeStatus(stock);
        return "redirect:/stock";
    }
    //change article number
    @GetMapping("/storage/formChangeArticleNumber/{id}")
    public String updateStockChangeArticleNumber(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formChangeArticleNumber";
    }

    @PostMapping("/storage/formChangeArticleNumber")
    public String updateStockChangeArticleNumberPost(Stock stock) {
        stockService.changeArticleNumber(stock);
        return "redirect:/stock";
    }

    //Change Quantity

    @GetMapping("/storage/formChangeQty/{id}")
    public String updateStockChangeQuantity(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formChangeQty";
    }

    @PostMapping("/storage/formChangeQty")
    public String updateStockChangeQuantityPost(Stock stock) {
        stockService.changeQty(stock);
        return "redirect:/stock";
    }

    //Change Quality

    @GetMapping("/storage/formChangeQuality/{id}")
    public String updateStockChangeQuality(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formChangeQuality";
    }

    @PostMapping("/storage/formChangeQuality")
    public String updateStockChangeQualityPost(Stock stock) {
        stockService.changeQuality(stock);
        return "redirect:/stock";
    }

    //Change Unit

    @GetMapping("/storage/formChangeUnit/{id}")
    public String updateStockChangeUnit(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formChangeUnit";
    }

    @PostMapping("/storage/formChangeUnit")
    public String updateStockChangeUnitPost(Stock stock) {
        stockService.changeUnit(stock);
        return "redirect:/stock";
    }

    //Add Comment

    @GetMapping("/formAddComment/{id}")
    public String updateStockAddComment(@PathVariable Long id, Model model) {
        Stock stock = stockService.findById(id);
        customerUserDetailsService.chosenStockPosition = stock;
        model.addAttribute(stock);
        usersService.loggedUserData(model);
        return "storage/formAddComment";
    }

    @PostMapping("/formAddComment")
    public String updateStockAddCommentPost(Stock stock) {
        stockService.changeComment(stock);
        return "redirect:/stock";
    }


    //DeleteStock

//    @GetMapping("/delete/{id}")
//    public String removeStock(@PathVariable Long id) {
//        stockService.delete(id);
//        return "redirect:/stock";
//    }

    //Create Stock

    @GetMapping("/storage/formStock")
    public String stockForm(Model model){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Unit> units = unitService.getUnit();
        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("articles", articles);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("stock", new Stock());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("companys", companys);
        model.addAttribute("nextPalletNbr", receptionService.nextPalletNbr());
        List<Location> locations = locationRepository.locations();
        model.addAttribute("locations", locations);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        usersService.loggedUserData(model);
        return "storage/formStock";
    }

    @PostMapping("/storage/formStock")
    public String stockFormPost(Stock stock) {
        stockService.addNewStock(stock);
        return "redirect:/stock";
    }

}