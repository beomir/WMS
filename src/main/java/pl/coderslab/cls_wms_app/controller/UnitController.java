package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.service.UnitService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class UnitController {

    private UnitService unitService;


    @Autowired
    public UnitController(UnitService unitService) {
        this.unitService = unitService;
    }

    @GetMapping("unit")
    public String list(Model model) {
        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);
        return "unit";
    }

    @GetMapping("unitDeactivatedList")
    public String unitDeactivatedList(Model model) {
        List<Unit> unitList = unitService.getDeactivatedUnit();
        model.addAttribute("units", unitList);
        return "unitDeactivatedList";
    }


    @GetMapping("formUnitCreation")
    public String unitForm(Model model){
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("unit", new Unit());
        return "formUnitCreation";
    }

    @PostMapping("formUnitCreation")
    public String unitAdd(Unit unit) {
        unitService.add(unit);
        return "redirect:/unit";
    }

    @GetMapping("/deleteUnit/{id}")
    public String removeUnit(@PathVariable Long id) {
        unitService.delete(id);
        return "redirect:/unit";
    }

    @GetMapping("/activateUnit/{id}")
    public String activateUnit(@PathVariable Long id) {
        unitService.activate(id);
        return "redirect:/unitDeactivatedList";
    }


    @GetMapping("/formEditUnit/{id}")
    public String updateCustomer(@PathVariable Long id, Model model) {
        Unit unit = unitService.findById(id);
        model.addAttribute(unit);
        model.addAttribute("localDateTime", LocalDateTime.now());
        return "formEditUnit";
    }

    @PostMapping("formEditUnit")
    public String edit(Unit unit) {
        unitService.add(unit);
        return "redirect:/unit";
    }
}
