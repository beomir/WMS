package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.repository.LocationRepository;
import pl.coderslab.cls_wms_app.repository.StockRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.ScannerService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

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


    @Autowired
    public ScannerStockPreviewController(StockRepository stockRepository, ScannerService scannerService, CompanyService companyService, LocationRepository locationRepository) {
        this.stockRepository = stockRepository;
        this.scannerService = scannerService;
        this.companyService = companyService;
        this.locationRepository = locationRepository;
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
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        log.error("valueToPreview: " + valueToPreview);
        log.error("previewWay: " + session.getAttribute("previewWay"));
        if(session.getAttribute("previewWay").toString().equals("location")){
            List<Stock> stockPreviewList = scannerService.locationPreview(valueToPreview,company.getName(),warehouse);
            model.addAttribute("stockPreviewList",stockPreviewList);
            model.addAttribute("locationName", valueToPreview);
            model.addAttribute("previewWay","1");
        }
        else{
            List<Stock> stockPreviewList = stockRepository.getStockByHdNumberAndCompanyNameAndWarehouseName(Long.parseLong(valueToPreview),company.getName(),warehouse);
            model.addAttribute("stockPreviewList",stockPreviewList);
            model.addAttribute("hdNumber", valueToPreview);
            model.addAttribute("previewWay","2");
        }
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
        model.addAttribute("token", token);
        model.addAttribute("equipment", equipment);
        model.addAttribute("warehouse", warehouse);
        log.error("valueToPreview: " + valueToPreview);
        log.error("previewWay: " + session.getAttribute("previewWay"));
        List<Stock> stockPreviewList = stockRepository.getStockByHdNumberAndCompanyNameAndWarehouseName(hdNumber,company.getName(),warehouse);
        model.addAttribute("stockPreviewList",stockPreviewList);
        model.addAttribute("locationName", valueToPreview);
        model.addAttribute("previewWay","1");

        return "wmsOperations/scanner/preview/stock/scannerStockPreviewLocationHDDetails";
    }

}
