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
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import java.util.List;

@Controller
@RequestMapping("/config")
public class ExtremelyController {

    private final ExtremelyService extremelyService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;


    @Autowired
    public ExtremelyController(ExtremelyService extremelyService, WarehouseService warehouseService, CompanyService companyService, UsersService usersService) {
        this.extremelyService = extremelyService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
    }

    @GetMapping("extremely")
    public String list(Model model) {
        List<Extremely> extremely = extremelyService.getExtremely();
        model.addAttribute("extremely", extremely);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);


        usersService.loggedUserData(model);
        return "wmsSettings/extremely/extremely";
    }


    @GetMapping("formExtremelyCreation")
    public String extremelyForm(Model model){
        model.addAttribute("extremelys", new Extremely());

        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);

        usersService.loggedUserData(model);
        return "wmsSettings/extremely/formExtremelyCreation";
    }



    @PostMapping("formExtremelyCreation")
    public String extremelyAdd(Extremely extremely) {
        extremelyService.add(extremely);
        return "redirect:/config/extremely";
    }

    @GetMapping("/deleteExtremely/{id}")
    public String removeExtremely(@PathVariable Long id) {
        extremelyService.delete(id);
        return "redirect:/config/extremely";
    }


    @GetMapping("/formEditExtremely/{id}")
    public String updateExtremely(@PathVariable Long id, Model model) {
        model.addAttribute("extremelys", extremelyService.findById(id));

        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);

        usersService.loggedUserData(model);
        return "wmsSettings/extremely/formEditExtremely";
    }

    @PostMapping("formEditExtremely")
    public String edit(Extremely extremely) {
        extremelyService.edit(extremely);
        return "redirect:/config/extremely";
    }
}
