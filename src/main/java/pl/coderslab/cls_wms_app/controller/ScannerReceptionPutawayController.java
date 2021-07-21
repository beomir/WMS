package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.WorkDetails;

import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerReceptionPutawayController {

    private final WorkDetailsService workDetailsService;
    private final CompanyService companyService;
    private final WorkDetailsRepository workDetailsRepository;

    @Autowired
    public ScannerReceptionPutawayController(WorkDetailsService workDetailsService,CompanyService companyService,  WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.companyService = companyService;
        this.workDetailsRepository = workDetailsRepository;
    }


    //reception branch
    @GetMapping("{token}/{warehouse}/{equipment}/1" )
    public String receptionMenu(@PathVariable String token,
                                @PathVariable String equipment,
                                @PathVariable String warehouse,
                                Model model,
                                @SessionAttribute(required = false) String receptionMenuMessage,
                                HttpSession session) {

        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);

        model.addAttribute("warehouse", warehouse);
        model.addAttribute("message", receptionMenuMessage);
        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");

        session.setAttribute("menuScannerMessage","");
        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");

        session.setAttribute("hdNumberMessage","");
        session.setAttribute("articleMessage","");

        return "wmsOperations/scanner/reception/scannerReception";
    }
    @PostMapping("scannerReception")
    public String receptionMenuPost(@SessionAttribute String scannerChosenWarehouse,
                                    String token,
                                    @RequestParam int scannerReception,
                                    HttpSession session,
                                    @SessionAttribute int scannerMenuChoice,
                                    @SessionAttribute String scannerChosenEquipment) {
        log.debug("Reception Step scanner.scannerMenu: " +  scannerReception);
        session.setAttribute("workReceptionScannerChoice", scannerReception);
        return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerReception;
    }

    //Selection Work
    @GetMapping("{token}/{warehouse}/{equipment}/1/1")
    public String receptionMenuManualWork(@PathVariable String warehouse,
                                          @PathVariable String token,
                                          @PathVariable String equipment,
                                          Model model,HttpSession session,
                                          @SessionAttribute(required = false) String manualReceptionMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);

        model.addAttribute("message", manualReceptionMessage);
        model.addAttribute("warehouse", warehouse);
        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");

        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");

        session.setAttribute("hdNumberMessage","");
        session.setAttribute("articleMessage","");
        List<WorkDetailsRepository.WorkHeaderListProduction> workDetailsQueue = workDetailsRepository.workHeaderListProduction(warehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(), "%", "Reception", "%", "%", SecurityUtils.username(), "%", "%", "%", "Reception Put Away");
        model.addAttribute("workDetailsQueue", workDetailsQueue);

        return "wmsOperations/scanner/reception/scannerReceptionManualWork";
    }
    @PostMapping("scannerReceptionManualWork")
    public String receptionMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,
                                              String token, Long receptionNumber,
                                              HttpSession session,
                                              @SessionAttribute int scannerMenuChoice,
                                              @SessionAttribute String scannerChosenEquipment,
                                              @SessionAttribute int workReceptionScannerChoice,
                                              String automaticallyFinder) {
        log.error("automaticallyFinder value: " + automaticallyFinder);
        if (automaticallyFinder == null && receptionNumber == null) {
            session.setAttribute("scannerProductionPutawayMessage", "Please enter production number manually or push button check to find work automatically by system ");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice;
        }
        else{
            if(automaticallyFinder.equals("automatically")){
                log.error("WorkNumberByCompanyAndWarehouse: " + workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Putaway after producing",SecurityUtils.username()));
                if(workDetailsRepository.handleByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Reception Put Away",SecurityUtils.username()) == null){
                    session.setAttribute("manualReceptionMessage", "No works found");
                    return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice;
                }
                else{
                    receptionNumber = workDetailsRepository.handleByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Reception Put Away",SecurityUtils.username());
                }
            }

            if(workDetailsRepository.checkIfWorksExistsForHandle(receptionNumber.toString(),scannerChosenWarehouse)>0){
                session.setAttribute("receptionNumberSearch", receptionNumber);
                List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndHandle(receptionNumber.toString(),scannerChosenWarehouse);
                for(WorkDetails singularWork : works){
                    singularWork.setStatus(SecurityUtils.username());
                    workDetailsRepository.save(singularWork);
                    log.error(singularWork.getId() + " " + singularWork.getWorkNumber());
                }
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumber;
            }
            else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUser(receptionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username()) > 0){
                session.setAttribute("receptionNumberSearch", receptionNumber);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumber;
            }
            else{
                session.setAttribute("manualReceptionMessage","To reception number: " + receptionNumber + " are not assigned any works to do");
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice;
            }
        }
    }

    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}")
    public String receptionMenuManualWorkReceptionNumberFound(@PathVariable String warehouse,
                                                              @PathVariable String token,
                                                              @PathVariable String equipment,
                                                              Model model,
                                                              @SessionAttribute(required = false) Long receptionNumberSearch,
                                                              HttpSession session,
                                                              @SessionAttribute(required = false) String locationFromMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);

        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", locationFromMessage);
        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");

        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");
        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationToMessage","");

        session.setAttribute("hdNumberMessage","");
        session.setAttribute("articleMessage","");

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/reception/scannerReceptionWorkFoundOriginLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFound")
    public String receptionMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,
                                                                  String token,
                                                                  @RequestParam String fromLocation,
                                                                  @RequestParam String originLocation,
                                                                  HttpSession session,
                                                                  @SessionAttribute int scannerMenuChoice,
                                                                  @SessionAttribute String scannerChosenEquipment,
                                                                  @SessionAttribute int workReceptionScannerChoice,
                                                                  @SessionAttribute Long receptionNumberSearch) {
        log.error("Location found by query: " + fromLocation);
        log.error("Location enter by user: " + originLocation);
        if(fromLocation.equals(originLocation)){
            session.setAttribute("fromLocation", fromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + nextPath ;
        }
        else{
            session.setAttribute("locationFromMessage","Location from where you want pick up: " + originLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber")
    public String receptionMenuManualWorkReceptionNumberFoundHdNumber(@PathVariable String warehouse,
                                                                      @PathVariable String token,
                                                                      @PathVariable String equipment,
                                                                      Model model,
                                                                      @SessionAttribute Long receptionNumberSearch,
                                                                      HttpSession session,
                                                                      @SessionAttribute(required = false) String hdNumberMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", hdNumberMessage);

        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");

        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");
        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");
        session.setAttribute("articleMessage","");

        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);
        return "wmsOperations/scanner/reception/scannerReceptionWorkFoundHdNumber";
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
            session.setAttribute("hdNumberMessage","Entered HdNumber: " + enteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber/article")
    public String receptionMenuManualWorkReceptionNumberFoundArticle(@PathVariable String warehouse,
                                                                     @PathVariable String token,
                                                                     @PathVariable String equipment,
                                                                     Model model,
                                                                     @SessionAttribute Long receptionNumberSearch,
                                                                     HttpSession session,
                                                                     @SessionAttribute(required = false) String articleMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);

        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", articleMessage);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);

        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");
        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");

        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("locationToMessage","");
        session.setAttribute("hdNumberMessage","");

        session.setAttribute("receptionPutawayPiecesQty",workToDoFound.getPiecesQty());
        log.error("receptionPutawayPiecesQty: " +workToDoFound.getPiecesQty());

        return "wmsOperations/scanner/reception/scannerReceptionWorkFoundArticle";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundArticle")
    public String receptionMenuManualWorkReceptionNumberFoundArticlePost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token,
                                                                         @RequestParam String expectedArticle,
                                                                         @RequestParam String enteredArticle,
                                                                         HttpSession session,
                                                                         @SessionAttribute int scannerMenuChoice,
                                                                         @SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int workReceptionScannerChoice,
                                                                         @SessionAttribute Long receptionNumberSearch,
                                                                         @SessionAttribute String enteredHdNumber,
                                                                         @SessionAttribute String fromLocation,
                                                                         @SessionAttribute Long receptionPutawayPiecesQty) {
        log.error("Article found by query: " + expectedArticle);
        log.error("Article enter by user: " + enteredArticle);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(expectedArticle.equals(enteredArticle)){
            session.setAttribute("enteredArticle", enteredArticle);
            workDetailsService.pickUpGoods(fromLocation,enteredArticle,enteredHdNumber,scannerChosenEquipment,scannerChosenWarehouse,companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),receptionNumberSearch.toString(),receptionPutawayPiecesQty,"ReceptionPutaway");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath ;

        }
        else{
            session.setAttribute("articleMessage","Entered article: " + enteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/1/1/{receptionNumber}/hdNumber/article/toLocation")
    public String receptionMenuManualWorkReceptionNumberFoundDestinationLocation(@PathVariable String warehouse,
                                                                                 @PathVariable String token,
                                                                                 @PathVariable String equipment,
                                                                                 Model model,
                                                                                 @SessionAttribute Long receptionNumberSearch,
                                                                                 HttpSession session,
                                                                                 @SessionAttribute(required = false) String locationToMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("receptionNumber", receptionNumberSearch);
        model.addAttribute("message", locationToMessage);
        WorkDetailsRepository.WorkToDoFound workToDoFound = workDetailsRepository.workToDoFound(receptionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFound);

        session.setAttribute("message","");
        session.setAttribute("equipmentMessage","");
        session.setAttribute("menuScannerMessage","");
        session.setAttribute("receptionMenuMessage","");

        session.setAttribute("manualReceptionMessage","");
        session.setAttribute("articleMessage","");
        session.setAttribute("locationFromMessage","");
        session.setAttribute("hdNumberMessage","");
        return "wmsOperations/scanner/reception/scannerReceptionWorkFoundDestinationLocation";
    }

    @PostMapping("receptionMenuManualWorkReceptionNumberFoundDestinationLocation")
    public String receptionMenuManualWorkReceptionNumberFoundDestinationLocationPost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token, @RequestParam String expectedDestinationLocation,
                                                                                     @RequestParam String enteredDestinationLocation,
                                                                         HttpSession session,
                                                                                     @SessionAttribute int scannerMenuChoice,
                                                                                     @SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int workReceptionScannerChoice,
                                                                                     @SessionAttribute Long receptionNumberSearch,
                                                                         @SessionAttribute String enteredHdNumber,
                                                                                     @SessionAttribute Long enteredArticle) {
        log.error("Destination location found by query: " + expectedDestinationLocation);
        log.error("Destination location enter by user: " + enteredDestinationLocation);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if(expectedDestinationLocation.equals(enteredDestinationLocation)){
            session.setAttribute("enteredDestinationLocation", enteredDestinationLocation);
            WorkDetails workDetails = workDetailsRepository.workLineFinish(Long.parseLong(enteredHdNumber),scannerChosenWarehouse,receptionNumberSearch.toString(),enteredArticle);
            workDetailsService.workLineFinish(workDetails,scannerChosenEquipment);
            if(workDetailsRepository.checkIfWorksExistsForHandleWithStatusUser(receptionNumberSearch.toString(),scannerChosenWarehouse,SecurityUtils.username()) == 0 ){
                workDetailsService.workFinished(workDetails,session);
                session.setAttribute("receptionMenuMessage","Work: " + workDetails.getWorkNumber() + " for reception: " + workDetails.getHandle() + " finished. Goods are available on stock. Mail to customer sent");
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '1';
            }
            session.setAttribute("locationFromMessage","Work for: " + workDetails.getHdNumber() + " finished, start next one from your reception");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch;

        }
        else{
            session.setAttribute("locationToMessage","Entered destination location: " + enteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workReceptionScannerChoice + '/' + receptionNumberSearch + '/' + prevprevious + '/' +  previous + '/' + nextPath;
        }
    }

}
