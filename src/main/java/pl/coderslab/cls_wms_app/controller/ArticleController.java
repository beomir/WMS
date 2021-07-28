package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.ArticleServiceImpl;
import pl.coderslab.cls_wms_app.service.storage.ArticleTypesService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsSettings.IntermediateArticleService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ProductionArticleService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.temporaryObjects.AddLocationToStorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.ArticleSearch;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final ReceptionServiceImpl receptionServiceImpl;
    private final ArticleServiceImpl articleServiceImpl;
    private final ArticleTypesService articleTypesService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final UsersServiceImpl usersServiceImpl;
    private final LocationNameConstruction locationNameConstruction;
    private final AddLocationToStorageZone addLocationToStorageZone;
    public ArticleSearch articleSearch;
    private final ArticleTypesRepository articleTypesRepository;
    private final ExtremelyRepository extremelyRepository;
    private final StorageZoneRepository storageZoneRepository;
    private final ProductionArticleService productionArticleService;
    private final LocationRepository locationRepository;
    private final WarehouseRepository warehouseRepository;
    private final IntermediateArticleService intermediateArticleService;
    private final ArticleRepository articleRepository;
    private final ProductionArticleRepository productionArticleRepository;

    @Autowired
    public ArticleController(ArticleService articleService, ReceptionServiceImpl receptionServiceImpl, ArticleServiceImpl articleServiceImpl, ArticleTypesService articleTypesService, CompanyService companyService, UsersService usersService, UsersServiceImpl usersServiceImpl, LocationNameConstruction locationNameConstruction, AddLocationToStorageZone addLocationToStorageZone, ArticleSearch articleSearch, ArticleTypesRepository articleTypesRepository, ExtremelyRepository extremelyRepository, StorageZoneRepository storageZoneRepository, ProductionArticleService productionArticleService, LocationRepository locationRepository, WarehouseRepository warehouseRepository, IntermediateArticleService intermediateArticleService, ArticleRepository articleRepository, ProductionArticleRepository productionArticleRepository) {
        this.articleService = articleService;
        this.receptionServiceImpl = receptionServiceImpl;
        this.articleServiceImpl = articleServiceImpl;
        this.articleTypesService = articleTypesService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.usersServiceImpl = usersServiceImpl;
        this.locationNameConstruction = locationNameConstruction;
        this.addLocationToStorageZone = addLocationToStorageZone;
        this.articleSearch = articleSearch;
        this.articleTypesRepository = articleTypesRepository;
        this.extremelyRepository = extremelyRepository;
        this.storageZoneRepository = storageZoneRepository;
        this.productionArticleService = productionArticleService;
        this.locationRepository = locationRepository;
        this.warehouseRepository = warehouseRepository;
        this.intermediateArticleService = intermediateArticleService;
        this.articleRepository = articleRepository;
        this.productionArticleRepository = productionArticleRepository;
    }

    @GetMapping("article")
    public String list(Model model,HttpSession session) {
        List<Article> article = articleService.getArticleByAllCriteria(articleSearch.getArticle_number(),articleSearch.getVolumeBiggerThan(),articleSearch.getVolumeLowerThan(),articleSearch.getWidthBiggerThan(),articleSearch.getWidthLowerThan(),articleSearch.getDepthBiggerThan(),articleSearch.getDepthLowerThan(),articleSearch.getHeightBiggerThan(),articleSearch.getHeightLowerThan(),articleSearch.getWeightBiggerThan(),articleSearch.getWeightLowerThan(),articleSearch.getCreatedBy(),articleSearch.getCreationDateFrom(),articleSearch.getCreationDateTo(),articleSearch.getLastUpdateDateFrom(),articleSearch.getLastUpdateDateTo(),articleSearch.getCompany(),articleSearch.getArticleDescription(),articleSearch.getArticleTypes());
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());

        try{
            Extremely extremely = extremelyRepository.findExtremelyByCompanyNameAndExtremelyName(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),"Production_module");
            model.addAttribute("productionModule", extremely.getExtremelyValue());
            log.error("extremely value: " + extremely.getExtremelyValue());
        }
        catch (NullPointerException e){
            String productionModule = "off";
            model.addAttribute("productionModule", productionModule);
            log.error("extremely value is null");
        }
        model.addAttribute("article", article);
        model.addAttribute("articleMessage", session.getAttribute("articleMessage"));
        model.addAttribute("productionArticleMessage", session.getAttribute("productionArticleMessage"));
        model.addAttribute("companies", companies);
        model.addAttribute("articleSearch",articleSearch);
        receptionServiceImpl.insertReceptionFileResult = "";
        locationNameConstruction.message = "";
        addLocationToStorageZone.message = "";
        usersServiceImpl.alertMessage = "";
        String token = usersServiceImpl.FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "storage/article/article";
    }

    @GetMapping("/config/articleDeactivatedList")
    public String articleDeactivatedList(Model model) {
        List<Article> article = articleService.getDeactivatedArticle();
        model.addAttribute("article", article);
        return "storage/article/articleDeactivatedList";
    }


    @GetMapping("formArticle")
    public String articleForm(Model model, HttpSession session){

        List<ArticleTypes> articleTypesList = articleTypesService.getArticleTypes();
        List<StorageZone> storageZoneList = storageZoneRepository.getStorageZones();
        List<LocationRepository.ProductionLocations> productionLocations = locationRepository.getProductionLocations();
        List<Warehouse> warehouseList = warehouseRepository.getWarehouse();

        try{
            Extremely extremely = extremelyRepository.findExtremelyByCompanyNameAndExtremelyName(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),"Production_module");
            model.addAttribute("productionModule", extremely.getExtremelyValue());
            log.error("extremely value: " + extremely.getExtremelyValue());
        }
        catch (NullPointerException e){
            String productionModule = "off";
            model.addAttribute("productionModule", productionModule);
            log.error("extremely value is null");
        }
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("article", new Article());
        model.addAttribute("productionArticle", new ProductionArticle());


        model.addAttribute("articleTypesList", articleTypesList);
        model.addAttribute("storageZones",storageZoneList);
        model.addAttribute("productionLocations",productionLocations);
        model.addAttribute("warehouses",warehouseList);


        usersService.loggedUserData(model,session);

        return "storage/article/formArticle";
    }

    @PostMapping("formArticle")
    public String articleAdd(Article article, ProductionArticle productionArticle, HttpServletRequest request,boolean productionArticleCheckbox,HttpSession session) {
        articleService.addNew(article,productionArticle,request,session,productionArticleCheckbox);
        log.error("productionArticle value New Article Post: " + productionArticle);
        log.error("productionArticleCheckbox: " + productionArticleCheckbox);
        log.error("productionArticle.getProductionArticleType(): " + productionArticle.getProductionArticleType());
        Article checkIfArticleSavedInDB = articleRepository.findArticleByArticle_number(article.getArticle_number());
        if(productionArticle.getProductionArticleType().equals("finish product") && checkIfArticleSavedInDB != null && productionArticleCheckbox){
            productionArticleService.addNew(productionArticle,article);
            log.error("finish product way");
        }
        else if(productionArticle.getProductionArticleType().equals("intermediate") && checkIfArticleSavedInDB != null && productionArticleCheckbox){
            log.error("intermediate way 1/2");
            intermediateArticleService.addNew(productionArticle,article);
        }

        return "redirect:/article";
    }

    @GetMapping("/deleteArticle/{id}")
    public String removeArticle(@PathVariable Long id,HttpSession session) {
        articleService.delete(id,session);
        return "redirect:/article";
    }

    @PostMapping("/changeStatusOfProductionArticle")
    public String changeStatusOfProductionArticle(String multiChange,Long id,String status,HttpSession session) {
        log.error("multiChange: " + multiChange);
        log.error("status: " + status);
        if(multiChange.equals("2")){
            if(status.equals("deactivated")){
                articleService.activate(id,session);
            }
            else{
                articleService.delete(id,session);
            }

            log.error("statusValue selected No");
        }
        else if(multiChange.equals("1")){
            if(status.equals("deactivated")){
                articleService.activateFinishProductWithIntermediates(id,session);
            }
            else{
                articleService.deactivateFinishProductWithIntermediates(id,session);
            }
            log.error("multiChange selected Yes");
        }

        return "redirect:/article";
    }

    @GetMapping("/config/activateArticle/{id}")
    public String activateArticle(@PathVariable Long id,HttpSession session) {
        articleService.activate(id,session);
        return "redirect:/article";
    }

    @GetMapping("/formEditArticle/{id}")
    public String updateArticle(@PathVariable Long id, Model model,HttpSession session) {
        Article article = articleService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<ArticleTypes> articleTypesList = articleTypesService.getArticleTypes();
        List<StorageZone> storageZoneList = storageZoneRepository.getStorageZones();
        List<LocationRepository.ProductionLocations> productionLocations = locationRepository.getProductionLocations();
        List<Warehouse> warehouseList = warehouseRepository.getWarehouse();

        List<ProductionArticle> productionArticlesList = productionArticleRepository.getProductionArticles();
        model.addAttribute("productionArticlesList",productionArticlesList);

        if(productionArticleService.getProductionArticleByArticleId(id) != null){
            ProductionArticle productionArticle = productionArticleService.getProductionArticleByArticleId(id);
            model.addAttribute("productionArticle", productionArticle);
            int qtyOfAssignedIntermediateToFinishProduct = articleRepository.intermediateProductNumberByFinishProductNumberAndCompanyName(article.getArticle_number(),companyService.getOneCompanyByUsername(SecurityUtils.username()).getName()).size();
            model.addAttribute("qtyOfAssignedIntermediateToFinishProduct",qtyOfAssignedIntermediateToFinishProduct);

            List<Long> intermediateProductNumberByFinishProductNumberAndCompanyName = articleRepository.intermediateProductNumberByFinishProductNumberAndCompanyName(article.getArticle_number(),companyService.getOneCompanyByUsername(SecurityUtils.username()).getName());
            List<Article> intermediateArticleList = new ArrayList<>();
            for(Long intermediateArticleNumber : intermediateProductNumberByFinishProductNumberAndCompanyName){
                Article intermediateArticle = articleRepository.findArticleByArticle_number(intermediateArticleNumber);
                intermediateArticleList.add(intermediateArticle);
            }

            model.addAttribute("intermediatesList",intermediateArticleList);

            log.error("productionArticle value: " + productionArticleService.getProductionArticleByArticleId(id));
        }
        if(productionArticleService.getProductionArticleByArticleId(id) == null){
            model.addAttribute("productionArticle", new ProductionArticle());
            log.error("productionArticle value is null");
        }
        if(intermediateArticleService.getIntermediateArticleByArticleId(id) != null){
            IntermediateArticle intermediateArticle = intermediateArticleService.getIntermediateArticleByArticleId(id);
            model.addAttribute("intermediateArticle",intermediateArticle);

            List<Long> intermediateArticleNumbersByIntermediateArticle = articleRepository.intermediateArticleNumbersByIntermediateArticleAndCompanyName(article.getArticle_number(),companyService.getOneCompanyByUsername(SecurityUtils.username()).getName());
            List<Article> intermediateArticleListFromIntermediate  = new ArrayList<>();
            for(Long intermediateArticleNumber : intermediateArticleNumbersByIntermediateArticle){
                Article singularIntermediateArticle = articleRepository.findArticleByArticle_number(intermediateArticleNumber);
                intermediateArticleListFromIntermediate.add(singularIntermediateArticle);
            }
            model.addAttribute("intermediateArticleListFromIntermediate",intermediateArticleListFromIntermediate);
        }


        try{
            Extremely extremely = extremelyRepository.findExtremelyByCompanyNameAndExtremelyName(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),"Production_module");
            model.addAttribute("productionModule", extremely.getExtremelyValue());
            log.debug("extremely value: " + extremely.getExtremelyValue());
        }
        catch (NullPointerException e){
            String productionModule = "off";
            model.addAttribute("productionModule", productionModule);
            log.error("extremely value is null");
        }


        model.addAttribute(article);
        model.addAttribute("companies", companies);
        model.addAttribute("articleTypesList", articleTypesList);
        model.addAttribute("localDateTime", LocalDateTime.now());

        model.addAttribute("storageZones",storageZoneList);
        model.addAttribute("productionLocations",productionLocations);
        model.addAttribute("warehouses",warehouseList);

        usersService.loggedUserData(model,session);

        return "storage/article/formEditArticle";
    }

    @PostMapping("formEditArticle")
    public String edit(Article article,ProductionArticle productionArticle,String warehouseName,String productionArticleId,HttpServletRequest request,boolean productionArticleCheckbox,HttpSession session) {
        boolean productionStatusOfArticle = article.isProduction();
        log.info("productionArticle value edit Article Post: " + productionArticle);
        log.info("chosen warehouse: " +  warehouseName);
        log.error("productionArticleId: " + productionArticleId);
        log.error("productionArticleCheckbox: " + productionArticleCheckbox);
        articleService.edit(article,productionArticle,request,session,productionArticleCheckbox);
        boolean intermediateQtyForFinishProductStatus = (boolean) session.getAttribute("intermediateQtyForFinishProductStatus");
        log.error("intermediateQtyForFinishProduct: " + intermediateQtyForFinishProductStatus);
        log.error("productionStatusOfArticle: " + productionStatusOfArticle);
        if(productionArticle.getProductionArticleType().equals("finish product") && intermediateQtyForFinishProductStatus) {
            log.error("production way edit ");
            if((productionArticleCheckbox && !productionStatusOfArticle) || (!productionArticleCheckbox && productionStatusOfArticle) || (productionArticleCheckbox && productionStatusOfArticle)){
                log.error("production way edit ");
                productionArticleService.edit(productionArticle, article, warehouseName, productionArticleId);
            }
        }
        else if(productionArticle.getProductionArticleType().equals("intermediate") && intermediateQtyForFinishProductStatus){
            log.error("Inter productionArticleCheckbox: " + productionArticleCheckbox);
            log.error("Inter productionStatusOfArticle: " + productionStatusOfArticle);

            if((productionArticleCheckbox && !productionStatusOfArticle) || (!productionArticleCheckbox && productionStatusOfArticle) || (productionArticleCheckbox && productionStatusOfArticle)){
                log.error("intermediate way edit 1/2");
                intermediateArticleService.edit(productionArticle, article, warehouseName,productionStatusOfArticle,session);
            }

        }
        return "redirect:/article";
    }

    @GetMapping("articleTypes")
    public String listOfArticleTypes(Model model,HttpSession session) {
        List<ArticleTypes> articleTypes = articleTypesService.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        usersService.loggedUserData(model,session);
        return "storage/article/articleTypes";
    }

    @GetMapping("/article-browser")
    public String browser(Model model,HttpSession session) {
        model.addAttribute("articleSearching", new ArticleSearch());
        Company userCompany = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("userCompany", userCompany);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        usersService.loggedUserData(model,session);
        return "storage/article/article-browser";
    }

    @PostMapping("/article-browser")
    public String findArticle(ArticleSearch articleSearching) {
        articleService.save(articleSearching);
        return "redirect:/article";
    }

}
