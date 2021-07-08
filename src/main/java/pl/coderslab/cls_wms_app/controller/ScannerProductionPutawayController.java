package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
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
public class ScannerProductionPutawayController {

    private final WorkDetailsService workDetailsService;
    private final CompanyService companyService;
    private final WorkDetailsRepository workDetailsRepository;


    @Autowired
    public ScannerProductionPutawayController(WorkDetailsService workDetailsService, CompanyService companyService,  WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.companyService = companyService;
        this.workDetailsRepository = workDetailsRepository;
    }


    //Selection Putaway Work
    @GetMapping("{token}/{warehouse}/{equipment}/5/3")
    public String scannerProductionPutaway(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,
                                          @SessionAttribute(required = false) String scannerProductionPutawayMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", scannerProductionPutawayMessage);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/production/putaway/scannerProductionPutaway";
    }
    @PostMapping("scannerProductionPutaway")
    public String scannerProductionPutawayPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam Long productionNumber,
                                              HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                              @SessionAttribute int workProductionScannerChoice) {
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber,scannerChosenWarehouse,"Putaway after producing")>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            workDetailsService.changeStatusAfterStartWork(productionNumber,scannerChosenWarehouse);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Putaway after producing") > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else{
            session.setAttribute("scannerProductionPutawayMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
    }


    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/5/3/{productionNumber}")
    public String scannerProductionPutawayProductionNumberFound(@PathVariable String warehouse,@PathVariable String token,
                                                                @PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,
                                                                @SessionAttribute(required = false) String productionPutawayScannerLocationFromMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionPutawayScannerLocationFromMessage);

        WorkDetailsRepository.WorkToDoFoundProduction workToDoFound = workDetailsRepository.workToDoFoundProduction(productionNumberSearch,warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundOriginLocation";
    }

    @PostMapping("scannerProductionPutawayFoundOriginLocation")
    public String scannerProductionPutawayProductionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,@RequestParam String productionScannerFromLocation,
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
            session.setAttribute("productionPutawayScannerLocationFromMessage","Location from where you want pick up: " + originLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/5/3/{productionNumber}/hdNumber")
    public String scannerProductionPutawayProductionNumberFoundHdNumber(@PathVariable String warehouse,@PathVariable String token,
                                                                        @PathVariable String equipment, Model model,
                                                                        @SessionAttribute Long productionNumberSearch,
                                                                        @SessionAttribute(required = false) String productionPutawayScannerHdNumberMessage ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionPutawayScannerHdNumberMessage );

        WorkDetailsRepository.WorkToDoFoundProduction workToDoFound = workDetailsRepository.workToDoFoundProduction(productionNumberSearch,warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundHdNumber";
    }

    @PostMapping("scannerProductionPutawayFoundHdNumber")
    public String scannerProductionPutawayProductionNumberFoundHdNumberPost(@SessionAttribute String scannerChosenWarehouse,
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
            session.setAttribute("productionPutawayScannerHdNumberMessage","Entered HdNumber: " + productionScannerEnteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/5/3/{productionNumber}/hdNumber/article")
    public String scannerProductionPutawayProductionNumberFoundArticle(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment,
                                                                       Model model,@SessionAttribute Long productionNumberSearch,
                                                                       @SessionAttribute(required = false) String productionPutawayScannerArticleMessage ) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionPutawayScannerArticleMessage);

        WorkDetailsRepository.WorkToDoFoundProduction workToDoFound = workDetailsRepository.workToDoFoundProduction(productionNumberSearch,warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);

        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundArticle";
    }

    @PostMapping("scannerProductionPutawayFoundArticle")
    public String scannerProductionPutawayProductionNumberFoundArticlePost(@SessionAttribute String scannerChosenWarehouse,
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
            session.setAttribute("productionPutawayScannerArticleMessage","Entered article: " + productionScannerEnteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/5/3/{productionNumber}/hdNumber/article/toLocation")
    public String scannerProductionPutawayProductionNumberFoundDestinationLocation(@PathVariable String warehouse,
                                                                                 @PathVariable String token,
                                                                                 @PathVariable String equipment, Model model,
                                                                                 @SessionAttribute Long productionNumberSearch,
                                                                                 @SessionAttribute(required = false) String productionPutawayScannerLocationToMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", productionPutawayScannerLocationToMessage);

        WorkDetailsRepository.WorkToDoFoundProduction workToDoFound = workDetailsRepository.workToDoFoundProduction(productionNumberSearch,warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundDestinationLocation";
    }

    @PostMapping("scannerProductionPutawayFoundDestinationLocation")
    public String scannerProductionPutawayProductionNumberFoundDestinationLocationPost(@SessionAttribute String scannerChosenWarehouse,
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
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(productionScannerEnteredHdNumber),scannerChosenWarehouse,productionNumberSearch,productionScannerEnteredArticle);
            workDetailsService.workLineFinish(workDetails,scannerChosenEquipment);
            if(workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumberSearch.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Production picking") == 0 ){
                workDetailsService.workFinished(workDetails,session);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '5';
            }
            session.setAttribute("productionPutawayScannerLocationFromMessage","Work for: " + workDetails.getHdNumber() + " finished, start next one from your production");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;

        }
        else{
            session.setAttribute("productionPutawayScannerLocationToMessage","Entered destination location: " + productionScannerEnteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath;
        }
    }

}
