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
import pl.coderslab.cls_wms_app.service.VendorService;

import java.util.List;

@Controller
public class VendorController {


    private final VendorService vendorService;
    private final CompanyService companyService;

    @Autowired
    public VendorController(VendorService vendorService, CompanyService companyService) {
        this.vendorService = vendorService;
        this.companyService = companyService;
    }

    @GetMapping("vendor")
    public String list(Model model) {
        List<Vendor> vendor = vendorService.getVendor(SecurityUtils.username());
        model.addAttribute("vendor", vendor);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "vendor";
    }

    @GetMapping("vendorDeactivatedList")
    public String vendorDeactivatedList(Model model) {
        List<Vendor> vendor = vendorService.getDeactivatedVendor();
        model.addAttribute("vendor", vendor);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "vendorDeactivatedList";
    }


    @GetMapping("formVendor")
    public String vendorForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
//        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("vendor", new Vendor());
        model.addAttribute("companies", companies);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formVendor";
    }

    @PostMapping("formVendor")
    public String vendorAdd(Vendor vendor) {
        vendorService.add(vendor);
        return "redirect:/vendor";
    }

    @GetMapping("/deleteVendor/{id}")
    public String removeVendor(@PathVariable Long id) {
        vendorService.delete(id);
        return "redirect:/vendor";
    }

    @GetMapping("/activateVendor/{id}")
    public String activateVendor(@PathVariable Long id) {
        vendorService.activate(id);
        return "redirect:/vendorDeactivatedList";
    }

    @GetMapping("/formEditVendor/{id}")
    public String updateVendor(@PathVariable Long id, Model model) {
        Vendor vendor = vendorService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute(vendor);
        model.addAttribute("companies", companies);
//        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formEditVendor";
    }

    @PostMapping("formEditVendor")
    public String editVendor(Vendor vendor) {
        vendorService.add(vendor);
//        usersDetailsService.add(usersDetails);
        return "redirect:/vendor";
    }


}
