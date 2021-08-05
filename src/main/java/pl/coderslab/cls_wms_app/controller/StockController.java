package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.ArticleTypesRepository;
import pl.coderslab.cls_wms_app.repository.LocationRepository;

import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.storage.StockServiceImpl;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;

import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.StatusService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Controller
public class StockController {
    private final StockService stockService;
    private final StockServiceImpl stockServiceImpl;
    private final UsersService usersService;

    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final StatusService statusService;
    private final ArticleService articleService;
    private final UnitService unitService;
    private CustomerUserDetailsService customerUserDetailsService;
    private final LocationRepository locationRepository;
    private final ArticleTypesRepository articleTypesRepository;
    private final ExtremelyService extremelyService;
    private final WorkDetailsService workDetailsService;



    @Autowired
    public StockController(StockService stockService, StockServiceImpl stockServiceImpl, UsersService usersService,WarehouseService warehouseService, CompanyService companyService, StatusService statusService, ArticleService articleService, UnitService unitService, CustomerUserDetailsService customerUserDetailsService, LocationRepository locationRepository, ArticleTypesRepository articleTypesRepository, ExtremelyService extremelyService, WorkDetailsService workDetailsService) {
        this.stockService = stockService;
        this.stockServiceImpl = stockServiceImpl;
        this.usersService = usersService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.statusService = statusService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.customerUserDetailsService = customerUserDetailsService;
        this.locationRepository = locationRepository;
        this.articleTypesRepository = articleTypesRepository;
        this.extremelyService = extremelyService;
        this.workDetailsService = workDetailsService;

    }

    @GetMapping("/stock")
    public String list(Model model, HttpSession session, @SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request,
                       @SessionAttribute(required = false) String stockMessage) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")){
            List<Stock> storage = stockService.getStorage(chosenWarehouse,SecurityUtils.username());
            Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
            model.addAttribute("stock", storage);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("stockMessage", stockMessage);
            usersService.loggedUserData(model,session);
            return "stock";
        }
            else{
            return "redirect:/selectWarehouse";
        }
    }

    //change status
    @GetMapping("/storage/formChangeStatus/{id}")
    public String updateStockChangeStatus(@PathVariable Long id, Model model,HttpSession session) {

        Stock stock = stockService.findById(id);

        List<Status> statuses = statusService.getStockStatuses();
        model.addAttribute("statuses", statuses);

        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeStatus";
    }

    @PostMapping("/storage/formChangeStatus")
    public String updateStockChangeStatusPost(Stock stock, String newStatus) {
        stockService.changeStatus(stock, newStatus);
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

        Stock stock = stockService.findById(id);
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);

        model.addAttribute(stock);
        usersService.loggedUserData(model,session);
        return "storage/formChangeUnit";
    }

    @PostMapping("/storage/formChangeUnit")
    public String updateStockChangeUnitPost(Stock stock, String newUnit) {
        stockService.changeUnit(stock,newUnit);
        return "redirect:/stock";
    }

    //Add Comment

    @GetMapping("/formAddComment/{id}")
    public String updateStockAddComment(@PathVariable Long id, Model model,HttpSession session)  {
        Stock stock = stockService.findById(id);
        model.addAttribute(stock);

        usersService.loggedUserData(model, session);
        return "storage/formAddComment";
    }

    @PostMapping("/formAddComment")
    public String updateStockAddCommentPost(Stock stock, String newComment) {
        log.error("newComment: " + newComment);
        log.error("stock comment: " + stock.getComment());
        stockService.changeComment(stock,newComment);
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
    public String stockForm(Model model,HttpSession session,@SessionAttribute(required = false) String chosenWarehouse){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);

        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);

        Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
        model.addAttribute("warehouse", warehouse);

        model.addAttribute("stock", new Stock());

        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);

        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("nextPalletNbr", extremelyService.nextPalletNbr());

        List<Location> locations = locationRepository.locations(warehouse.getId());
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
    public String transferFromFS(@PathVariable Long id,@SessionAttribute(required = false) String chosenWarehouse, Model model,HttpSession session) throws CloneNotSupportedException {
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);

        Stock stock = stockService.findById(id);
        model.addAttribute("stock", stock);

        Stock chosenStockPositional = (Stock) stock.clone();
        session.setAttribute("chosenStockPositional",chosenStockPositional);

        model.addAttribute("chosenStockPosition", chosenStockPositional);

        List<Unit> units = unitService.getUnit();
        Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("units", units);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("nextPalletNbr", extremelyService.nextPalletNbr());
        List<Location> locations = locationRepository.locations(warehouse.getId());
        model.addAttribute("locations", locations);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        model.addAttribute("locationN", stockServiceImpl.locationName);
        usersService.loggedUserData(model,session);
        return "storage/formTransfer";
    }

    @PostMapping("/storage/formTransfer")
    public String transferFromFSPost(Stock stock, String locationN,
                                     @SessionAttribute(required = false) Stock chosenStockPositional,
                                     String formTransfer,HttpSession session) {
        if(formTransfer.equals("2")){
            stockService.transfer(stock,locationN, chosenStockPositional);
            session.setAttribute("stockMessage","Goods transferred from: " + chosenStockPositional.getLocation().getLocationName() + " to: " + locationN);
            return "redirect:/stock";
        }
        else if(formTransfer.equals("1") && stock.getStatus().getStatus().equals("on_hand")){
            workDetailsService.createTransferWork(chosenStockPositional, stock, locationN);
            session.setAttribute("stockMessage","Transfer Work number: " + chosenStockPositional.getHandle() + " created");
            return "redirect:/stock";
        }
        else{
            session.setAttribute("formTransferMessage","Transfer not possible because of wrong stock status");
            return "redirect:/formTransfer";
        }

    }

}