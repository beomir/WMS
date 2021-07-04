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
public class ScannerProductionProduceController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseRepository warehouseRepository;
    private final CompanyService companyService;
    private final LocationRepository locationRepository;
    private final WorkDetailsRepository workDetailsRepository;


    @Autowired
    public ScannerProductionProduceController(WorkDetailsService workDetailsService, WarehouseRepository warehouseRepository, CompanyService companyService, LocationRepository locationRepository, WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.warehouseRepository = warehouseRepository;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
        this.workDetailsRepository = workDetailsRepository;
    }

    //Manual Selection Produce Work
    @GetMapping("{token}/{warehouse}/{equipment}/5/3")
    public String produceMenuManualWork(@PathVariable String warehouse,@PathVariable String token,@PathVariable String equipment, Model model,
                                          @SessionAttribute(required = false) String manualProduceScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", manualProduceScannerMessage);
        model.addAttribute("warehouse", warehouse);
        return "wmsOperations/scanner/production/produce/scannerProduceManualWork";
    }
    @PostMapping("scannerProduceManualWork")
    public String produceMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,String token, @RequestParam Long productionNumber,
                                              HttpSession session,@SessionAttribute int scannerMenuChoice,@SessionAttribute String scannerChosenEquipment,
                                              @SessionAttribute int workProductionScannerChoice) {
        if(workDetailsRepository.checkIfWorksExistsForHandleProduction(productionNumber.toString(),scannerChosenWarehouse,"Producing finish product from collected intermediate articles")>0){
            session.setAttribute("productionNumberSearch", productionNumber);
            List<WorkDetails> works = workDetailsRepository.getWorkListByWarehouseAndWorkNumber(productionNumber,scannerChosenWarehouse);
            for(WorkDetails singularWork : works){
                singularWork.setStatus(SecurityUtils.username());
                workDetailsRepository.save(singularWork);
                log.error("Production: " + singularWork.getId() + " " + singularWork.getWorkNumber());
            }
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else if( workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(productionNumber.toString(),scannerChosenWarehouse,SecurityUtils.username(),"Producing finish product from collected intermediate articles") > 0){
            session.setAttribute("productionNumberSearch", productionNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
        }
        else{
            session.setAttribute("manualProductionScannerMessage","To production number: " + productionNumber + " are not assigned any works to do");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
    }


    //confirm production
    @GetMapping("{token}/{warehouse}/{equipment}/5/3/{productionNumber}")
    public String produceMenuManualWorkProductionNumberFound(@PathVariable String warehouse,@PathVariable String token,
                                                                @PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,
                                                                @SessionAttribute(required = false) String manualProduceConfirmationScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", manualProduceConfirmationScannerMessage);
        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));
        WorkDetailsRepository.WorkToDoFoundProduction workToDoFoundProduction = workDetailsRepository.workToDoFoundProduction(productionNumberSearch.toString(),warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFoundProduction);

        List<WorkDetails> workDetailsList = workDetailsRepository.getWorkDetailsByWorkNumber(Long.parseLong(workToDoFoundProduction.getHandle()));
        model.addAttribute("workDetailsList",workDetailsList);
        return "wmsOperations/scanner/production/produce/scannerProduceConfirmationManualWork";
    }

    @PostMapping("produceMenuManualWorkProductionNumberFound")
    public String produceMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,
                                                                  @RequestParam Boolean produceConfirmation, HttpSession session,@SessionAttribute int scannerMenuChoice,
                                                                  @SessionAttribute String scannerChosenEquipment, @SessionAttribute int workProductionScannerChoice,
                                                                  @SessionAttribute Long productionNumberSearch) {
        if(produceConfirmation){
            workDetailsService.createPutAwayWork(productionNumberSearch);
            session.setAttribute("manualProduceScannerMessage", "Finish product: " + " " + " successfully produced. Putaway work: " + " " + " after production created");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
        else{
            session.setAttribute("manualProduceConfirmationScannerMessage","If content under this message is correct and to finish production with creation putaway works push the button Finish Production");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch ;
        }
    }



}
