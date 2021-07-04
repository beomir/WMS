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
import pl.coderslab.cls_wms_app.repository.ExtremelyRepository;
import pl.coderslab.cls_wms_app.repository.ProductionRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;

import pl.coderslab.cls_wms_app.service.wmsOperations.ProductionService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class ProductionModuleController {

    private final ProductionRepository productionRepository;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final ArticleRepository articleRepository;
    private final ProductionService productionService;
    private final ExtremelyRepository extremelyRepository;

    @Autowired
    public ProductionModuleController(ProductionRepository productionRepository, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, ArticleRepository articleRepository,  ProductionService productionService, ExtremelyRepository extremelyRepository) {
        this.productionRepository = productionRepository;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.articleRepository = articleRepository;
        this.productionService = productionService;
        this.extremelyRepository = extremelyRepository;
    }

    @GetMapping("production-browser")
    public String browser(Model model,HttpSession session) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company", company);

        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);

        usersService.loggedUserData(model, session);
        return "wmsOperations/production-browser";
    }

    @PostMapping("production-browser")
    public String findProduction(HttpSession session, String chosenWarehouse, String productionCompany, String productionFinishProductNumber, String productionIntermediateArticleNumber, String productionCreated, String productionStatus, String productionLastUpdate) {

        session.setAttribute("chosenWarehouse", chosenWarehouse);
        session.setAttribute("productionCompany", productionCompany);
        session.setAttribute("productionFinishProductNumber", productionFinishProductNumber);
        session.setAttribute("productionIntermediateArticleNumber", productionIntermediateArticleNumber);
        session.setAttribute("productionCreated", productionCreated);
        session.setAttribute("productionStatus", productionStatus);
        session.setAttribute("productionLastUpdate", productionLastUpdate);
        return "redirect:/production";
    }

    @GetMapping("production")
    public String list(Model model,
                       @SessionAttribute(required = false) String chosenWarehouse, @SessionAttribute(required = false) String productionCompany,
                       @SessionAttribute(required = false) String productionFinishProductNumber, @SessionAttribute(required = false) String productionIntermediateArticleNumber,
                       @SessionAttribute(required = false) String productionCreated, @SessionAttribute(required = false) String productionStatus,
                       @SessionAttribute(required = false) String productionLastUpdate,@SessionAttribute(required = false) String productionMessage,
                       HttpSession session) {


        List<ProductionRepository.ProductionHeader> productionList = productionService.getProductionHeaderByCriteria(productionCompany,chosenWarehouse,productionFinishProductNumber,productionIntermediateArticleNumber,productionCreated,productionLastUpdate,productionStatus);
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company",company);
        model.addAttribute("productionList", productionList);
        model.addAttribute("productionWarehouse", chosenWarehouse);

        model.addAttribute("productionCompany", productionCompany);
        model.addAttribute("productionFinishProductNumber", productionFinishProductNumber);
        model.addAttribute("productionIntermediateArticleNumber", productionIntermediateArticleNumber);
        model.addAttribute("productionCreated", productionCreated);
        model.addAttribute("productionStatus", productionStatus);
        model.addAttribute("productionLastUpdate", productionLastUpdate);

        model.addAttribute("productionMessage", productionMessage);

        log.info("productionCompany: " + productionCompany);
        log.info("chosenWarehouse: " + chosenWarehouse);
        log.info("productionFinishProductNumber: " + productionFinishProductNumber);
        log.info("productionIntermediateArticleNumber: " + productionIntermediateArticleNumber);
        log.info("productionCreated: " + productionCreated);
        log.info("productionLastUpdate: " + productionLastUpdate);
        log.info("productionStatus: " + productionStatus);
        log.info("productionMessage: " + productionMessage);

        session.setAttribute("productionMessage","");
        String userName = "";
        if(SecurityUtils.username().equals("%")){
            userName = "admin";
        }
        else {
            userName = SecurityUtils.username();
        }
        String token = usersService.FindUsernameByToken(userName);
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());

        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);

        return "wmsOperations/production";
    }

    @GetMapping("productionDetails/{productionNumber}")
    public String productionDetails(Model model,@PathVariable Long productionNumber,HttpSession session) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company",company);

        ProductionRepository.ProductionHeaderTitleInfo productionHeaderTitleInfo = productionRepository.getProductionHeaderTitleInfo(productionNumber);
        model.addAttribute("productionHeaderTitleInfo",productionHeaderTitleInfo);

        List<Production> productionList = productionRepository.getProductionListByNumber(productionNumber);
        model.addAttribute("productionList", productionList);

        usersService.loggedUserData(model, session);

        return "wmsOperations/productionDetails";
    }

    @GetMapping("startProducing")
    public String startProducing(HttpSession session, Model model, @SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request) {
        log.error("Session chosenWarehouse: " + chosenWarehouse);
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")){
            List<Article> articleFinishProductList = articleRepository.articleListByCompanyAndWarehouse(companyService.getOneCompanyByUsername(SecurityUtils.username()),chosenWarehouse);
            List<Warehouse> warehouseList = warehouseService.getWarehouse();
            Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
            model.addAttribute("warehouseList",warehouseList);
            model.addAttribute("article", articleFinishProductList);
            model.addAttribute("company", company);
            model.addAttribute("productionWarehouse",chosenWarehouse);
            usersService.loggedUserData(model,session);
            return "wmsOperations/startProducing";
        }
        else{
            return "redirect:/selectWarehouse";
        }

    }

    @GetMapping("selectWarehouse")
    public String startProducingWHS(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        usersService.loggedUserData(model,session);

        return "wmsOperations/selectWarehouse";
    }

    @PostMapping("selectWarehouse")
    public String startProducingWHSPost(HttpSession session,String chosenWarehouse,@SessionAttribute(required = false) String goingToURL) {
        log.error("goingToURL in selectWarehouse POST: " + goingToURL);
        session.setAttribute("chosenWarehouse", chosenWarehouse);
        return "redirect:" + goingToURL;
    }

    @GetMapping("producingHeader/{id}")
    public String producingHeader(@PathVariable Long id, Model model,@SessionAttribute(required = false) String chosenWarehouse,HttpSession session) {

        String productionAfterCreationValue = extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),"Production_after_creation").getExtremelyValue();
        model.addAttribute("productionAfterCreationValue",productionAfterCreationValue);

        Article article = articleRepository.articleForProduction(id,companyService.getOneCompanyByUsername(SecurityUtils.username()),chosenWarehouse);
        model.addAttribute("article",article);
        List<ArticleRepository.ProductionArticleOnStock> productionArticleOnStocks = articleRepository.articlesNeededForProductionOnStock(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),chosenWarehouse,id);
        model.addAttribute("productionArticleOnStocks",productionArticleOnStocks);
        Production production = new Production();
        model.addAttribute("production",production);

        model.addAttribute("chosenWarehouse",chosenWarehouse);
        usersService.loggedUserData(model,session);
        return "wmsOperations/producingHeader";
    }

    @PostMapping("production/producingHeader")
    public String producingHeaderPost(Production production, String articleId,@SessionAttribute(required = false) String chosenWarehouse,HttpSession session) {

        productionService.createProduction(production,articleId,chosenWarehouse,session);

        return "redirect:/production";
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






}
