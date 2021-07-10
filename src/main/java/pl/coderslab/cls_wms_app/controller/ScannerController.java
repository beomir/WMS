package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerController {

    private final WarehouseRepository warehouseRepository;
    private final CompanyService companyService;
    private final LocationRepository locationRepository;

    @Autowired
    public ScannerController( WarehouseRepository warehouseRepository, CompanyService companyService, LocationRepository locationRepository) {
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
    }

    //warehouse selection
    @GetMapping("{token}")
    public String scannerWarehouse(@PathVariable String token, Model model,
                                   @SessionAttribute(required = false) String message,
                                   HttpSession session) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseRepository.getWarehouse();

        session.setAttribute("equipmentMessage","");
        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");
        session.setAttribute("manualReceptionMessage","");

        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");
        session.setAttribute("hdNumberMessage","");
        session.setAttribute("articleMessage","");

        model.addAttribute("companies", companies);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("token", token);
        model.addAttribute("message", message);
        return "wmsOperations/scanner/scannerWarehouse";
    }
    @PostMapping("scannerWarehouse")
    public String scannerWarehousePost(String token,@RequestParam String warehouse, HttpSession session) {
        log.debug("Scanner Warehouse Step: " +  warehouse);
        if(warehouseRepository.checkWarehouse(warehouse)>0){
            session.setAttribute("scannerChosenWarehouse", warehouse);
            return "redirect:/scanner/" + token + '/' + warehouse;
        }
        else {
            session.setAttribute("message","Warehouse: " + warehouse +" ,not exists in DB");
            return "redirect:/scanner/" + token;
        }

    }

    //equipment menu
    @GetMapping("{token}/{warehouse}")
    public String scannerEquipment(@PathVariable String token,
                                   @PathVariable String warehouse,
                                   Model model,
                                   HttpSession session,
                                   @SessionAttribute(required = false) String equipmentMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("message", equipmentMessage);

        model.addAttribute("warehouse", warehouse);
        session.setAttribute("message","");
        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");

        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");
        session.setAttribute("hdNumberMessage","");

        session.setAttribute("articleMessage","");

        return "wmsOperations/scanner/scannerEquipment";
    }
    @PostMapping("scannerEquipment")
    public String scannerEquipmentPost(@SessionAttribute String scannerChosenWarehouse,
                                       String token,
                                       @RequestParam String equipment,
                                       HttpSession session) {
        log.debug("Scanner Equipment Step scanner.scannerMenu: " + equipment);
        if(locationRepository.checkEquipment(equipment,scannerChosenWarehouse)>0){
            session.setAttribute("scannerChosenEquipment", equipment);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + equipment;
        }
        else {
            session.setAttribute("equipmentMessage","Equipment: " + equipment +" ,not exists in DB");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse;
        }

    }

    //Main Scanner Menu
    @GetMapping("{token}/{warehouse}/{equipment}")
    public String scannerMenu(@PathVariable String token,
                              @PathVariable String equipment,
                              @PathVariable String warehouse,
                              Model model,
                              HttpSession session,
                              @SessionAttribute(required = false) String menuScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());

        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", menuScannerMessage);

        model.addAttribute("warehouse", warehouse);
        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");
        session.setAttribute("receptionMenuMessage","");

        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");
        session.setAttribute("hdNumberMessage","");

        session.setAttribute("articleMessage","");

        return "wmsOperations/scanner/scannerMenu";
    }
    @PostMapping("scannerMenu")
    public String scannerMenuPost(@SessionAttribute String scannerChosenWarehouse,@SessionAttribute String scannerChosenEquipment,
                                  String token,@RequestParam int scannerMenu, HttpSession session) {
        log.debug("Scanner Menu Step: " +  scannerMenu);
        session.setAttribute("scannerMenuChoice", scannerMenu);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenu;
    }


    //shipment branch
    @GetMapping("{token}/{warehouse}/{equipment}/3" )
    public String shipmentMenu(@PathVariable String warehouse,
                               @PathVariable String token,
                               @PathVariable String equipment,
                               Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerShipment";
    }
    @PostMapping("scannerShipment")
    public String shipmentMenuPost(@SessionAttribute String scannerChosenWarehouse,
                                   String token,
                                   @RequestParam int scannerShipment,
                                   HttpSession session,
                                   @SessionAttribute int scannerMenuChoice,
                                   @SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerShipment);
        session.setAttribute("workReceptionScannerChoice", scannerShipment);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerShipment;
    }

    //preview branch
    @GetMapping("{token}/{warehouse}/{equipment}/4" )
    public String previewMenu(@PathVariable String warehouse,
                              @PathVariable String token,
                              @PathVariable String equipment,
                              Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerPreview";
    }
    @PostMapping("scannerPreview")
    public String previewMenuPost(@SessionAttribute String scannerChosenWarehouse,
                                  String token,
                                  @RequestParam int scannerPreview,
                                  HttpSession session,
                                  @SessionAttribute int scannerMenuChoice,
                                  @SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }



    //stock branch
    @GetMapping("{token}/{warehouse}/{equipment}/2" )
    public String stockMenu(@PathVariable String warehouse,
                            @PathVariable String token,
                            @PathVariable String equipment,
                            Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerStock";
    }
    @PostMapping("scannerStock")
    public String stockMenuPost(@SessionAttribute String scannerChosenWarehouse,
                                String token,
                                @RequestParam int scannerPreview,
                                HttpSession session,
                                @SessionAttribute int scannerMenuChoice,
                                @SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }





}
