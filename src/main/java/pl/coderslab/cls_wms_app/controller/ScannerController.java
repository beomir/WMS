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
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

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
    public String equipmentMessage;
    public String menuScannerMessage;
    public String receptionMenuMessage;
    public String manualReceptionMessage;
    public String locationFromMessage;
    public String locationToMessage;
    public String hdNumberMessage;
    public String articleMessage;




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
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseRepository.getWarehouse();
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationFromMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";
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
            message = "Warehouse: " + warehouse +" ,not exists in DB";
            return "redirect:/scanner/" + token;
        }

    }

    //equipment menu
    @GetMapping("{token}/{warehouse}")
    public String scannerEquipment(@PathVariable String token,@PathVariable String warehouse, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("message", equipmentMessage);
        model.addAttribute("warehouse", warehouse);
        message = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationFromMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";
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
            equipmentMessage = "Equipment: " + equipment +" ,not exists in DB";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse;
        }

    }

    //Main Scanner Menu
    @GetMapping("{token}/{warehouse}/{equipment}")
    public String scannerMenu(@PathVariable String token,@PathVariable String equipment,@PathVariable String warehouse, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", menuScannerMessage);
        model.addAttribute("warehouse", warehouse);
        message = "";
        equipmentMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationFromMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";

        return "wmsOperations/scanner/scannerMenu";
    }
    @PostMapping("scannerMenu")
    public String scannerMenuPost(@SessionAttribute String scannerChosenWarehouse,@SessionAttribute String scannerChosenEquipment,
                                  String token,@RequestParam int scannerMenu, HttpSession session) {
        log.debug("Scanner Menu Step: " +  scannerMenu);
        session.setAttribute("scannerMenuChoice", scannerMenu);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenu;
    }
    //reception branch
    @GetMapping("{token}/{warehouse}/{equipment}/1" )
    public String receptionMenu(@PathVariable String token,@PathVariable String equipment,@PathVariable String warehouse, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("message", receptionMenuMessage);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        manualReceptionMessage = "";
        locationFromMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";
        return "wmsOperations/scanner/scannerReception";
    }
    @PostMapping("scannerReception")
    public String receptionMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerReception,
                                    HttpSession session,@SessionAttribute int scannerMenuChoice,
                                    @SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //Manual Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/1/1")
    public String receptionMenuManualWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", manualReceptionMessage);
        model.addAttribute("warehouse", warehouse);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        locationFromMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";
        return "wmsOperations/scanner/scannerReceptionManualWork";
    }
    @PostMapping("scannerReceptionManualWork")
    public String receptionMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam Long receptionNumber,
                                              HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                              @SessionAttribute int workReceptionScannerChoice) {
        if(workDetailsRepository.checkIfWorksExistsForHandle(receptionNumber.toString(),scannerChosenWarehouse)>0){
            session.setAttribute("receptionNumberSearch", receptionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumber;
        }
        else{
            manualReceptionMessage = "To reception number: " + receptionNumber + " are not assigned any works to do";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice;
        }
    }

    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}")
    public String receptionMenuManualWorkReceptionNumberFound(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", locationFromMessage);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationToMessage = "";
        hdNumberMessage = "";
        articleMessage = "";
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/scannerReceptionWorkFoundOriginLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFound")
    public String receptionMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String fromLocation,
                                                                  @RequestParam String originLocation, HttpSession session,@SessionAttribute int scannerMenuChoice,
                                                                  @SessionAttribute String scannerChosenEquipment, @SessionAttribute int workReceptionScannerChoice,
                                                                  @SessionAttribute Long receptionNumberSearch) {
        log.error("Location found by query: " + fromLocation);
        log.error("Location enter by user: " + originLocation);
        if(fromLocation.equals(originLocation)){
            session.setAttribute("fromLocation", fromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + nextPath ;
        }
        else{
            locationFromMessage = "Location from where you want pick up: " + originLocation + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber")
    public String receptionMenuManualWorkReceptionNumberFoundHdNumber(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", hdNumberMessage);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationToMessage = "";
        locationFromMessage = "";
        articleMessage = "";
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/scannerReceptionWorkFoundHdNumber";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundHdNumber")
    public String receptionMenuManualWorkReceptionNumberFoundHdNumberPost(@SessionAttribute String scannerChosenWarehouse,
                                                                          String token,@RequestParam String expectedHdNumber, @RequestParam String enteredHdNumber,
                                                                          HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                          @SessionAttribute int workReceptionScannerChoice,@SessionAttribute Long receptionNumberSearch) {
        log.error("HdNumber found by query: " + expectedHdNumber);
        log.error("HdNumber enter by user: " + enteredHdNumber);
        String nextPath = "article";
        String previous = "hdNumber";
        if(expectedHdNumber.equals(enteredHdNumber)){
            session.setAttribute("enteredHdNumber", enteredHdNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + previous + '/' + nextPath ;
        }
        else{
            hdNumberMessage = "Entered HdNumber: " + enteredHdNumber + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber/article")
    public String receptionMenuManualWorkReceptionNumberFoundArticle(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", articleMessage);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        locationToMessage = "";
        locationFromMessage = "";
        hdNumberMessage = "";
        return "wmsOperations/scanner/scannerReceptionWorkFoundArticle";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundArticle")
    public String receptionMenuManualWorkReceptionNumberFoundArticlePost(@SessionAttribute String scannerChosenWarehouse,
                                                                          String token,@RequestParam String expectedArticle, @RequestParam String enteredArticle,
                                                                          HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                          @SessionAttribute int workReceptionScannerChoice,@SessionAttribute Long receptionNumberSearch,
                                                                          @SessionAttribute String enteredHdNumber, @SessionAttribute String fromLocation) {
        log.error("Article found by query: " + expectedArticle);
        log.error("Article enter by user: " + enteredArticle);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(expectedArticle.equals(enteredArticle)){
            session.setAttribute("enteredArticle", enteredArticle);
            workDetailsService.pickUpGoods(fromLocation,enteredArticle,enteredHdNumber,scannerChosenEquipment,scannerChosenWarehouse,companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName());
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath ;

        }
        else{
            articleMessage = "Entered article: " + enteredArticle + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber/article/toLocation")
    public String receptionMenuManualWorkReceptionNumberFoundDestinationLocation(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long receptionNumberSearch ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", locationToMessage);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse);
        model.addAttribute("workToDoFound",workToDoFound);
        message = "";
        equipmentMessage = "";
        menuScannerMessage = "";
        receptionMenuMessage = "";
        manualReceptionMessage = "";
        articleMessage = "";
        locationFromMessage = "";
        hdNumberMessage = "";
        return "wmsOperations/scanner/scannerReceptionWorkFoundDestinationLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundDestinationLocation")
    public String receptionMenuManualWorkReceptionNumberFoundDestinationLocationPost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token,@RequestParam String expectedDestinationLocation, @RequestParam String enteredDestinationLocation,
                                                                         HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int workReceptionScannerChoice,@SessionAttribute Long receptionNumberSearch,
                                                                         @SessionAttribute String enteredHdNumber, @SessionAttribute Long enteredArticle,@SessionAttribute(required = false) String receptionMessage) {
        log.error("Destination location found by query: " + expectedDestinationLocation);
        log.error("Destination location enter by user: " + enteredDestinationLocation);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(expectedDestinationLocation.equals(enteredDestinationLocation)){
            session.setAttribute("enteredDestinationLocation", enteredDestinationLocation);
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(enteredHdNumber),scannerChosenWarehouse,receptionNumberSearch.toString(),enteredArticle);
            workDetailsService.workLineFinish(workDetails,scannerChosenEquipment);
            if(workDetailsRepository.checkIfWorksExistsForHandle(receptionNumberSearch.toString(),scannerChosenWarehouse) == 0 ){
                workDetailsService.workFinished(workDetails,session);
                receptionMenuMessage = "Work: " + workDetails.getWorkNumber() + " for reception: " + workDetails.getHandle() + " finished. Goods are available on stock. Mail to customer sent";
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '1';
            }
            locationFromMessage = "Work for: " + workDetails.getHdNumber() + " finished, start next one from your reception";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;

        }
        else{
            locationToMessage = "Entered destination location: " + enteredDestinationLocation + " is incorrect";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath;
        }
    }
    //Automatic Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/1/2" )
    public String receptionMenuAutomaticWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerReceptionAutomatWork";
    }
    @PostMapping("scannerReceptionAutomatWork")
    public String receptionMenuAutomaticWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerReception,
                                                 HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //shipment branch
    @GetMapping("{token}/{warehouse}/{equipment}/3" )
    public String shipmentMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerShipment";
    }
    @PostMapping("scannerShipment")
    public String shipmentMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerShipment,
                                   HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerShipment);
        session.setAttribute("workReceptionScannerChoice", scannerShipment);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerShipment;
    }

    //preview branch
    @GetMapping("{token}/{warehouse}/{equipment}/4" )
    public String previewMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerPreview";
    }
    @PostMapping("scannerPreview")
    public String previewMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerPreview,
                                  HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }



    //stock branch
    @GetMapping("{token}/{warehouse}/{equipment}/2" )
    public String stockMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/scannerStock";
    }
    @PostMapping("scannerStock")
    public String stockMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerPreview,
                                HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.debug("Shipment Step scanner.scannerMenu: " +  scannerPreview);
        session.setAttribute("workReceptionScannerChoice", scannerPreview);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerPreview;
    }


}
