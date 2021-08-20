package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipMethodService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentInCreationService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CustomerService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
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

    @Autowired
    public ShipmentInCreationController(ShipmentInCreationService shipmentInCreationService, ShipMethodService shipMethodService, WarehouseService warehouseService, CompanyService companyService, ArticleService articleService, UnitService unitService, CustomerService customerService, ShipmentService shipmentService, UsersService usersService) {

        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.shipmentInCreationService = shipmentInCreationService;
        this.articleService = articleService;
        this.unitService = unitService;
        this.customerService = customerService;
        this.shipmentService = shipmentService;
        this.usersService = usersService;
    }

    @GetMapping("/shipmentInCreation")
    public String list(Model model,
                       HttpSession session,
                       @SessionAttribute(required = false) String chosenWarehouse,
                       HttpServletRequest request) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")) {

            List<ShipmentInCreation> getShipmentInCreation = shipmentInCreationService.getShipmentsListForLoggedUser(chosenWarehouse,SecurityUtils.username());
            List<ShipMethod> shipMethod = shipMethodService.getShipMethod();
            model.addAttribute("shipments", getShipmentInCreation);
            model.addAttribute("shipMethod", shipMethod);

            Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
            model.addAttribute("warehouse", warehouse);
            List<Long> stockDifferences = shipmentInCreationService.stockDifference(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("stockDifferences", stockDifferences);

            List<Long> stockDifferencesQty = shipmentInCreationService.stockDifferenceQty(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("stockDifferencesQty", stockDifferencesQty);
            List<Long> shipmentCreationSummary = shipmentInCreationService.shipmentCreationSummary(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("shipmentCreationSummary", shipmentCreationSummary);

            List<Shipment> shipments = shipmentService.getShipmentsForLoggedUser(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("shipment", shipments);
            String messages = shipmentInCreationService.resultOfShipmentCreationValidation(chosenWarehouse);
            model.addAttribute("messages", messages);

            int checkHowManyNotFinishedShipments = shipmentService.checkHowManyNotFinishedShipments(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("cHMNFS", checkHowManyNotFinishedShipments);
            usersService.loggedUserData(model, session);

            return "wmsOperations/shipmentInCreation";
        }
        else{
            return "redirect:/selectWarehouse";
        }

    }

    @GetMapping("/formShipment")
    public String shipmentForm(Model model,
                               HttpSession session,
                               @SessionAttribute(required = false) String chosenWarehouse,
                               HttpServletRequest request){
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")) {

            model.addAttribute("shipment", new ShipmentInCreation());
            List<Customer> customers = customerService.getCustomer(SecurityUtils.username());
            model.addAttribute("customers", customers);
            model.addAttribute("lastShipmentNumber", shipmentInCreationService.lastShipment());

            List<Unit> units = unitService.getUnit();
            model.addAttribute("units", units);
            List<ShipMethod> shipMethods = shipMethodService.getShipMethod();
            model.addAttribute("shipMethods", shipMethods);

            List<Article> articles = articleService.getArticle(SecurityUtils.username());
            model.addAttribute("articles", articles);
            Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
            model.addAttribute("warehouse", warehouse);

            int qtyOfOpenedShipmentsInCreation = shipmentInCreationService.qtyOfOpenedShipmentsInCreation(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("qtyOfOpenedShipmentsInCreation", qtyOfOpenedShipmentsInCreation);
            List<ShipmentInCreation> openedShipments = shipmentInCreationService.openedShipments(chosenWarehouse, SecurityUtils.username());
            model.addAttribute("openedShipments", openedShipments);

            List<Company> activeCompany = companyService.getCompany();
            model.addAttribute("activeCompany", activeCompany);
            usersService.loggedUserData(model, session);

            return "wmsOperations/formShipment";
        }
        else{
            return "redirect:/selectWarehouse";
        }

    }

    @PostMapping("formShipment")
    public String add(ShipmentInCreation shipmentInCreation) {
        shipmentInCreationService.addShipmentInCreation(shipmentInCreation);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/editShipment/{id}")
    public String updateShipment(@PathVariable Long id,
                                 Model model,
                                 @SessionAttribute(required = false) String chosenWarehouse,
                                 HttpSession session) {
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
        Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
        model.addAttribute("warehouse", warehouse);

        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        usersService.loggedUserData(model,session);
        return "wmsOperations/editShipment";
    }

    @PostMapping("editShipment")
    public String updateShipmentPost(ShipmentInCreation shipmentInCreation) {
        shipmentInCreationService.addShipmentInCreation(shipmentInCreation);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/closeCreationShipment/{id}")
    public String closeCreationShipment(@PathVariable Long id, @SessionAttribute(required = false) String chosenWarehouse) {
        shipmentInCreationService.closeCreationShipment(id,chosenWarehouse);
        return "redirect:/shipment/shipmentInCreation";
    }

    @GetMapping("/deleteShipmentLine/{id}")
    public String deleteShipmentLine(@PathVariable Long id) {
        shipmentInCreationService.remove(id);
        return "redirect:/shipment/shipmentInCreation";
    }

}
