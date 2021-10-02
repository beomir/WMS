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
public class ScannerStockTransferController {

    private final WorkDetailsService workDetailsService;
    private final StockService stockService;
    private final CompanyService companyService;
    private final WorkDetailsRepository workDetailsRepository;
    private final LocationRepository locationRepository;
    private final UsersService usersService;


    @Autowired
    public ScannerStockTransferController(WorkDetailsService workDetailsService, StockService stockService, CompanyService companyService, WorkDetailsRepository workDetailsRepository, LocationRepository locationRepository, UsersService usersService) {
        this.workDetailsService = workDetailsService;
        this.stockService = stockService;
        this.companyService = companyService;
        this.workDetailsRepository = workDetailsRepository;
        this.locationRepository = locationRepository;
        this.usersService = usersService;
    }

    //Selection Transfer Work
    @GetMapping("{token}/{warehouse}/{equipment}/2/1")
    public String stockTransfer(@PathVariable String warehouse,
                                @PathVariable String token,
                                @PathVariable String equipment,
                                Model model,
                                @SessionAttribute(required = false) String stockTransferScannerMessage,
                                HttpSession session,
                                HttpServletRequest request) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", stockTransferScannerMessage);
        model.addAttribute("warehouse", warehouse);

        usersService.nextURL(request,session);

        List<WorkDetailsRepository.WorkHeaderListProduction> workDetailsQueue = workDetailsRepository.workHeaderListProduction(warehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(), "%", "Transfer", "%", "%", SecurityUtils.username(), "%", "%", "%", "Stock transfer Work");
        model.addAttribute("workDetailsQueue", workDetailsQueue);

