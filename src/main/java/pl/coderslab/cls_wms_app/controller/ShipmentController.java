package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipMethodService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipMethodService shipMethodService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ShipMethodService shipMethodService, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, CustomerUserDetailsService customerUserDetailsService) {
        this.shipmentService = shipmentService;
        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.customerUserDetailsService = customerUserDetailsService;
    }

    @GetMapping("/shipment")
    public String list(Model model) {
        List<Shipment> shipments = shipmentService.getShipment(customerUserDetailsService.chosenWarehouse ,SecurityUtils.username());
        List<ShipMethod> shipMethod = shipMethodService.getShipMethod();
        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse );
        model.addAttribute("shipments", shipments);
        model.addAttribute("shipMethod", shipMethod);
        model.addAttribute("warehouse", warehouse);

        usersService.loggedUserData(model);

//        Map<String,Integer> surveyMap =  shipmentService.surveyMap(warehouseId,SecurityUtils.username());
//        model.addAttribute("surveyMap",surveyMap);

        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/shipment";
        }

    }

    @GetMapping("/finishedShipment/{id}")
    public String finishShipment(@PathVariable Long id) throws IOException, MessagingException {
        Long getShipmentById = shipmentService.findById(id).getShipmentNumber();
        shipmentService.finishShipment(getShipmentById);
        return "redirect:/shipment/shipment";
    }

//    @GetMapping("/displayBarGraph")
//    public String barGraph(Model model){
//        Map<String,Integer> surveyMap = new HashMap<>();
//        surveyMap.put("Java",40);
//        surveyMap.put("Dev oops",20);
//        surveyMap.put("Python",15);
//        surveyMap.put(".Net",10);
//        model.addAttribute("surveyMap",surveyMap);
//        return "barGraph";
//    }

}
