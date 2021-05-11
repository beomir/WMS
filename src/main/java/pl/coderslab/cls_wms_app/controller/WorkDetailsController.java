package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class WorkDetailsController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final CustomerUserDetailsService customerUserDetailsService;


    @Autowired
    public WorkDetailsController(WorkDetailsService workDetailsService, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, CustomerUserDetailsService customerUserDetailsService) {
        this.workDetailsService = workDetailsService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @GetMapping("workDetails")
    public String list(Model model) {
        List<WorkDetails> workDetails = workDetailsService.getWorkDetailsPerWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("workDetails", workDetails);
        List<Warehouse> warehouses =warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("warehouses", warehouses);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);

        usersService.loggedUserData(model);

        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/workDetails";
        }
    }

    @GetMapping("/deleteWork/{id}")
    public String removeWork(@PathVariable Long id) {
        workDetailsService.delete(id);
        return "redirect:/workDetails";
    }

    @GetMapping("/formEditWork/{id}")
    public String updateUnit(@PathVariable Long id, Model model) {
        WorkDetails workDetails = workDetailsService.findById(id);
        model.addAttribute(workDetails);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        usersService.loggedUserData(model);
        return "wmsOperations/formEditWork";
    }

    @PostMapping("formEditUnit")
    public String edit(WorkDetails workDetails) {
        workDetailsService.add(workDetails);
        return "redirect:/workDetails";
    }

}
