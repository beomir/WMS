package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.ArticleRepository;
import pl.coderslab.cls_wms_app.repository.ProductionRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;

import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;


import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@Slf4j
public class ProductionModuleController {

    private final ProductionRepository productionRepository;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final ArticleRepository articleRepository;
    private final StockRepository stockRepository;


    @Autowired
    public ProductionModuleController(ProductionRepository productionRepository, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, ArticleRepository articleRepository, StockRepository stockRepository) {
        this.productionRepository = productionRepository;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.articleRepository = articleRepository;
        this.stockRepository = stockRepository;
    }

    @GetMapping("production")
    public String list(Model model,
                       @SessionAttribute(required = false) String productionWarehouse, @SessionAttribute(required = false) String productionCompany,
                       @SessionAttribute(required = false) String productionFinishProductNumber, @SessionAttribute(required = false) String productionIntermediateArticleNumber,
                       @SessionAttribute(required = false) String productionCreated, @SessionAttribute(required = false) String productionStatus,
                       @SessionAttribute(required = false) String productionLastUpdate) {
        List<Production> productionList = productionRepository.getProductionByCriteria(productionCompany,productionWarehouse,productionFinishProductNumber,productionIntermediateArticleNumber,productionCreated,productionLastUpdate,productionStatus);
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company",company);
        model.addAttribute("productionList", productionList);
        model.addAttribute("productionWarehouse", productionWarehouse);
        usersService.loggedUserData(model);

        return "wmsOperations/production";
    }

    @GetMapping("/deleteProduction/{id}")
    public String removeProduction(@PathVariable Long id) {
//        workDetailsService.delete(id);
        return "redirect:/production";
    }

    @GetMapping("formEditProduction/{id}")
    public String updateProduction(@PathVariable Long id, Model model) {
//        WorkDetails workDetails = workDetailsService.findById(id);
//        model.addAttribute(workDetails);
//        model.addAttribute("localDateTime", LocalDateTime.now());
//        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
//        model.addAttribute("companys", companys);
//
//        usersService.loggedUserData(model);
        return "wmsOperations/formEditProduction";
    }

    @PostMapping("formEditUnit")
    public String editProduction(Production production) {
        //workDetailsService.add(workDetails);
        return "redirect:/production";
    }

    @GetMapping("production-browser")
    public String browser(Model model) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company", company);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        return "wmsOperations/production-browser";
    }

    @PostMapping("production-browser")
    public String findProduction(HttpSession session, String productionWarehouse, String productionCompany, String productionFinishProductNumber, String productionIntermediateArticleNumber, String productionCreated, String productionStatus, String productionLastUpdate) {
        session.setAttribute("productionWarehouse", productionWarehouse);
        session.setAttribute("productionCompany", productionCompany);
        session.setAttribute("productionFinishProductNumber", productionFinishProductNumber);
        session.setAttribute("productionIntermediateArticleNumber", productionIntermediateArticleNumber);
        session.setAttribute("productionCreated", productionCreated);
        session.setAttribute("productionStatus", productionStatus);
        session.setAttribute("productionLastUpdate", productionLastUpdate);
        return "redirect:/production";
    }

    //TODO correct it
    @GetMapping("startProducing")
    public String startProducing(Model model,@SessionAttribute(required = false) String chosenWarehouse) {
        log.error("Session chosenWarehouse: " + chosenWarehouse);
        if(chosenWarehouse == null){
            return "redirect:/selectWarehouse";
        }
        else{
            List<Article> articleFinishProductList = articleRepository.articleListByCompanyAndWarehouse(companyService.getOneCompanyByUsername(SecurityUtils.username()),chosenWarehouse);
            List<Stock> stockList = stockRepository.getStockForProductionArticleByCompanyAndWarehouse(companyService.getOneCompanyByUsername(SecurityUtils.username()),chosenWarehouse);
            List<Warehouse> warehouseList = warehouseService.getWarehouse();
            Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
            model.addAttribute("warehouseList",warehouseList);
            model.addAttribute("stockList", stockList);
            model.addAttribute("article", articleFinishProductList);
            model.addAttribute("company", company);
            model.addAttribute("productionWarehouse",chosenWarehouse);
            usersService.loggedUserData(model);

            return "wmsOperations/startProducing";
        }
    }

    @GetMapping("selectWarehouse")
    public String startProducingWHS(Model model) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        return "wmsOperations/selectWarehouse";
    }

    @PostMapping("selectWarehouse")
    public String startProducingWHSPost(HttpSession session,String chosenWarehouse) {
        session.setAttribute("chosenWarehouse", chosenWarehouse);
        return "redirect:/startProducing";
    }


}
