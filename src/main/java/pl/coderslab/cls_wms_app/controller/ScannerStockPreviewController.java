package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.repository.ExtremelyRepository;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.ScannerService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/scanner")
@Slf4j
public class ScannerStockPreviewController {

    private final StockRepository stockRepository;
    private final ScannerService scannerService;
    private final CompanyService companyService;
    private final LocationRepository locationRepository;
    private final ExtremelyRepository extremelyRepository;


    @Autowired
    public ScannerStockPreviewController(StockRepository stockRepository, ScannerService scannerService, CompanyService companyService, LocationRepository locationRepository, ExtremelyRepository extremelyRepository) {
        this.stockRepository = stockRepository;
        this.scannerService = scannerService;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
        this.extremelyRepository = extremelyRepository;
    }

    //Selection Preview
    @GetMapping("{token}/{warehouse}/{equipment}/2/2")
    public String scannerStockPreview(@PathVariable String warehouse,
                                        @PathVariable String token,
                                        @PathVariable String equipment,
                                        Model model,
                                        @SessionAttribute(required = false) String stockPreviewMessage) {
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("message", stockPreviewMessage);
        model.addAttribute("warehouse", warehouse);

        return "wmsOperations/scanner/preview/stock/scannerStockPreview";
    }
    @PostMapping("scannerStockPreview")
    public String scannerStockPreviewPost(@SessionAttribute String scannerChosenWarehouse,
                                          String token,
                                          HttpSession session,
                                          @SessionAttribute int scannerMenuChoice,
                                          @SessionAttribute int scannerStock,
                                          String valueToPreview,
                                          @SessionAttribute String scannerChosenEquipment) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        String goPreviewURL = "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + valueToPreview;
        String stayAtTheSameURL = "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock;
        if(locationRepository.findLocationByLocationName(valueToPreview,scannerChosenWarehouse) != null){
            session.setAttribute("previewWay","location");
            return goPreviewURL;
        }
        else{
            try{
                Long hdNumber = Long.parseLong(valueToPreview);
                if(stockRepository.getStockByHdNumberAndCompanyNameAndWarehouseName(hdNumber,company.getName(),scannerChosenWarehouse) != null){
                    session.setAttribute("previewWay","hdNumber");
                    return goPreviewURL;
                }
                else{
                    session.setAttribute("stockPreviewMessage","Entered value is not a location: " + valueToPreview + " which exists for warehouse: " + scannerChosenWarehouse + " or hd number on stock of: " + company.getName() + " company");
                    return stayAtTheSameURL;
                }
            }
            catch(Exception e){
                session.setAttribute("stockPreviewMessage","Entered value is not a location: " + valueToPreview + " which exists for warehouse: " + scannerChosenWarehouse + " or hd number on stock of: " + company.getName() + " company");
                return stayAtTheSameURL;
            }

        }

    }

    //valueToPreview scanned
    @GetMapping("{token}/{warehouse}/{equipment}/2/2/{valueToPreview}")
    public String scannerStockPreviewLocationScanned(@PathVariable String warehouse,
                                                     @PathVariable String token,
                                                     @PathVariable String equipment, Model model,
                                                     @PathVariable String valueToPreview,HttpSession session) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company", company);

        Extremely extremely = extremelyRepository.findExtremelyByCompanyNameAndExtremelyName(company.getName(),"Scan_SP_loc_num");
        model.addAttribute("ScanSPLocNum",extremely);

        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        log.error("valueToPreview: " + valueToPreview);
        log.error("previewWay: " + session.getAttribute("previewWay"));
        if(session.getAttribute("previewWay").toString().equals("location")){
            List<Stock> stockPreviewList = scannerService.locationPreview(valueToPreview,company.getName(),warehouse);
            model.addAttribute("stockPreviewList",stockPreviewList);
            model.addAttribute("valueToPreview", valueToPreview);
            model.addAttribute("previewWay","1");
        }
        else{
            List<Stock> stockPreviewList = stockRepository.getStockByHdNumberAndCompanyNameAndWarehouseName(Long.parseLong(valueToPreview),company.getName(),warehouse);
            model.addAttribute("stockPreviewList",stockPreviewList);
            model.addAttribute("valueToPreview", valueToPreview);
            model.addAttribute("previewWay","2");
        }
        model.addAttribute("locationsForArticleFound",session.getAttribute("locationsForArticleFound"));
        return "wmsOperations/scanner/preview/stock/scannerStockPreviewLocationOrHDScanned";
    }

    //valueToPreview scanned details
    @GetMapping("{token}/{warehouse}/{equipment}/2/2/{valueToPreview}/{hdNumber}")
    public String scannerStockPreviewLocationScannedHdDetails(@PathVariable String warehouse,
                                                              @PathVariable String token,
                                                              @PathVariable String equipment, Model model,
                                                              @PathVariable String valueToPreview,
                                                              @PathVariable Long hdNumber,
                                                              HttpSession session) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company", company);
        Extremely extremely = extremelyRepository.findExtremelyByCompanyNameAndExtremelyName(company.getName(),"Scan_SP_loc_num");
        model.addAttribute("ScanSPLocNum",extremely);
        model.addAttribute("locationsForArticleFound",session.getAttribute("locationsForArticleFound"));

        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("hdNumber", hdNumber);

        log.error("valueToPreview: " + valueToPreview);
        log.error("previewWay: " + session.getAttribute("previewWay"));
        List<Stock> stockPreviewList = stockRepository.getStockByHdNumberAndCompanyNameAndWarehouseName(hdNumber,company.getName(),warehouse);
        model.addAttribute("stockPreviewList",stockPreviewList);

        model.addAttribute("valueToPreview", valueToPreview);
        model.addAttribute("previewWay","1");

        return "wmsOperations/scanner/preview/stock/scannerStockPreviewLocationHDDetails";
    }

    @PostMapping("findArticleInNearbyLocations")
    public String scannerStockPreviewFindLocationsWithArticle(@SessionAttribute String scannerChosenWarehouse,
                                                              String token,
                                                              HttpSession session,
                                                              @SessionAttribute int scannerMenuChoice,
                                                              @SessionAttribute int scannerStock,
                                                              String placeForLocationName,
                                                              String inHowManyLocations,
                                                              String articleNumberInLocations,
                                                              @SessionAttribute String scannerChosenEquipment,
                                                              String valueToPreview) {
        log.error("articleNumberInLocations: " + articleNumberInLocations);
        log.error("placeForLocationName: " + placeForLocationName);
        log.error("inHowManyLocations: " + inHowManyLocations);
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        if(scannerService.locationsForArticleFound(placeForLocationName,articleNumberInLocations,inHowManyLocations,session,company)){
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + placeForLocationName + '/' + articleNumberInLocations + '/' + inHowManyLocations;
        }
        else{
            return "redirect:/scanner/" + token + '/' + scannerChosenWarehouse + '/' + scannerChosenEquipment + '/' + scannerMenuChoice + '/' + scannerStock + '/' + valueToPreview;
        }
    }

    @GetMapping("{token}/{warehouse}/{equipment}/2/2/{locationName}/{articleNumberInLocations}/{inHowManyLocations}")
    public String scannerStockPreviewArticleInNearbyLocations(@PathVariable String warehouse,
                                                              @PathVariable String token,
                                                              @PathVariable String equipment, Model model,
                                                              @PathVariable String locationName,
                                                              @PathVariable String articleNumberInLocations,
                                                              @PathVariable String inHowManyLocations,
                                                              HttpSession session,
                                                              HttpServletRequest request) {
        Company company = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("company", company);
        model.addAttribute("backUrl",request.getHeader("Referer"));
        model.addAttribute("articleNumberInLocations",articleNumberInLocations);

        model.addAttribute("inHowManyLocations",inHowManyLocations);

        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("locationName", locationName);


        List<Stock> stockPreviewList = scannerService.findArticleInNearbyLocations(locationName,articleNumberInLocations,inHowManyLocations,session,company,warehouse);
        model.addAttribute("stockPreviewList",stockPreviewList);
        return "wmsOperations/scanner/preview/stock/scannerStockPreviewArticleInNearbyLocations";
    }

}
