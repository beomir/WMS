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
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.UnitService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/config")
public class UnitController {

    private final UnitService unitService;
    private final CompanyService companyService;


    @Autowired
    public UnitController(UnitService unitService, CompanyService companyService) {
        this.unitService = unitService;
        this.companyService = companyService;
    }

    @GetMapping("unit")
    public String list(Model model) {
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "unit";
    }

    @GetMapping("unitDeactivatedList")
    public String unitDeactivatedList(Model model) {
        List<Unit> unitList = unitService.getDeactivatedUnit();
        model.addAttribute("units", unitList);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "unitDeactivatedList";
    }


    @GetMapping("formUnitCreation")
    public String unitForm(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("unit", new Unit());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formUnitCreation";
    }

    @PostMapping("formUnitCreation")
    public String unitAdd(Unit unit) {
        unitService.add(unit);
        return "redirect:/config/unit";
    }

    @GetMapping("/deleteUnit/{id}")
    public String removeUnit(@PathVariable Long id) {
        unitService.delete(id);
        return "redirect:/config/unit";
    }

    @GetMapping("/activateUnit/{id}")
    public String activateUnit(@PathVariable Long id) {
        unitService.activate(id);
        return "redirect:/config/unitDeactivatedList";
    }


    @GetMapping("/formEditUnit/{id}")
    public String updateCustomer(@PathVariable Long id, Model model) {
        Unit unit = unitService.findById(id);
        model.addAttribute(unit);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "formEditUnit";
    }

    @PostMapping("formEditUnit")
    public String edit(Unit unit) {
        unitService.add(unit);
        return "redirect:/config/unit";
    }
}
