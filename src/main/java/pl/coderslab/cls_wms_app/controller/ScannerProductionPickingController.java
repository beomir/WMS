package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerProductionPickingController {

    private final WorkDetailsService workDetailsService;
    private final CompanyService companyService;
    private final WorkDetailsRepository workDetailsRepository;
    private final LocationRepository locationRepository;

    private final UsersService usersService;


    @Autowired
    public ScannerProductionPickingController(WorkDetailsService workDetailsService, CompanyService companyService, WorkDetailsRepository workDetailsRepository, LocationRepository locationRepository, UsersService usersService) {
        this.workDetailsService = workDetailsService;
        this.companyService = companyService;
        this.workDetailsRepository = workDetailsRepository;
        this.locationRepository = locationRepository;
        this.usersService = usersService;
    }

    //Production branch
    @GetMapping("{token}/{warehouse}/{equipment}/5")
    public String productionMenu(@PathVariable String warehouse,
                                 @PathVariable String token,
                                 @PathVariable String equipment,
                                 Model model,
                                 HttpServletRequest request,
                                 @SessionAttribute(required = false) String productionScannerMessage,
                                 @SessionAttribute(required = false) String scannerProductionPutawayMessage,
                                 @SessionAttribute(required = false) String produceScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);

        if(request.getHeader("Referer").contains("/5/1")){
            model.addAttribute("message", productionScannerMessage);
        }
        if(request.getHeader("Referer").contains("/5/3")){
            model.addAttribute("message", scannerProductionPutawayMessage);
        }
        if(request.getHeader("Referer").contains("/5/2")){
            model.addAttribute("message", produceScannerMessage);
        }


        return "wmsOperations/scanner/production/scannerProduction";
    }

    @PostMapping("scannerProduction")
    public String pickingMenuPost(@SessionAttribute String scannerChosenWarehouse, String token, @RequestParam int scannerProduction,
                                  HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment) {
        log.error("Production Step scanner.scannerMenu: " + scannerProduction);
        session.setAttribute("workProductionScannerChoice", scannerProduction);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerProduction;
    }


    //Selection Picking Work
    @GetMapping("{token}/{warehouse}/{equipment}/5/1")
    public String pickingMenuManualWork(@PathVariable String warehouse,
                                        @PathVariable String token,
                                        @PathVariable String equipment,
                                        Model model,
                                        @SessionAttribute(required = false) String manualProductionScannerMessage,
                                        HttpSession session,
                                        HttpServletRequest request) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", manualProductionScannerMessage);
        model.addAttribute("warehouse", warehouse);

        usersService.nextURL(request,session);

        List<WorkDetailsRepository.WorkHeaderListProduction> workDetailsQueue = workDetailsRepository.workHeaderListProduction(warehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(), "%", "Production", "%", "%", SecurityUtils.username(), "%", "%", "%", "Production picking");
        model.addAttribute("workDetailsQueue", workDetailsQueue);
        return "wmsOperations/scanner/production/picking/scannerProductionPicking";
    }

    @PostMapping("scannerProductionManualWork")
    public String pickingMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,
                                            String token,
                                            Long productionNumber,
                                            HttpSession session,
                                            @SessionAttribute int scannerMenuChoice,
                                            @SessionAttribute String scannerChosenEquipment,
                                            @SessionAttribute int workProductionScannerChoice,
                                            String automaticallyFinder) {
        log.error("automaticallyFinder value: " + automaticallyFinder);
        if (automaticallyFinder == null && productionNumber == null) {
            session.setAttribute("manualProductionScannerMessage", "Please enter production number manually or push button check to find work automatically by system ");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
            else {
            if(automaticallyFinder != null){
                log.error("workNumberBrCompanyAndWarehouse: " + workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Production picking",SecurityUtils.username()));
                if(workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Production picking",SecurityUtils.username()) == null){
                    session.setAttribute("manualProductionScannerMessage", "No works found");
                    return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
                }
                else{
                    productionNumber = workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Production picking",SecurityUtils.username());
                    session.setAttribute("productionNumberSearch", productionNumber);
                }
            }
            //when on equipment is not enough space for pick goods
            WorkDetailsRepository.MaxVolumeAndWeightForWork maxVolumeAndWeightForWork = workDetailsRepository.maxVolumeAndWeightForWork(productionNumber.toString());
            Location equipment = locationRepository.findLocationByLocationName(scannerChosenEquipment,scannerChosenWarehouse);
            log.error("maxVolumeAndWeightForWork.getHandle(): " + maxVolumeAndWeightForWork.getHandle());
            log.error("equipment.getFreeWeight(): " + equipment.getFreeWeight());
            log.error("maxVolumeAndWeightForWork.getPiecesQty(): " + maxVolumeAndWeightForWork.getPiecesQty());
            log.error("equipment.getFreeSpace(): " + equipment.getFreeSpace());
            if(maxVolumeAndWeightForWork.getHandle()>equipment.getFreeWeight() || maxVolumeAndWeightForWork.getPiecesQty()>equipment.getFreeSpace()){
                log.error("OverloadedPath");
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + productionNumber + "/equipmentOverloaded" ;
            }


            //work for production found logic
            if(workDetailsService.productionPickingWorkSearch(session,productionNumber,scannerChosenWarehouse)){
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
            }
            else{
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
            }

        }
    }


    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}")
    public String pickingMenuManualWorkProductionNumberFound(@PathVariable String warehouse, @PathVariable String token,
                                                             @PathVariable String equipment, Model model, @SessionAttribute Long productionNumberSearch,
                                                             @SessionAttribute(required = false) String productionScannerLocationFromMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerLocationFromMessage);
        model.addAttribute("finishProductNumber", workDetailsRepository.finishProductNumber(productionNumberSearch.toString(), warehouse));

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(), warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFound);
        return "wmsOperations/scanner/production/picking/scannerProductionPickingFoundOriginLocation";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFound")
    public String pickingMenuManualWorkProductionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse, String token, @RequestParam String productionScannerFromLocation,
                                                                 @RequestParam String originLocation, HttpSession session, @SessionAttribute int scannerMenuChoice,
                                                                 @SessionAttribute String scannerChosenEquipment, @SessionAttribute int workProductionScannerChoice,
                                                                 @SessionAttribute Long productionNumberSearch) {
        log.error("Location found by query: " + productionScannerFromLocation);
        log.error("Location enter by user: " + originLocation);
        if (productionScannerFromLocation.equals(originLocation)) {
            session.setAttribute("productionScannerFromLocation", productionScannerFromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + nextPath;
        } else {
            session.setAttribute("productionScannerLocationFromMessage", "Location from where you want pick up: " + originLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber")
    public String pickingMenuManualWorkProductionNumberFoundHdNumber(@PathVariable String warehouse, @PathVariable String token, @PathVariable String equipment, Model model, @SessionAttribute Long productionNumberSearch, @SessionAttribute(required = false) String productionScannerHdNumberMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerHdNumberMessage);

        model.addAttribute("finishProductNumber", workDetailsRepository.finishProductNumber(productionNumberSearch.toString(), warehouse));
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(), warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFound);
        return "wmsOperations/scanner/production/picking/scannerProductionPickingFoundHdNumber";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundHdNumber")
    public String pickingMenuManualWorkProductionNumberFoundHdNumberPost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token, @RequestParam String productionScannerExpectedHdNumber, @RequestParam String productionScannerEnteredHdNumber,
                                                                         HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int workProductionScannerChoice, @SessionAttribute Long productionNumberSearch) {
        log.error("HdNumber found by query: " + productionScannerExpectedHdNumber);
        log.error("HdNumber enter by user: " + productionScannerEnteredHdNumber);
        String nextPath = "article";
        String previous = "hdNumber";
        if (productionScannerExpectedHdNumber.equals(productionScannerEnteredHdNumber)) {
            session.setAttribute("productionScannerEnteredHdNumber", productionScannerEnteredHdNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous + '/' + nextPath;
        } else {
            session.setAttribute("productionScannerHdNumberMessage", "Entered HdNumber: " + productionScannerEnteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber/article")
    public String pickingMenuManualWorkProductionNumberFoundArticle(@PathVariable String warehouse, @PathVariable String token, @PathVariable String equipment,
                                                                    Model model, @SessionAttribute Long productionNumberSearch,
                                                                    @SessionAttribute(required = false) String productionScannerArticleMessage,HttpSession session) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerArticleMessage);
        model.addAttribute("finishProductNumber", workDetailsRepository.finishProductNumber(productionNumberSearch.toString(), warehouse));

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(), warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFound);

        session.setAttribute("productionPickingPiecesQty",workToDoFound.getPiecesQty());
        return "wmsOperations/scanner/production/picking/scannerProductionPickingFoundArticle";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundArticle")
    public String pickingMenuManualWorkProductionNumberFoundArticlePost(@SessionAttribute String scannerChosenWarehouse,
                                                                        String token, @RequestParam String productionScannerExpectedArticle, @RequestParam String productionScannerEnteredArticle,
                                                                        HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                        @SessionAttribute int workProductionScannerChoice, @SessionAttribute Long productionNumberSearch,
                                                                        @SessionAttribute String productionScannerEnteredHdNumber,
                                                                        @SessionAttribute String productionScannerFromLocation,
                                                                        @SessionAttribute Long productionPickingPiecesQty) throws CloneNotSupportedException {
        log.error("Article found by query: " + productionScannerExpectedArticle);
        log.error("Article enter by user: " + productionScannerEnteredArticle);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if (productionScannerExpectedArticle.equals(productionScannerEnteredArticle)) {
            session.setAttribute("productionScannerEnteredArticle", productionScannerEnteredArticle);
            workDetailsService.pickUpGoods(productionScannerFromLocation, productionScannerEnteredArticle, productionScannerEnteredHdNumber, scannerChosenEquipment, scannerChosenWarehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),productionNumberSearch.toString(),productionPickingPiecesQty,"ProductionPicking");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous + '/' + nextPath;

        } else {
            session.setAttribute("productionScannerArticleMessage", "Entered article: " + productionScannerEnteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber/article/toLocation")
    public String pickingMenuManualWorkProductionNumberFoundDestinationLocation(@PathVariable String warehouse,
                                                                                @PathVariable String token,
                                                                                @PathVariable String equipment, Model model,
                                                                                @SessionAttribute Long productionNumberSearch,
                                                                                @SessionAttribute(required = false) String productionScannerLocationToMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerLocationToMessage);
        model.addAttribute("finishProductNumber", workDetailsRepository.finishProductNumber(productionNumberSearch.toString(), warehouse));

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(), warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFound);
        return "wmsOperations/scanner/production/picking/scannerProductionPickingFoundDestinationLocation";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundDestinationLocation")
    public String pickingMenuManualWorkProductionNumberFoundDestinationLocationPost(@SessionAttribute String scannerChosenWarehouse,
                                                                                    String token, @RequestParam String productionScannerExpectedDestinationLocation, @RequestParam String productionScannerEnteredDestinationLocation,
                                                                                    HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                                    @SessionAttribute int workProductionScannerChoice, @SessionAttribute Long productionNumberSearch,
                                                                                    @SessionAttribute String productionScannerEnteredHdNumber, @SessionAttribute Long productionScannerEnteredArticle) {
        log.error("Destination location found by query: " + productionScannerExpectedDestinationLocation);
        log.error("Destination location enter by user: " + productionScannerEnteredDestinationLocation);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if (productionScannerExpectedDestinationLocation.equals(productionScannerEnteredDestinationLocation)) {
            session.setAttribute("enteredDestinationLocation", productionScannerEnteredDestinationLocation);
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(productionScannerEnteredHdNumber), scannerChosenWarehouse, productionNumberSearch.toString(), productionScannerEnteredArticle);
            workDetailsService.workLineFinish(workDetails, scannerChosenEquipment);
            if (workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumberSearch.toString(), scannerChosenWarehouse, SecurityUtils.username(), "Production picking") == 0) {
                workDetailsService.workFinished(workDetails, session);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '5';
            }
            session.setAttribute("productionScannerLocationFromMessage", "Work for: " + workDetails.getHdNumber() + " finished, start next one from your production");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;

        } else {
            session.setAttribute("productionScannerLocationToMessage", "Entered destination location: " + productionScannerEnteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous + '/' + nextPath;
        }
    }

}
