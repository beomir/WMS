package pl.coderslab.cls_wms_app.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.ShipmentRepository;
import pl.coderslab.cls_wms_app.repository.StatusRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipMethodService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.wmsValues.CustomerService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Slf4j
@Controller
@RequestMapping("/shipment")
public class ShipmentController {

    private final ShipmentService shipmentService;
    private final ShipMethodService shipMethodService;
    private final WarehouseService warehouseService;
    private final UsersService usersService;
    private final StatusRepository statusRepository;
    private final CustomerService customerService;

    @Autowired
    public ShipmentController(ShipmentService shipmentService, ShipMethodService shipMethodService, WarehouseService warehouseService, UsersService usersService, StatusRepository statusRepository, CustomerService customerService) {
        this.shipmentService = shipmentService;
        this.shipMethodService = shipMethodService;
        this.warehouseService = warehouseService;
        this.usersService = usersService;
        this.statusRepository = statusRepository;
        this.customerService = customerService;
    }

    @GetMapping("/shipment")
    public String shipmentList(Model model,
                               HttpSession session,
                               @SessionAttribute(required = false) String chosenWarehouse,
                               HttpServletRequest request,
                               @SessionAttribute(required = false) String shipmentCreatedBy,
                               @SessionAttribute(required = false) String shipmentWarehouse,
                               @SessionAttribute(required = false) String shipmentCompany,
                               @SessionAttribute(required = false) String shipmentCustomer,
                               @SessionAttribute(required = false) String shipmentShipmentNumber,
                               @SessionAttribute(required = false) String shipmentHdNumber,
                               @SessionAttribute(required = false) String shipmentStatus,
                               @SessionAttribute(required = false) String shipmentLocation,
                               @SessionAttribute(required = false) String shipmentCreatedFrom,
                               @SessionAttribute(required = false) String shipmentCreatedTo,
                               @SessionAttribute(required = false) String shipmentMessage) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected") || shipmentWarehouse != null){
            List<ShipmentRepository.ShipmentViewObject> shipments = shipmentService.shipmentSummary(shipmentCompany,shipmentWarehouse,shipmentCustomer,shipmentStatus,shipmentLocation,shipmentShipmentNumber,shipmentHdNumber,shipmentCreatedFrom,shipmentCreatedTo,shipmentCreatedBy);
            usersService.loggedUserData(model,session);
            model.addAttribute("shipmentMessage", shipmentMessage);
            String warehouseName = "";
            if(shipmentWarehouse == null || shipmentWarehouse == ""){
                warehouseName = chosenWarehouse;
            }else{
                warehouseName = shipmentWarehouse;
            }
            Warehouse warehouse = warehouseService.getWarehouseByName(warehouseName);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("shipments", shipments);

            model.addAttribute("shipmentCreatedBy",shipmentCreatedBy);
            model.addAttribute("shipmentCompany",shipmentCompany);
            model.addAttribute("shipmentCustomer",shipmentCustomer);
            model.addAttribute("shipmentShipmentNumber",shipmentShipmentNumber);
            model.addAttribute("shipmentHdNumber",shipmentHdNumber);
            model.addAttribute("shipmentStatus",shipmentStatus);
            model.addAttribute("shipmentLocation",shipmentLocation);
            model.addAttribute("shipmentCreatedFrom",shipmentCreatedFrom);
            model.addAttribute("shipmentCreatedTo",shipmentCreatedTo);
            model.addAttribute("shipmentWarehouse",shipmentWarehouse);

//        Map<String,Integer> surveyMap =  shipmentService.surveyMap(warehouseId,SecurityUtils.username());
//        model.addAttribute("surveyMap",surveyMap);
            return "wmsOperations/shipment";
        }
        else{
            return "redirect:/shipment/shipments-browser";
        }
    }

    @GetMapping("/shipmentDetails/{shipmentNumber}")
    public String shipmentDetailsList(Model model,
                                      HttpSession session,
                                      @PathVariable Long shipmentNumber,
                                      @SessionAttribute(required = false) String chosenWarehouse,
                                      HttpServletRequest request) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")){
            List<Shipment> shipments = shipmentService.getShipmentsForLoggedUserByShipmentNumber(chosenWarehouse ,SecurityUtils.username(),shipmentNumber);
            usersService.loggedUserData(model,session);

            Warehouse warehouse = warehouseService.getWarehouseByName(chosenWarehouse);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("shipments", shipments);

//        Map<String,Integer> surveyMap =  shipmentService.surveyMap(warehouseId,SecurityUtils.username());
//        model.addAttribute("surveyMap",surveyMap);
            return "wmsOperations/shipmentDetails";
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


    @GetMapping("shipments-browser")
    public String browser(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<Customer> customers = customerService.getCustomer(SecurityUtils.username());
        model.addAttribute("customers", customers);
        List<Status> status = statusRepository.getStatusesByProcess("Shipment");
        model.addAttribute("status", status);

        usersService.loggedUserData(model, session);
        return "wmsOperations/shipments-browser";
    }

    @PostMapping("shipments-browser")
    public String findShipment(HttpSession session,
                               String shipmentWarehouse,
                               String shipmentCreatedBy,
                               String shipmentCompany,
                               String shipmentCustomer,
                               String shipmentShipmentNumber,
                               String shipmentHdNumber,
                               String shipmentStatus,
                               String shipmentLocation,
                               String shipmentCreatedFrom,
                               String shipmentCreatedTo) {
        session.setAttribute("shipmentWarehouse", shipmentWarehouse);
        session.setAttribute("shipmentCreatedBy", shipmentCreatedBy);
        session.setAttribute("shipmentCompany", shipmentCompany);
        session.setAttribute("shipmentCustomer", shipmentCustomer);
        session.setAttribute("shipmentShipmentNumber", shipmentShipmentNumber);
        session.setAttribute("shipmentHdNumber", shipmentHdNumber);
        session.setAttribute("shipmentStatus", shipmentStatus);
        session.setAttribute("shipmentLocation", shipmentLocation);
        session.setAttribute("shipmentCreatedFrom", shipmentCreatedFrom);
        session.setAttribute("shipmentCreatedTo", shipmentCreatedTo);

        log.info("Post warehouse: " + shipmentWarehouse);
        log.info("Post createdBy: " + shipmentCreatedBy);
        log.info("Post company: " + shipmentCompany );
        log.info("Post customer: " + shipmentCustomer);
        log.info("Post receptionNumber: " + shipmentShipmentNumber);
        log.info("Post hdNumber: " + shipmentHdNumber);
        log.info("Post status: " + shipmentStatus);
        log.info("Post location: " + shipmentLocation);
        log.info("Post createdFrom: " + shipmentCreatedFrom);
        log.info("Post createdTo: " + shipmentCreatedTo);
        return "redirect:/shipment/shipment";
    }


}
