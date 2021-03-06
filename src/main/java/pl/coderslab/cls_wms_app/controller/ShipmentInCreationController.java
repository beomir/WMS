package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.*;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/shipment")
public class ShipmentInCreationController {


    private final ShipmentInCreationService shipmentInCreationService;
    private final ShipMethodService shipMethodService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final ArticleService articleService;
    private final UnitService unitService;
    private final CustomerService customerService;
    private final ShipmentService shipmentService;
    private final UsersService usersService;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public ShipmentInCreationController(ShipmentInCreationService shipmentInCreationService, ShipMethodService shipMethodService, WarehouseService warehouseService, CompanyService companyService, ArticleService articleService, UnitService unitService, CustomerService customerService, ShipmentService shipmentService, UsersService usersService, CustomerUserDetailsService customerUserDetailsService) {

        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.shipmentInCreationService = shipmentInCreationService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.customerService = customerService;
        this.shipmentService = shipmentService;
        this.usersService = usersService;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @GetMapping("/shipmentInCreation")
    public String list(Model model) {
        List<ShipmentInCreation> getShipmentInCreation = shipmentInCreationService.getShipmentInCreationById(customerUserDetailsService.chosenWarehouse );
        List<ShipMethod> shipMethod = shipMethodService.getShipMethod();
        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse );
        model.addAttribute("shipments", getShipmentInCreation);
        model.addAttribute("shipMethod", shipMethod);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        List<Long> stockDifferences = shipmentInCreationService.stockDifference(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("stockDifferences", stockDifferences);
        List<Long> stockDifferencesQty = shipmentInCreationService.stockDifferenceQty(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("stockDifferencesQty", stockDifferencesQty);
        List<Long> shipmentCreationSummary = shipmentInCreationService.shipmentCreationSummary(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("shipmentCreationSummary", shipmentCreationSummary);

        List<Shipment> shipments = shipmentService.getShipment(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("shipment", shipments);
        String messages = shipmentInCreationService.resultOfShipmentCreationValidation(customerUserDetailsService.chosenWarehouse );
        model.addAttribute("messages", messages);

        int checkHowManyNotFinishedShipments = shipmentService.checkHowManyNotfinishedShipments(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("cHMNFS", checkHowManyNotFinishedShipments);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "shipmentInCreation";
        }
    }

    @GetMapping("/formShipment")
    public String shipmentForm(Model model){
        model.addAttribute("shipment", new ShipmentInCreation());
        List<Customer> customers = customerService.getCustomer(SecurityUtils.username());
        model.addAttribute("customers", customers);

        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);

        List<ShipMethod> shipMethods = shipMethodService.getShipMethod();
        model.addAttribute("shipMethods", shipMethods);

        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);

        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse );
        model.addAttribute("warehouses", warehouses);

        int qtyOfOpenedShipmentsInCreation = shipmentInCreationService.qtyOfOpenedShipmentsInCreation(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("qtyOfOpenedShipmentsInCreation", qtyOfOpenedShipmentsInCreation);

        List<ShipmentInCreation> openedShipments = shipmentInCreationService.openedShipments(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        model.addAttribute("openedShipments", openedShipments);

        model.addAttribute("lastShipmentNumber", shipmentInCreationService.lastShipment());

        List<Company> companies = companyService.getCompany();
        model.addAttribute("companies", companies);

        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "formShipment";
        }
    }

    @PostMapping("formShipment")
    public String add(ShipmentInCreation shipmentInCreation) {
        shipmentInCreationService.addShipmentInCreation(shipmentInCreation);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/editShipment/{id}")
    public String updateShipment(@PathVariable Long id, Model model, @SessionAttribute Long warehouseId) {
        ShipmentInCreation shipmentInCreation = shipmentInCreationService.findById(id);
        model.addAttribute(shipmentInCreation);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Customer> customers = customerService.getCustomer(SecurityUtils.username());
        model.addAttribute("customers", customers);

        List<Unit> units = unitService.getUnit();
        model.addAttribute("units", units);

        List<ShipMethod> shipMethods = shipMethodService.getShipMethod();
        model.addAttribute("shipMethods", shipMethods);

        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        model.addAttribute("articles", articles);

        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        model.addAttribute("warehouses", warehouses);

        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        usersService.loggedUserData(model);
        return "editShipment";
    }

    @PostMapping("editShipment")
    public String updateShipmentPost(ShipmentInCreation shipmentInCreation) {
        shipmentInCreationService.addShipmentInCreation(shipmentInCreation);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/closeCreationShipment/{id}")
    public String closeCreationShipment(@PathVariable Long id, @SessionAttribute Long warehouseId) {
        shipmentInCreationService.closeCreationShipment(id,warehouseId);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/deleteShipmentLine/{id}")
    public String deleteShipmentLine(@PathVariable Long id) {
        shipmentInCreationService.remove(id);
        return "redirect:/shipment/shipmentInCreation";
    }

}
