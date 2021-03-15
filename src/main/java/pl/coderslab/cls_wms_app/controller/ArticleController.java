package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class ArticleController {

    private final ArticleService articleService;
    private final CompanyService companyService;
    private final UsersService usersService;

    @Autowired
    public ArticleController(ArticleService articleService, CompanyService companyService, UsersService usersService) {
        this.articleService = articleService;
        this.companyService = companyService;
        this.usersService = usersService;
    }

    @GetMapping("article")
    public String list(Model model) {
        List<Article> article = articleService.getArticle(SecurityUtils.username());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("article", article);
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "/storage/article/article";
    }

    @GetMapping("/config/articleDeactivatedList")
    public String articleDeactivatedList(Model model) {
        List<Article> article = articleService.getDeactivatedArticle();
        model.addAttribute("article", article);
        return "/storage/article/articleDeactivatedList";
    }


    @GetMapping("formArticle")
    public String articleForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("article", new Article());
        model.addAttribute("companies", companies);
        usersService.loggedUserData(model);
        return "/storage/article/formArticle";
    }

    @PostMapping("formArticle")
    public String articleAdd(Article article) {
        articleService.add(article);
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
        model.addAttribute(article);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "/storage/article/formEditArticle";
    }

    @PostMapping("formEditArticle")
    public String edit(Article article) {
        articleService.add(article);
        return "redirect:/article";
    }

}
