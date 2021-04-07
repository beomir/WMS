package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.Scanner;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final Scanner scanner;
    private final LocationRepository locationRepository;
    public String message;


    @Autowired
    public ScannerController(WorkDetailsService workDetailsService, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, Scanner scanner, LocationRepository locationRepository) {
        this.workDetailsService = workDetailsService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.scanner = scanner;
        this.locationRepository = locationRepository;
    }

    //equipment menu
    @GetMapping("{token}")
    public String scannerEquipment(@PathVariable String token, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("message", message);

        return "wmsOperations/scanner/scannerEquipment";
    }
    @PostMapping("scannerEquipment")
    public String scannerEquipmentPost(Scanner scanner,String token,@RequestParam String equipment, HttpSession session) {
        log.debug("Scanner Equipment Step scanner.scannerMenu: " +  scanner.equipment);
        if(locationRepository.checkEquipment(equipment)>0){
            session.setAttribute("scannerChosenEquipment", equipment);
            return "redirect:/scanner/" + token + '/' + equipment;
        }
        else {
            message = "Equipment: " + scanner.equipment +" ,not exists in DB";
            return "redirect:/scanner/" + token;
        }

    }

    //Main Scanner Menu
    @GetMapping("{token}/{equipment}")
    public String scannerMenu(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        message = "";

        return "wmsOperations/scanner/scannerMenu";
    }
    @PostMapping("scannerMenu")
    public String scannerMenuPost(@SessionAttribute String scannerChosenEquipment, Scanner scanner,String token,@RequestParam int scannerMenu, HttpSession session) {
        log.debug("Scanner Menu Step scanner.scannerMenu: " +  scanner.scannerMenu);
        session.setAttribute("scannerMenuChoice", scannerMenu);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenu;
    }
    //reception branch
    @GetMapping("{token}/{equipment}/1" )
    public String receptionMenu(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerReception";
    }
    @PostMapping("scannerReception")
    public String receptionMenuPost(Scanner scanner,String token, @RequestParam int scannerReception, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    @GetMapping("{token}/{equipment}/1/1" )
    public String receptionMenuManualWork(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerReceptionManualWork";
    }
    @PostMapping("scannerReceptionManualWork")
    public String receptionMenuManualWorkPost(Scanner scanner,String token, @RequestParam int scannerReception, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    @GetMapping("{token}/{equipment}/1/2" )
    public String receptionMenuAutomatWork(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerReceptionAutomatWork";
    }
    @PostMapping("scannerReceptionAutomatWork")
    public String receptionMenuAutomatWorkPost(Scanner scanner,String token, @RequestParam int scannerReception, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //shipment branch
    @GetMapping("{token}/{equipment}/3" )
    public String shipmentMenu(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerShipment";
    }
    @PostMapping("scannerShipment")
    public String shipmentMenuPost(Scanner scanner,String token, @RequestParam int scannerShipment, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerShipment);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerShipment;
    }

    //preview branch
    @GetMapping("{token}/{equipment}/4" )
    public String previewMenu(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerPreview";
    }
    @PostMapping("scannerPreview")
    public String previewMenuPost(Scanner scanner,String token, @RequestParam int scannerPreview, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }



    //stock branch
    @GetMapping("{token}/{equipment}/2" )
    public String stockMenu(@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("scanner", scanner);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        return "wmsOperations/scanner/scannerStock";
    }
    @PostMapping("scannerStock")
    public String stockMenuPost(Scanner scanner,String token, @RequestParam int scannerPreview, HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scanner.scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }


}
