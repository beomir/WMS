package pl.coderslab.cls_wms_app.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipMethodService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipMethodService shipMethodService;
    private final WarehouseService warehouseService;
    private final UsersService usersService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ShipMethodService shipMethodService, WarehouseService warehouseService, UsersService usersService) {
        this.shipmentService = shipmentService;
        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.usersService = usersService;
    }

    @GetMapping("/shipment")
    public String shipmentList(Model model,
                               HttpSession session,
                               @SessionAttribute(required = false) String chosenWarehouse,
                               HttpServletRequest request) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")){
            List<Shipment> shipments = shipmentService.getShipmentsForLoggedUser(chosenWarehouse ,SecurityUtils.username());
            List<ShipMethod> shipMethod = shipMethodService.getShipMethod();
            usersService.loggedUserData(model,session);

            Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("shipments", shipments);
            model.addAttribute("shipMethod", shipMethod);



//        Map<String,Integer> surveyMap =  shipmentService.surveyMap(warehouseId,SecurityUtils.username());
//        model.addAttribute("surveyMap",surveyMap);
            return "wmsOperations/shipment";
        }
        else{
            return "redirect:/selectWarehouse";
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
