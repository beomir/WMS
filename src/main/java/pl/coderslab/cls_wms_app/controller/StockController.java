package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.ArticleTypesRepository;
import pl.coderslab.cls_wms_app.repository.ExtremelyRepository;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.storage.StockServiceImpl;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.StatusService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;


import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class StockController {
    private final StockService stockService;
    private final StockServiceImpl stockServiceImpl;
    private final UsersService usersService;
    private final ReceptionService receptionService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final StatusService statusService;
    private final ArticleService articleService;
    private final UnitService unitService;
    private CustomerUserDetailsService customerUserDetailsService;
    private final LocationRepository locationRepository;
    private final ArticleTypesRepository articleTypesRepository;
    private final ExtremelyService extremelyService;


    @Autowired
    public StockController(StockService stockService, StockServiceImpl stockServiceImpl, UsersService usersService, ReceptionService receptionService, WarehouseService warehouseService, CompanyService companyService, StatusService statusService, ArticleService articleService, UnitService unitService, CustomerUserDetailsService customerUserDetailsService, LocationRepository locationRepository, ArticleTypesRepository articleTypesRepository, ExtremelyService extremelyService) {
        this.stockService = stockService;
        this.stockServiceImpl = stockServiceImpl;
        this.usersService = usersService;
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.locationRepository = locationRepository;
        this.articleTypesRepository = articleTypesRepository;
        this.extremelyService = extremelyService;
    }

    @GetMapping("/stock")
    public String list(Model model, HttpSession session) {
        List<Stock> storage = stockService.getStorage(customerUserDetailsService.chosenWarehouse,SecurityUtils.username());
        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("stock", storage);
        model.addAttribute("warehouse", warehouse);

        usersService.loggedUserData(model,session);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
            else{
                return "stock";
        }
    }

    //change status
    @GetMapping("/storage/formChangeStatus/{id}")
    public String updateStockChangeStatus(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setStatusId(stock.getStatus().getId());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        List<Status> statuses = statusService.getStockStatuses();
        model.addAttribute("statuses", statuses);
        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeStatus";
    }

    @PostMapping("/storage/formChangeStatus")
    public String updateStockChangeStatusPost(Stock stock, ChosenStockPositional chosenStockPositionalStatus) {
        stockService.changeStatus(stock, chosenStockPositionalStatus);
        return "redirect:/stock";
    }
    //change article number
    @GetMapping("/storage/formChangeArticleNumber/{id}")
    public String updateStockChangeArticleNumber(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setArticleId(stock.getArticle().getId());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);
        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeArticleNumber";
    }

    @PostMapping("/storage/formChangeArticleNumber")
    public String updateStockChangeArticleNumberPost(Stock stock, ChosenStockPositional chosenStockPositionalStatus) {
        stockService.changeArticleNumber(stock,chosenStockPositionalStatus);
        return "redirect:/stock";
    }

    //Change Quantity

    @GetMapping("/storage/formChangeQty/{id}")
    public String updateStockChangeQuantity(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setPieces_qtyObj(stock.getPieces_qty());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        model.addAttribute(stock);
        log.error("GET chosenStockPositionalStatus: " + chosenStockPositionalStatus);
        usersService.loggedUserData(model,session);
        return "storage/formChangeQty";
    }

    @PostMapping("/storage/formChangeQty")
    public String updateStockChangeQuantityPost(Stock stock, ChosenStockPositional chosenStockPositionalStatus) {
        stockService.changeQty(stock,chosenStockPositionalStatus);
        return "redirect:/stock";
    }

    //Change Quality

    @GetMapping("/storage/formChangeQuality/{id}")
    public String updateStockChangeQuality(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setQualityObj(stock.getQuality());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeQuality";
    }

    @PostMapping("/storage/formChangeQuality")
    public String updateStockChangeQualityPost(Stock stock, ChosenStockPositional chosenStockPositionalStatus) {
        stockService.changeQuality(stock,chosenStockPositionalStatus);
        return "redirect:/stock";
    }

    //Change Unit

    @GetMapping("/storage/formChangeUnit/{id}")
    public String updateStockChangeUnit(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setUnitId(stock.getUnit().getId());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);
        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeUnit";
    }

    @PostMapping("/storage/formChangeUnit")
    public String updateStockChangeUnitPost(Stock stock, ChosenStockPositional chosenStockPositional) {
        stockService.changeUnit(stock,chosenStockPositional);
        return "redirect:/stock";
    }

    //Add Comment

    @GetMapping("/formAddComment/{id}")
    public String updateStockAddComment(@PathVariable Long id, Model model,HttpSession session) {
        ChosenStockPositional chosenStockPositionalStatus = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        chosenStockPositionalStatus.setCommentObj(stock.getComment());
        model.addAttribute("chosenStockPositionalStatus",chosenStockPositionalStatus);
        customerUserDetailsService.chosenStockPosition = stock;
        model.addAttribute(stock);
        usersService.loggedUserData(model, session);
        return "storage/formAddComment";
    }

    @PostMapping("/formAddComment")
    public String updateStockAddCommentPost(Stock stock, ChosenStockPositional chosenStockPositional) {
        stockService.changeComment(stock,chosenStockPositional);
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
    public String stockForm(Model model,HttpSession session){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Unit> units = unitService.getUnit();
        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("articles", articles);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("stock", new Stock());
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("nextPalletNbr", extremelyService.nextPalletNbr());
        List<Location> locations = locationRepository.locations(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("locations", locations);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        model.addAttribute("locationN", stockServiceImpl.locationName);
        usersService.loggedUserData(model, session);
        return "storage/formStock";
    }

    @PostMapping("/storage/formStock")
    public String stockFormPost(Stock stock,String locationN) {
        log.error("POST: " + locationN);
        stockService.addNewStock(stock,locationN);
        return "redirect:/stock";
    }

    //Transfer From FS
    @GetMapping("/storage/formTransfer/{id}")
    public String transferFromFS(@PathVariable Long id, Model model,HttpSession session) {
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);
        ChosenStockPositional chosenStockPositional = new ChosenStockPositional();
        Stock stock = stockService.findById(id);
        model.addAttribute("stock", stock);

        chosenStockPositional.setIdObj(stock.getId());
        chosenStockPositional.setChangeByObj(stock.getChangeBy());
        chosenStockPositional.setArticleId(stock.getArticle().getId());
        chosenStockPositional.setCommentObj(stock.getComment());
        chosenStockPositional.setCompanyId(stock.getCompany().getId());
        chosenStockPositional.setCreatedObj(stock.getCreated());
        chosenStockPositional.setHd_numberObj(stock.getHd_number());
        chosenStockPositional.setLocationId(stock.getLocation().getId());
        chosenStockPositional.setQualityObj(stock.getQuality());
        chosenStockPositional.setUnitId(stock.getUnit().getId());
        chosenStockPositional.setReceptionNumberObj(stock.getReceptionNumber());
        chosenStockPositional.setWarehouseId(stock.getWarehouse().getId());
        chosenStockPositional.setStatusId(stock.getStatus().getId());
        chosenStockPositional.setLast_updateObj(stock.getLast_update());
        chosenStockPositional.setPieces_qtyObj(stock.getPieces_qty());
        chosenStockPositional.setShipmentNumberObj(stock.getShipmentNumber());

        model.addAttribute("chosenStockPosition", chosenStockPositional);

        List<Unit> units = unitService.getUnit();
        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("nextPalletNbr", extremelyService.nextPalletNbr());
        List<Location> locations = locationRepository.locations(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("locations", locations);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        model.addAttribute("locationN", stockServiceImpl.locationName);
        usersService.loggedUserData(model,session);
        return "storage/formTransfer";
    }

    @PostMapping("/storage/formTransfer")
    public String transferFromFSPost(Stock stock, String locationN, ChosenStockPositional chosenStockPositional) {
        stockService.transfer(stock,locationN, chosenStockPositional);
        return "redirect:/stock";
    }

}