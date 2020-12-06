package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.ArticleService;
import pl.coderslab.cls_wms_app.service.CompanyService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/")
public class ArticleController {

    private ArticleService articleService;
    private CompanyService companyService;

    @Autowired
    public ArticleController(ArticleService articleService, CompanyService companyService) {
        this.articleService = articleService;
        this.companyService = companyService;
    }

    @GetMapping("article")
    public String list(Model model) {
        List<Article> article = articleService.getArticle();
        model.addAttribute("article", article);
        return "article";
    }

    @GetMapping("articleDeactivatedList")
    public String articleDeactivatedList(Model model) {
        List<Article> article = articleService.getDeactivatedArticle();
        model.addAttribute("article", article);
        return "articleDeactivatedList";
    }


    @GetMapping("formArticle")
    public String articleForm(Model model){
        List<Company> companies = companyService.getCompany();
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("article", new Article());
        model.addAttribute("companies", companies);
        return "formArticle";
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

    @GetMapping("/activateArticle/{id}")
    public String activateArticle(@PathVariable Long id) {
        articleService.activate(id);
        return "redirect:/articleDeactivatedList";
    }

    @GetMapping("/formEditArticle/{id}")
    public String updateArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        List<Company> companies = companyService.getCompany();
        model.addAttribute(article);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formEditArticle";
    }

    @PostMapping("formEditArticle")
    public String edit(Article article) {
        articleService.add(article);
        return "redirect:/article";
    }

}
