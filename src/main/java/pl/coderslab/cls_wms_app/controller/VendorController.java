package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Vendor;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.UsersService;
import pl.coderslab.cls_wms_app.service.VendorService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class VendorController {


    private final VendorService vendorService;
    private final CompanyService companyService;
    private final UsersService usersService;

    @Autowired
    public VendorController(VendorService vendorService, CompanyService companyService, UsersService usersService) {
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.usersService = usersService;
    }

    @GetMapping("/reception/vendor")
    public String list(Model model) {
        List<Vendor> vendor = vendorService.getVendor(SecurityUtils.username());
        model.addAttribute("vendor", vendor);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "vendor";
    }

    @GetMapping("/config/vendorDeactivatedList")
    public String vendorDeactivatedList(Model model) {
        List<Vendor> vendor = vendorService.getDeactivatedVendor();
        model.addAttribute("vendor", vendor);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "vendorDeactivatedList";
    }


    @GetMapping("/reception/formVendor")
    public String vendorForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("vendor", new Vendor());
        model.addAttribute("companies", companies);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "formVendor";
    }

    @PostMapping("/reception/formVendor")
    public String vendorAdd(Vendor vendor) {
        vendorService.add(vendor);
        return "redirect:/reception/vendor";
    }

    @GetMapping("/reception/deleteVendor/{id}")
    public String removeVendor(@PathVariable Long id) {
        vendorService.delete(id);
        return "redirect:/reception/vendor";
    }

    @GetMapping("/config/activateVendor/{id}")
    public String activateVendor(@PathVariable Long id) {
        vendorService.activate(id);
        return "redirect:/config/vendorDeactivatedList";
    }

    @GetMapping("/reception/formEditVendor/{id}")
    public String updateVendor(@PathVariable Long id, Model model) {
        Vendor vendor = vendorService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute(vendor);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "formEditVendor";
    }

    @PostMapping("/reception/formEditVendor")
    public String editVendor(Vendor vendor) {
        vendorService.add(vendor);
        return "redirect:/reception/vendor";
    }


}
