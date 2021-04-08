package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.temporaryObjects.Scanner;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseRepository warehouseRepository;
    private final CompanyService companyService;
    private final LocationRepository locationRepository;
    private final WorkDetailsRepository workDetailsRepository;
    public String message;

    @Autowired
    public ScannerController(WorkDetailsService workDetailsService, WarehouseRepository warehouseRepository, CompanyService companyService, LocationRepository locationRepository, WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
        this.workDetailsRepository = workDetailsRepository;
    }

    //warehouse selection
    @GetMapping("{token}")
    public String scannerWarehouse(@PathVariable String token, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseRepository.getWarehouse();
        model.addAttribute("companys", companys);
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
            message = "Warehouse: " + warehouse +" ,not exists in DB";
            return "redirect:/scanner/" + token;
        }

    }

    //equipment menu
    @GetMapping("{token}/{warehouse}")
    public String scannerEquipment(@PathVariable String token,@PathVariable String warehouse, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("message", message);
        model.addAttribute("warehouse", warehouse);

        return "wmsOperations/scanner/scannerEquipment";
    }
    @PostMapping("scannerEquipment")
    public String scannerEquipmentPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String equipment, HttpSession session) {
        log.debug("Scanner Equipment Step scanner.scannerMenu: " + equipment);
        if(locationRepository.checkEquipment(equipment,scannerChosenWarehouse)>0){
            session.setAttribute("scannerChosenEquipment", equipment);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + equipment;
        }
        else {
            message = "Equipment: " + equipment +" ,not exists in DB";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse;
        }

    }

    //Main Scanner Menu
    @GetMapping("{token}/{warehouse}/{equipment}")
    public String scannerMenu(@PathVariable String token,@PathVariable String equipment,@PathVariable String warehouse, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        message = "";

        return "wmsOperations/scanner/scannerMenu";
    }
    @PostMapping("scannerMenu")
    public String scannerMenuPost(@SessionAttribute String scannerChosenWarehouse,@SessionAttribute String scannerChosenEquipment,String token,@RequestParam int scannerMenu, HttpSession session) {
        log.debug("Scanner Menu Step scanner.scannerMenu: " +  scannerMenu);
        session.setAttribute("scannerMenuChoice", scannerMenu);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenu;
    }
    //reception branch
    @GetMapping("{token}/{warehouse}/{equipment}/1" )
    public String receptionMenu(@PathVariable String token,@PathVariable String equipment,@PathVariable String warehouse, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerReception";
    }
    @PostMapping("scannerReception")
    public String receptionMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerReception, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //Manual Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/1/1")
    public String receptionMenuManualWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerReceptionManualWork";
    }
    @PostMapping("scannerReceptionManualWork")
    public String receptionMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam Long receptionNumber, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment, @SessionAttribute int workReceptionScannerChoice) {
        if(workDetailsRepository.checkIfWorksExistsForHandle(receptionNumber.toString(),scannerChosenWarehouse)>0){
            session.setAttribute("receptionNumberSearch", receptionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumber;
        }
        else{
            message = "To reception number: " + receptionNumber + " are not assigned any works to do";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice;
        }
    }

    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}")
    public String receptionMenuManualWorkReceptionNumberFound(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/scannerReceptionWorkFoundOriginLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFound")
    public String receptionMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String fromLocation, @RequestParam String originLocation, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment, @SessionAttribute int workReceptionScannerChoice,@SessionAttribute Long receptionNumberSearch) {
        log.error("Location found by query: " + fromLocation);
        log.error("Location enter by user: " + originLocation);
        if(fromLocation.equals(originLocation)){
            session.setAttribute("fromLocation", fromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + nextPath ;
        }
        else{
            message = "Location from where you want pick up: " + originLocation + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;
        }
    }
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber")
    public String receptionMenuManualWorkReceptionNumberFoundHdNumber(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/scannerReceptionWorkFoundOriginLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundHdNumber")
    public String receptionMenuManualWorkReceptionNumberFoundHdNumberPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String fromLocation, @RequestParam String originLocation, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment, @SessionAttribute int workReceptionScannerChoice,@SessionAttribute Long receptionNumberSearch) {
        log.error("Location found by query: " + fromLocation);
        log.error("Location enter by user: " + originLocation);
        if(fromLocation.equals(originLocation)){
            session.setAttribute("fromLocation", fromLocation);
            String nextPath = "article";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + nextPath ;
        }
        else{
            message = "Location from where you want pick up: " + originLocation + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;
        }
    }
    //Automatic Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/1/2" )
    public String receptionMenuAutomaticWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerReceptionAutomatWork";
    }
    @PostMapping("scannerReceptionAutomatWork")
    public String receptionMenuAutomaticWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerReception, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //shipment branch
    @GetMapping("{token}/{warehouse}/{equipment}/3" )
    public String shipmentMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerShipment";
    }
    @PostMapping("scannerShipment")
    public String shipmentMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerShipment, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerShipment);
        session.setAttribute("workReceptionScannerChoice", scannerShipment);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerShipment;
    }

    //preview branch
    @GetMapping("{token}/{warehouse}/{equipment}/4" )
    public String previewMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerPreview";
    }
    @PostMapping("scannerPreview")
    public String previewMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerPreview, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }



    //stock branch
    @GetMapping("{token}/{warehouse}/{equipment}/2" )
    public String stockMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerStock";
    }
    @PostMapping("scannerStock")
    public String stockMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerPreview, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }


}
