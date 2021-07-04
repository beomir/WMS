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
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerProductionController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseRepository warehouseRepository;
    private final CompanyService companyService;
    private final LocationRepository locationRepository;
    private final WorkDetailsRepository workDetailsRepository;


    @Autowired
    public ScannerProductionController(WorkDetailsService workDetailsService, WarehouseRepository warehouseRepository, CompanyService companyService, LocationRepository locationRepository, WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
        this.workDetailsRepository = workDetailsRepository;
    }


    //Production branch
    @GetMapping("{token}/{warehouse}/{equipment}/5" )
    public String productionMenu(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,
                                 @SessionAttribute(required = false) String productionScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);

        model.addAttribute("message", productionScannerMessage);

        return "wmsOperations/scanner/production/scannerProduction";
    }
    @PostMapping("scannerProduction")
    public String productionMenuPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam int scannerProduction,
                                HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment) {
        log.error("Production Step scanner.scannerMenu: " +  scannerProduction);
        session.setAttribute("workProductionScannerChoice", scannerProduction);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerProduction;
    }


    //Manual Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/5/1")
    public String productionMenuManualWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,
                                          @SessionAttribute(required = false) String manualProductionScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", manualProductionScannerMessage);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/production/scannerProductionManualWork";
    }
    @PostMapping("scannerProductionManualWork")
    public String productionMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam Long productionNumber,
                                              HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                              @SessionAttribute int workProductionScannerChoice) {
        if(workDetailsRepository.checkIfWorksExistsForHandle(productionNumber.toString(),scannerChosenWarehouse)>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndHandle(productionNumber.toString(),scannerChosenWarehouse);
            for(WorkDetails singularWork : works){
                singularWork.setStatus(SecurityUtils.username());
                workDetailsRepository.save(singularWork);
                log.error("Production: " + singularWork.getId() + " " + singularWork.getWorkNumber());
            }
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUser(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username()) > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else{
            session.setAttribute("manualProductionScannerMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
    }


    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}")
    public String productionMenuManualWorkProductionNumberFound(@PathVariable String warehouse,@PathVariable String token,
                                                                @PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,
                                                                @SessionAttribute(required = false) String productionScannerLocationFromMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerLocationFromMessage);
        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/scannerProductionWorkFoundOriginLocation";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFound")
    public String productionMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String productionScannerFromLocation,
                                                                  @RequestParam String originLocation, HttpSession session,@SessionAttribute int scannerMenuChoice,
                                                                  @SessionAttribute String scannerChosenEquipment, @SessionAttribute int workProductionScannerChoice,
                                                                  @SessionAttribute Long productionNumberSearch) {
        log.error("Location found by query: " + productionScannerFromLocation);
        log.error("Location enter by user: " + originLocation);
        if(productionScannerFromLocation.equals(originLocation)){
            session.setAttribute("productionScannerFromLocation", productionScannerFromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + nextPath ;
        }
        else{
            session.setAttribute("productionScannerLocationFromMessage","Location from where you want pick up: " + originLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber")
    public String productionMenuManualWorkProductionNumberFoundHdNumber(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,@SessionAttribute(required = false) String productionScannerHdNumberMessage ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerHdNumberMessage );

        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/scannerProductionWorkFoundHdNumber";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundHdNumber")
    public String productionMenuManualWorkProductionNumberFoundHdNumberPost(@SessionAttribute String scannerChosenWarehouse,
                                                                          String token,@RequestParam String productionScannerExpectedHdNumber, @RequestParam String productionScannerEnteredHdNumber,
                                                                          HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                          @SessionAttribute int workProductionScannerChoice,@SessionAttribute Long productionNumberSearch) {
        log.error("HdNumber found by query: " + productionScannerExpectedHdNumber);
        log.error("HdNumber enter by user: " + productionScannerEnteredHdNumber);
        String nextPath = "article";
        String previous = "hdNumber";
        if(productionScannerExpectedHdNumber.equals(productionScannerEnteredHdNumber)){
            session.setAttribute("productionScannerEnteredHdNumber", productionScannerEnteredHdNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous + '/' + nextPath ;
        }
        else{
            session.setAttribute("productionScannerHdNumberMessage","Entered HdNumber: " + productionScannerEnteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber/article")
    public String productionMenuManualWorkProductionNumberFoundArticle(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment,
                                                                       Model model,@SessionAttribute Long productionNumberSearch,
                                                                       @SessionAttribute(required = false) String productionScannerArticleMessage ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionScannerArticleMessage);
        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);

        return "wmsOperations/scanner/production/scannerProductionWorkFoundArticle";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundArticle")
    public String productionMenuManualWorkProductionNumberFoundArticlePost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token,@RequestParam String productionScannerExpectedArticle, @RequestParam String productionScannerEnteredArticle,
                                                                         HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int workProductionScannerChoice,@SessionAttribute Long productionNumberSearch,
                                                                         @SessionAttribute String productionScannerEnteredHdNumber, @SessionAttribute String productionScannerFromLocation) {
        log.error("Article found by query: " + productionScannerExpectedArticle);
        log.error("Article enter by user: " + productionScannerEnteredArticle);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(productionScannerExpectedArticle.equals(productionScannerEnteredArticle)){
            session.setAttribute("productionScannerEnteredArticle", productionScannerEnteredArticle);
            workDetailsService.pickUpGoods(productionScannerFromLocation,productionScannerEnteredArticle,productionScannerEnteredHdNumber,scannerChosenEquipment,scannerChosenWarehouse,companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName());
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath ;

        }
        else{
            session.setAttribute("productionScannerArticleMessage","Entered article: " + productionScannerEnteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/5/1/{productionNumber}/hdNumber/article/toLocation")
    public String productionMenuManualWorkProductionNumberFoundDestinationLocation(@PathVariable String warehouse,
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
        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(productionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/scannerProductionWorkFoundDestinationLocation";
    }

    @PostMapping("productionMenuManualWorkProductionNumberFoundDestinationLocation")
    public String productionMenuManualWorkProductionNumberFoundDestinationLocationPost(@SessionAttribute String scannerChosenWarehouse,
                                                                                     String token,@RequestParam String productionScannerExpectedDestinationLocation, @RequestParam String productionScannerEnteredDestinationLocation,
                                                                                     HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                                                                     @SessionAttribute int workProductionScannerChoice,@SessionAttribute Long productionNumberSearch,
                                                                                     @SessionAttribute String productionScannerEnteredHdNumber, @SessionAttribute Long productionScannerEnteredArticle) {
        log.error("Destination location found by query: " + productionScannerExpectedDestinationLocation);
        log.error("Destination location enter by user: " + productionScannerEnteredDestinationLocation);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(productionScannerExpectedDestinationLocation.equals(productionScannerEnteredDestinationLocation)){
            session.setAttribute("enteredDestinationLocation", productionScannerEnteredDestinationLocation);
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(productionScannerEnteredHdNumber),scannerChosenWarehouse,productionNumberSearch.toString(),productionScannerEnteredArticle);
            workDetailsService.workLineFinish(workDetails,scannerChosenEquipment);
            if(workDetailsRepository.checkIfWorksExistsForHandleWithStatusUser(productionNumberSearch.toString(),scannerChosenWarehouse,SecurityUtils.username()) == 0 ){
                workDetailsService.workFinished(workDetails,session);
                session.setAttribute("productionScannerMenuMessage","Work: " + workDetails.getWorkNumber() + " for reception: " + workDetails.getHandle() + " finished. Goods are available on stock. Mail to customer sent");
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '5';
            }
            session.setAttribute("productionScannerLocationFromMessage","Work for: " + workDetails.getHdNumber() + " finished, start next one from your reception");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;

        }
        else{
            session.setAttribute("productionScannerLocationToMessage","Entered destination location: " + productionScannerEnteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath;
        }
    }

}