        return "wmsOperations/scanner/transfer/stock/scannerStockTransfer";
    }
    @PostMapping("scannerStockTransfer")
    public String stockTransferPost(@SessionAttribute String scannerChosenWarehouse,
                                    String token,
                                    Long stockTransferWorkNumber,
                                    HttpSession session,
                                    @SessionAttribute int scannerMenuChoice,
                                    @SessionAttribute String scannerChosenEquipment,
                                    @SessionAttribute int scannerStock,
                                    String automaticallyFinder) {
        log.error("automaticallyFinder value: " + automaticallyFinder);
        //situation when user force front end without attributes
        if (automaticallyFinder == null && stockTransferWorkNumber == null) {
            session.setAttribute("stockTransferScannerMessage", "Please enter work number manually or push button check to find work automatically by system ");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock;
        }
        else{
            //selected automatic search
            if(automaticallyFinder != null){
                log.error("workNumberBrCompanyAndWarehouse: " + workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Stock transfer Work",SecurityUtils.username()));
                if(workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Stock transfer Work",SecurityUtils.username()) == null){
                    session.setAttribute("stockTransferScannerMessage", "No works found");
                    return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock;
                }
                else{
                    //work for reception found
                    stockTransferWorkNumber = workDetailsRepository.workNumberByCompanyWarehouseWorkDescriptionStatusUser(companyService.getOneCompanyByUsername(SecurityUtils.username()).getName(),scannerChosenWarehouse,"Stock transfer Work",SecurityUtils.username());
                    session.setAttribute("stockTransferWorkNumber", stockTransferWorkNumber);
                }
            }
            //when on equipment is not enough space for pick goods
            WorkDetailsRepository.MaxVolumeAndWeightForWorkByWorkNumber maxVolumeAndWeightForWork = workDetailsRepository.maxVolumeAndWeightForWorkByWorkNumber(stockTransferWorkNumber.toString());
            Location equipment = locationRepository.findLocationByLocationName(scannerChosenEquipment,scannerChosenWarehouse);
            log.error("maxVolumeAndWeightForWork.getHandle(): " + maxVolumeAndWeightForWork.getHandle());
            log.error("equipment.getFreeWeight(): " + equipment.getFreeWeight());
            log.error("maxVolumeAndWeightForWork.getPiecesQty(): " + maxVolumeAndWeightForWork.getPiecesQty());
            log.error("equipment.getFreeSpace(): " + equipment.getFreeSpace());
            if(maxVolumeAndWeightForWork.getHandle()>equipment.getFreeWeight() || maxVolumeAndWeightForWork.getPiecesQty()>equipment.getFreeSpace()){
                log.error("OverloadedPath");
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + stockTransferWorkNumber + "/equipmentOverloaded" ;
            }
            //work for reception found logic
            if(workDetailsService.stockTransferWorkSearch(session,stockTransferWorkNumber,scannerChosenWarehouse)){
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber;
            }
            else{
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock;
            }
        }

    }

    //scan locationFrom
    @GetMapping("{token}/{warehouse}/{equipment}/2/1/{workNumber}")
    public String stockTransferWorkNumberFound(@PathVariable String warehouse, @PathVariable String token,
                                                             @PathVariable String equipment, Model model, @SessionAttribute Long stockTransferWorkNumber,
                                                             @SessionAttribute(required = false) String stockTransferScannerMessageLocationFrom) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("workNumber", stockTransferWorkNumber);
        model.addAttribute("message", stockTransferScannerMessageLocationFrom);

        WorkDetailsRepository.WorkToDoFoundByWorkNumber workToDoFoundByWorkNumber = workDetailsRepository.workToDoFoundByWorkNumber(stockTransferWorkNumber, warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFoundByWorkNumber);
        return "wmsOperations/scanner/transfer/stock/scannerStockTransferFoundOriginLocation";
    }

    @PostMapping("stockTransferScanLocationFrom")
    public String stockTransferWorkNumberFoundPost(@SessionAttribute String scannerChosenWarehouse, String token, @RequestParam String stockTransferScannerFromLocation,
                                                                 @RequestParam String stockTransferOriginLocation, HttpSession session, @SessionAttribute int scannerMenuChoice,
                                                                 @SessionAttribute String scannerChosenEquipment, @SessionAttribute int scannerStock,
                                                                 @SessionAttribute Long stockTransferWorkNumber) {
        log.error("Location found by query: " + stockTransferScannerFromLocation);
        log.error("Location enter by user: " + stockTransferOriginLocation);
        if (stockTransferScannerFromLocation.equals(stockTransferOriginLocation)) {
            session.setAttribute("stockTransferScannerFromLocation", stockTransferScannerFromLocation);
            String nextPath = "hdNumber";
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + nextPath;
        } else {
            session.setAttribute("stockTransferScannerMessageLocationFrom", "Location from where you want pick up: " + stockTransferOriginLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber;
        }
    }

    //scan HdNumber
    @GetMapping("{token}/{warehouse}/{equipment}/2/1/{workNumber}/hdNumber")
    public String stockTransferHDScan(@PathVariable String warehouse, @PathVariable String token,
                                                                     @PathVariable String equipment, Model model,
                                                                     @SessionAttribute Long stockTransferWorkNumber,
                                                                     @SessionAttribute(required = false) String stockTransferScannerHdNumberMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
//        model.addAttribute("workNumber", stockTransferWorkNumber);
        model.addAttribute("message", stockTransferScannerHdNumberMessage);

        WorkDetailsRepository.WorkToDoFoundByWorkNumber workToDoFoundByWorkNumber = workDetailsRepository.workToDoFoundByWorkNumber(stockTransferWorkNumber, warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFoundByWorkNumber);
        return "wmsOperations/scanner/transfer/stock/scannerStockTransferFoundHdNumber";
    }

    @PostMapping("stockTransferHDScan")
    public String stockTransferHDFoundPost(@SessionAttribute String scannerChosenWarehouse,
                                                                         String token, @RequestParam String stockTransferScannerExpectedHdNumber,
                                                                         @RequestParam String stockTransferScannerEnteredHdNumber,
                                                                         HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                         @SessionAttribute int scannerStock, @SessionAttribute Long stockTransferWorkNumber) {
        log.error("HdNumber found by query: " + stockTransferScannerExpectedHdNumber);
        log.error("HdNumber enter by user: " + stockTransferScannerEnteredHdNumber);
        String nextPath = "article";
        String previous = "hdNumber";
        if (stockTransferScannerExpectedHdNumber.equals(stockTransferScannerEnteredHdNumber)) {
            session.setAttribute("stockTransferScannerEnteredHdNumber", stockTransferScannerEnteredHdNumber);
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + previous + '/' + nextPath;
        } else {
            session.setAttribute("stockTransferScannerHdNumberMessage", "Entered HdNumber: " + stockTransferScannerEnteredHdNumber + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + previous;
        }
    }

    //scan Article
    @GetMapping("{token}/{warehouse}/{equipment}/2/1/{workNumber}/hdNumber/article")
    public String stockTransferArticleScan(@PathVariable String warehouse, @PathVariable String token, @PathVariable String equipment,
                                                                    Model model, @SessionAttribute Long stockTransferWorkNumber,
                                                                    @SessionAttribute(required = false) String stockTransferScannerArticleMessage,HttpSession session) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("workNumber", stockTransferWorkNumber);
        model.addAttribute("message", stockTransferScannerArticleMessage);

        WorkDetailsRepository.WorkToDoFoundByWorkNumber workToDoFoundByWorkNumber = workDetailsRepository.workToDoFoundByWorkNumber(stockTransferWorkNumber, warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFoundByWorkNumber);

        session.setAttribute("stockTransferPiecesQty",workToDoFoundByWorkNumber.getPiecesQty());
        return "wmsOperations/scanner/transfer/stock/scannerStockTransferFoundArticle";
    }

    @PostMapping("stockTransferArticleScan")
    public String stockTransferArticleFoundPost(@SessionAttribute String scannerChosenWarehouse,
                                                                        String token, @RequestParam String stockTransferScannerExpectedArticle, @RequestParam String stockTransferScannerEnteredArticle,
                                                                        HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                        @SessionAttribute int scannerStock, @SessionAttribute Long stockTransferWorkNumber,
                                                                        @SessionAttribute String stockTransferScannerEnteredHdNumber,
                                                                        @SessionAttribute String stockTransferScannerFromLocation,
                                                                        @SessionAttribute Long stockTransferPiecesQty) throws CloneNotSupportedException {
        log.error("Article found by query: " + stockTransferScannerExpectedArticle);
        log.error("Article enter by user: " + stockTransferScannerEnteredArticle);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if (stockTransferScannerExpectedArticle.equals(stockTransferScannerEnteredArticle)) {
            session.setAttribute("stockTransferScannerEnteredArticle", stockTransferScannerEnteredArticle);
            workDetailsService.pickUpGoods(stockTransferScannerFromLocation, stockTransferScannerEnteredArticle, stockTransferScannerEnteredHdNumber, scannerChosenEquipment, scannerChosenWarehouse, companyService.getOneCompanyByUsername(SecurityUtils.usernameForActivations()).getName(),stockTransferWorkNumber.toString(),stockTransferPiecesQty,"StockTransfer");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + prevprevious + '/' + previous + '/' + nextPath;

        } else {
            session.setAttribute("stockTransferScannerArticleMessage", "Entered article: " + stockTransferScannerEnteredArticle + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + prevprevious + '/' + previous;
        }
    }

    //scan destinationLocation
    @GetMapping("{token}/{warehouse}/{equipment}/2/1/{workNumber}/hdNumber/article/toLocation")
    public String stockTransferDestinationLocationScan(@PathVariable String warehouse,
                                                                                @PathVariable String token,
                                                                                @PathVariable String equipment, Model model,
                                                                                @SessionAttribute Long stockTransferWorkNumber,
                                                                                @SessionAttribute(required = false) String stockTransferScannerLocationToMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("workNumber", stockTransferWorkNumber);
        model.addAttribute("message", stockTransferScannerLocationToMessage);

        WorkDetailsRepository.WorkToDoFoundByWorkNumber workToDoFoundByWorkNumber = workDetailsRepository.workToDoFoundByWorkNumber(stockTransferWorkNumber, warehouse, SecurityUtils.username());
        model.addAttribute("workToDoFound", workToDoFoundByWorkNumber);
        return "wmsOperations/scanner/transfer/stock/scannerStockTransferFoundDestinationLocation";
    }

    @PostMapping("stockTransferDestinationLocationScan")
    public String stockTransferDestinationLocationScanPost(@SessionAttribute String scannerChosenWarehouse,
                                                                                    String token, @RequestParam String stockTransferScannerExpectedDestinationLocation, @RequestParam String stockTransferEnteredDestinationLocation,
                                                                                    HttpSession session, @SessionAttribute int scannerMenuChoice, @SessionAttribute String scannerChosenEquipment,
                                                                                    @SessionAttribute int scannerStock, @SessionAttribute Long stockTransferWorkNumber,
                                                                                    @SessionAttribute String stockTransferScannerEnteredHdNumber, @SessionAttribute Long stockTransferScannerEnteredArticle) {
        log.error("Destination location found by query: " + stockTransferScannerExpectedDestinationLocation);
        log.error("Destination location enter by user: " + stockTransferEnteredDestinationLocation);
        String nextPath = "toLocation";
        String previous = "article";
        String prevprevious = "hdNumber";
        if (stockTransferScannerExpectedDestinationLocation.equals(stockTransferEnteredDestinationLocation)) {
            session.setAttribute("stockTransferEnteredDestinationLocation", stockTransferEnteredDestinationLocation);
            log.error("Long.parseLong(stockTransferScannerEnteredHdNumber): " + Long.parseLong(stockTransferScannerEnteredHdNumber));
            log.error("scannerChosenWarehouse: " + scannerChosenWarehouse);
            log.error("stockTransferWorkNumber: " + stockTransferWorkNumber);
            log.error("stockTransferScannerEnteredArticle: " + stockTransferScannerEnteredArticle);

            WorkDetails workDetails = workDetailsRepository.workLineFinishByWorkNumber(Long.parseLong(stockTransferScannerEnteredHdNumber), scannerChosenWarehouse, stockTransferWorkNumber, stockTransferScannerEnteredArticle);
            log.error("workDetails: " + workDetails);
            workDetailsService.workLineFinish(workDetails, scannerChosenEquipment);
            if (workDetailsRepository.checkIfWorksExistsForHandleWithStatusUserProduction(stockTransferWorkNumber.toString(), scannerChosenWarehouse, SecurityUtils.username(), "Stock transfer Work") == 0) {
                workDetailsService.workFinished(workDetails, session);
                return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + '2';
            }
            session.setAttribute("stockTransferScannerLocationToMessage", "Work for: " + workDetails.getHdNumber() + " finished, start next one from this transfer");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber;

        } else {
            session.setAttribute("stockTransferScannerLocationToMessage", "Entered destination location: " + stockTransferEnteredDestinationLocation + " is incorrect");
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + stockTransferWorkNumber + '/' + prevprevious + '/' + previous + '/' + nextPath;
        }
    }



}
