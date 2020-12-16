package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.service.CompanyService;

import java.time.LocalDateTime;
import java.util.List;


@Controller
@RequestMapping("/config")
public class CompanyController {


    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @GetMapping("company")
    public String companyList(Model model) {
        List<Company> company = companyService.getCompanyWithoutAll();
        model.addAttribute("companies", company);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "company";
    }

    @GetMapping("companyDeactivatedList")
    public String companyDeactivatedList(Model model) {
        List<Company> company = companyService.getDeactivatedCompany();
        model.addAttribute("company", company);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "companyDeactivatedList";
    }


    @GetMapping("formCompany")
    public String companyForm(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("company", new Company());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formCompany";
    }

    @PostMapping("formCompany")
    public String addCompany(Company company) {
        companyService.add(company);
        return "redirect:/config/company";
    }

    @GetMapping("/deleteCompany/{id}")
    public String removeCompany(@PathVariable Long id) {
        companyService.delete(id);
        return "redirect:/config/company";
    }

    @GetMapping("/activateCompany/{id}")
    public String activateCompany(@PathVariable Long id) {
        companyService.activate(id);
        return "redirect:/config/companyDeactivatedList";
    }

    @GetMapping("/formEditCompany/{id}")
    public String updateCompany(@PathVariable Long id, Model model) {
        Company company = companyService.findById(id);
        model.addAttribute(company);
//        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formEditCompany";
    }

    @PostMapping("formEditCompany")
    public String edit(Company company) {
        companyService.add(company);
//        usersDetailsService.add(usersDetails);
        return "redirect:/config/company";
    }


}
