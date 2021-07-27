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
import pl.coderslab.cls_wms_app.service.storage.StockService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerProductionProduceController {

    private final WorkDetailsService workDetailsService;
    private final StockService stockService;
    private final CompanyService companyService;
    private final WorkDetailsRepository workDetailsRepository;
    private final UsersService usersService;


    @Autowired
    public ScannerProductionProduceController(WorkDetailsService workDetailsService, StockService stockService, CompanyService companyService, WorkDetailsRepository workDetailsRepository, UsersService usersService) {
        this.workDetailsService = workDetailsService;
        this.stockService = stockService;
        this.companyService = companyService;
        this.workDetailsRepository = workDetailsRepository;
        this.usersService = usersService;
    }

    //Selection Produce Work
    @GetMapping("{token}/{warehouse}/{equipment}/5/2")
    public String produceMenuManualWork(@PathVariable String warehouse,
                                        @PathVariable String token,
                                        @PathVariable String equipment,
                                        Model model,
                                        @SessionAttribute(required = false) String produceScannerMessage,
                                        HttpSession session,
                                        HttpServletRequest request) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", produceScannerMessage);
        model.addAttribute("warehouse", warehouse);

        usersService.nextURL(request,session);

        List<WorkDetailsRepository.WorkHeaderListProduction> workDetailsQueue = workDetailsRepository.workHeaderListProduction(warehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(), "%", "Production", "%", "%", SecurityUtils.username(), "%", "%", "%", "Producing finish product from collected intermediate articles");
        model.addAttribute("workDetailsQueue", workDetailsQueue);

        return "wmsOperations/scanner/production/produce/scannerProduceManualWork";
    }
    @PostMapping("scannerProduceManualWork")
    public String produceMenuManualWorkPost(@SessionAttribute String scannerChosenWarehouse,
                                            String token,
                                            Long productionNumber,
                                            HttpSession session,
                                            @SessionAttribute int scannerMenuChoice,
                                            @SessionAttribute String scannerChosenEquipment,
                                            @SessionAttribute int workProductionScannerChoice,
                                            String automaticallyFinder) {
        log.error("automaticallyFinder value: " + automaticallyFinder);
        if (automaticallyFinder == null && productionNumber == null) {
            session.setAttribute("produceScannerMessage", "Please enter production number manually or push button check to find work automatically by system ");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
        }
        else{

            if(automaticallyFinder != null){
                log.error("workNumberBrCompanyAndWarehouse: " + workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Producing finish product from collected intermediate articles",SecurityUtils.username()));
                if(workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Producing finish product from collected intermediate articles",SecurityUtils.username()) == null){
                    session.setAttribute("produceScannerMessage", "No works found");
                    return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
                }
                else{
                    productionNumber = workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Producing finish product from collected intermediate articles",SecurityUtils.username());
                    session.setAttribute("productionNumberSearch", productionNumber);
                }
            }

            //work for production found logic
            if(workDetailsService.productionProduceWorkSearch(session,productionNumber,scannerChosenWarehouse)){
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumber;
            }
            else{
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice;
            }

        }

    }


    //confirm production
    @GetMapping("{token}/{warehouse}/{equipment}/5/2/{productionNumber}")
    public String produceMenuManualWorkProductionNumberFound(@PathVariable String warehouse,@PathVariable String token,
                                                                @PathVariable String equipment, Model model,@SessionAttribute Long productionNumberSearch,
                                                                @SessionAttribute(required = false) String manualProduceConfirmationScannerMessage,@SessionAttribute(required = false) String produceScannerMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("productionNumber", productionNumberSearch);
        model.addAttribute("message", manualProduceConfirmationScannerMessage);
        model.addAttribute("finishProductNumber",workDetailsRepository.finishProductNumber(productionNumberSearch.toString(),warehouse));
        WorkDetailsRepository.WorkToDoFoundProduction workToDoFoundProduction = workDetailsRepository.workToDoFoundProduction(productionNumberSearch,warehouse,SecurityUtils.username());
        model.addAttribute("workToDoFound",workToDoFoundProduction);
        model.addAttribute("messagePutaway", produceScannerMessage);

        List<WorkDetails> workDetailsList = workDetailsRepository.getWorkDetailsByWorkNumber(Long.parseLong(workToDoFoundProduction.getHandle()));
        model.addAttribute("workDetailsList",workDetailsList);
        return "wmsOperations/scanner/production/produce/scannerProduceConfirmationManualWork";
    }

    @PostMapping("produceMenuManualWorkProductionNumberFound")
    public String produceMenuManualWorkReceptionNumberFoundPost(@SessionAttribute String scannerChosenWarehouse,String token,
                                                                  @RequestParam Boolean produceConfirmation, HttpSession session,@SessionAttribute int scannerMenuChoice,@RequestParam Long productionNumberToConfirm,
                                                                  @SessionAttribute String scannerChosenEquipment, @SessionAttribute int workProductionScannerChoice,
                                                                  @SessionAttribute Long productionNumberSearch) throws CloneNotSupportedException {
        if(produceConfirmation){
            workDetailsService.createPutAwayWork(productionNumberToConfirm,session);
            if(session.getAttribute("putawayLocationAfterProducing").equals("found")) {
                workDetailsService.closeWorkDetail(productionNumberToConfirm,scannerChosenWarehouse);
                stockService.produceGoods(productionNumberToConfirm);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice;
            }
            else{
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch ;
            }
        }
        else{
            session.setAttribute("manualProduceConfirmationScannerMessage","If content under this message is correct and to finish production with creation putaway works push the button Finish Production");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + workProductionScannerChoice + '/' + productionNumberSearch ;
        }
    }
}
