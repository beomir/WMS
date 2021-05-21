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
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.storage.ArticleServiceImpl;
import pl.coderslab.cls_wms_app.service.storage.ArticleTypesService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.temporaryObjects.AddLocationToStorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.ArticleSearch;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;


import java.time.LocalDateTime;
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

    @Autowired
    public ArticleController(ArticleService articleService, ReceptionServiceImpl receptionServiceImpl, ArticleServiceImpl articleServiceImpl, ArticleTypesService articleTypesService, CompanyService companyService, UsersService usersService, UsersServiceImpl usersServiceImpl, LocationNameConstruction locationNameConstruction, AddLocationToStorageZone addLocationToStorageZone, ArticleSearch articleSearch, ArticleTypesRepository articleTypesRepository, ExtremelyRepository extremelyRepository) {
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
    }

    @GetMapping("article")
    public String list(Model model) {
        List<Article> article = articleService.getArticleByAllCriteria(articleSearch.getArticle_number(),articleSearch.getVolumeBiggerThan(),articleSearch.getVolumeLowerThan(),articleSearch.getWidthBiggerThan(),articleSearch.getWidthLowerThan(),articleSearch.getDepthBiggerThan(),articleSearch.getDepthLowerThan(),articleSearch.getHeightBiggerThan(),articleSearch.getHeightLowerThan(),articleSearch.getWeightBiggerThan(),articleSearch.getWeightLowerThan(),articleSearch.getCreatedBy(),articleSearch.getCreationDateFrom(),articleSearch.getCreationDateTo(),articleSearch.getLastUpdateDateFrom(),articleSearch.getLastUpdateDateTo(),articleSearch.getCompany(),articleSearch.getArticleDescription(),articleSearch.getArticleTypes());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("article", article);
        model.addAttribute("articleMessage", articleServiceImpl.articleMessage);
        model.addAttribute("companys", companys);
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
    public String articleForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<ArticleTypes> articleTypesList = articleTypesService.getArticleTypes();
        try{
            Extremely extremely = extremelyRepository.checkProductionModuleStatus(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),"Production_module");
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
        model.addAttribute("companies", companies);
        model.addAttribute("articleTypesList", articleTypesList);
        usersService.loggedUserData(model);

        return "storage/article/formArticle";
    }

    @PostMapping("formArticle")
    public String articleAdd(Article article) {
        articleService.addNew(article);
        return "redirect:/article";
    }

    @GetMapping("/deleteArticle/{id}")
    public String removeArticle(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/article";
    }

    @GetMapping("/config/activateArticle/{id}")
    public String activateArticle(@PathVariable Long id) {
        articleService.activate(id);
        return "redirect:/config/articleDeactivatedList";
    }

    @GetMapping("/formEditArticle/{id}")
    public String updateArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<ArticleTypes> articleTypesList = articleTypesService.getArticleTypes();
        model.addAttribute(article);
        model.addAttribute("companies", companies);
        model.addAttribute("articleTypesList", articleTypesList);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "storage/article/formEditArticle";
    }

    @PostMapping("formEditArticle")
    public String edit(Article article) {
        articleService.edit(article);
        return "redirect:/article";
    }

    @GetMapping("articleTypes")
    public String listOfArticleTypes(Model model) {
        List<ArticleTypes> articleTypes = articleTypesService.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        usersService.loggedUserData(model);
        return "storage/article/articleTypes";
    }

    @GetMapping("/article-browser")
    public String browser(Model model) {
        model.addAttribute("articleSearching", new ArticleSearch());
        Company companys = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        List<Company> company = companyService.getCompany();
        model.addAttribute("company", company);
        List<ArticleTypes> articleTypes = articleTypesRepository.getArticleTypes();
        model.addAttribute("articleTypes", articleTypes);
        usersService.loggedUserData(model);
        return "storage/article/article-browser";
    }

    @PostMapping("/article-browser")
    public String findArticle(ArticleSearch articleSearching) {
        articleService.save(articleSearching);
        return "redirect:/article";
    }

}
