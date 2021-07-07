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
    @GetMapping("{token}/{warehouse}/{equipment}/5/5")
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
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber,scannerChosenWarehouse,"Production picking")>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndHandle(productionNumber.toString(),scannerChosenWarehouse);
            for(WorkDetails singularWork : works){
                singularWork.setStatus(SecurityUtils.username());
                workDetailsRepository.save(singularWork);
                log.error("Production: " + singularWork.getId() + " " + singularWork.getWorkNumber());
            }
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Production picking") > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else{
            session.setAttribute("manualProductionScannerMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
    }


    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/5/5/{productionNumber}")
    public String scannerProductionPutawayProductionNumberFound(@PathVariable String warehouse,@PathVariable String token,
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
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundOriginLocation";
    }

    @PostMapping("scannerProductionPutawayProductionNumberFound")
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
            session.setAttribute("productionScannerLocationFromMessage","Location from where you want pick up: " + originLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/5/5/{productionNumber}/hdNumber")
    public String scannerProductionPutawayProductionNumberFoundHdNumber(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,@SessionAttribute(required = false) String productionScannerHdNumberMessage ) {
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
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundHdNumber";
    }

    @PostMapping("scannerProductionPutawayProductionNumberFoundHdNumber")
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
            session.setAttribute("productionScannerHdNumberMessage","Entered HdNumber: " + productionScannerEnteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/5/5/{productionNumber}/hdNumber/article")
    public String scannerProductionPutawayProductionNumberFoundArticle(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment,
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

        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundArticle";
    }

    @PostMapping("scannerProductionPutawayProductionNumberFoundArticle")
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
            session.setAttribute("productionScannerArticleMessage","Entered article: " + productionScannerEnteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/5/5/{productionNumber}/hdNumber/article/toLocation")
    public String scannerProductionPutawayProductionNumberFoundDestinationLocation(@PathVariable String warehouse,
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
        return "wmsOperations/scanner/production/putaway/scannerProductionPutawayFoundDestinationLocation";
    }

    @PostMapping("scannerProductionPutawayProductionNumberFoundDestinationLocation")
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
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(productionScannerEnteredHdNumber),scannerChosenWarehouse,productionNumberSearch.toString(),productionScannerEnteredArticle);
            workDetailsService.workLineFinish(workDetails,scannerChosenEquipment);
            if(workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumberSearch.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Production picking") == 0 ){
                workDetailsService.workFinished(workDetails,session);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '5';
            }
            session.setAttribute("productionScannerLocationFromMessage","Work for: " + workDetails.getHdNumber() + " finished, start next one from your production");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch;

        }
        else{
            session.setAttribute("productionScannerLocationToMessage","Entered destination location: " + productionScannerEnteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath;
        }
    }

}
