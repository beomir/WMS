package pl.coderslab.cls_wms_app.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.*;
import pl.coderslab.cls_wms_app.service.storage.ArticleService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsSettings.ExtremelyService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.VendorService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

@Slf4j
@Controller
@RequestMapping("/reception")
public class ReceptionController {

    private final ReceptionService receptionService;
    private final WarehouseService warehouseService;
    private final ArticleService articleService;
    private final VendorService vendorService;
    private final CompanyService companyService;
    private final UnitService unitService;
    private final UsersService usersService;
    private final ReceptionServiceImpl receptionServiceImpl;
    private final ReceptionRepository receptionRepository;
    private final LocationRepository locationRepository;
    private final StatusRepository statusRepository;
    private final WarehouseRepository warehouseRepository;
    private final ExtremelyService extremelyService;
    private CustomerUserDetailsService customerUserDetailsService;


    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService, CompanyService companyService, UnitService unitService, UsersService usersService, ReceptionServiceImpl receptionServiceImpl, ReceptionRepository receptionRepository, LocationRepository locationRepository, StatusRepository statusRepository, WarehouseRepository warehouseRepository, ExtremelyService extremelyService, CustomerUserDetailsService customerUserDetailsService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.unitService = unitService;
        this.usersService = usersService;
        this.receptionServiceImpl = receptionServiceImpl;
        this.receptionRepository = receptionRepository;
        this.locationRepository = locationRepository;
        this.statusRepository = statusRepository;
        this.warehouseRepository = warehouseRepository;
        this.extremelyService = extremelyService;
        this.customerUserDetailsService = customerUserDetailsService;

    }

    @GetMapping("receptions-browser")
    public String browser(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        model.addAttribute("vendors", vendors);
        List<Status> status = statusRepository.getStatusesByProcess("Reception");
        model.addAttribute("status", status);

        usersService.loggedUserData(model, session);
        return "wmsOperations/receptions-browser";
    }

    @PostMapping("receptions-browser")
    public String findReceptions(HttpSession session,String receptionCreatedBy,String receptionWarehouse,
                                 String receptionCompany,String receptionVendor, String receptionReceptionNumber,String receptionHdNumber,String receptionStatus,
                                 String receptionLocation, String receptionCreatedFrom, String receptionCreatedTo) {
        session.setAttribute("receptionWarehouse", receptionWarehouse);
        session.setAttribute("receptionCreatedBy", receptionCreatedBy);
        session.setAttribute("receptionVendor", receptionVendor);
        session.setAttribute("receptionCompany", receptionCompany);
        session.setAttribute("receptionReceptionNumber", receptionReceptionNumber);
        session.setAttribute("receptionHdNumber", receptionHdNumber);
        session.setAttribute("receptionStatus", receptionStatus);
        session.setAttribute("receptionLocation", receptionLocation);
        session.setAttribute("receptionCreatedFrom", receptionCreatedFrom);
        session.setAttribute("receptionCreatedTo", receptionCreatedTo);

        log.info("Post createdBy: " + receptionCreatedBy);
        log.info("Post warehouse: " + receptionWarehouse );
        log.info("Post company: " + receptionCompany );
        log.info("Post vendor: " + receptionVendor);
        log.info("Post receptionNumber: " + receptionReceptionNumber);
        log.info("Post hdNumber: " + receptionHdNumber);
        log.info("Post status: " + receptionStatus);
        log.info("Post location: " + receptionLocation);
        log.info("Post createdFrom: " + receptionCreatedFrom);
        log.info("Post createdTo: " + receptionCreatedTo);
        return "redirect:/reception/reception";
    }

    @GetMapping("reception")
    public String list(Model model,@SessionAttribute(required = false) String chosenWarehouse,@SessionAttribute(required = false) String receptionCreatedBy,
                       @SessionAttribute(required = false) String receptionWarehouse,@SessionAttribute(required = false) String receptionCompany,
                       @SessionAttribute(required = false) String receptionVendor,@SessionAttribute(required = false) String receptionReceptionNumber
            ,@SessionAttribute(required = false) String receptionHdNumber,@SessionAttribute(required = false) String receptionStatus,
                       @SessionAttribute(required = false) String receptionLocation,@SessionAttribute(required = false) String receptionCreatedFrom
            ,@SessionAttribute(required = false) String receptionCreatedTo,@SessionAttribute(required = false) String receptionMessage,HttpSession session) {

        String warehouseName = receptionWarehouse;
        if(warehouseName==null){
            warehouseName = chosenWarehouse;
        }


        List<ReceptionRepository.ReceptionViewObject> receptions = receptionService.receptionSummary(receptionCompany,warehouseName,receptionVendor,receptionStatus,receptionLocation,receptionReceptionNumber,receptionHdNumber,receptionCreatedFrom,receptionCreatedTo,receptionCreatedBy);
        model.addAttribute("receptions", receptions);

        model.addAttribute("fileStatus", receptionServiceImpl.insertReceptionFileResult);
        model.addAttribute("warehouseName", warehouseName);
        model.addAttribute("receptionMessage", receptionMessage);

        String token = usersService.FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());

        model.addAttribute("receptionCreatedBy",receptionCreatedBy);
        model.addAttribute("receptionCompany",receptionCompany);
        model.addAttribute("receptionVendor",receptionVendor);
        model.addAttribute("receptionReceptionNumber",receptionReceptionNumber);
        model.addAttribute("receptionHdNumber",receptionHdNumber);
        model.addAttribute("receptionStatus",receptionStatus);
        model.addAttribute("receptionLocation",receptionLocation);
        model.addAttribute("receptionCreatedFrom",receptionCreatedFrom);
        model.addAttribute("receptionCreatedTo",receptionCreatedTo);
        model.addAttribute("receptionWarehouse",receptionWarehouse);


        usersService.loggedUserData(model,session);

        log.error("receptionMessage: " + receptionMessage);
        return "wmsOperations/reception";
    }

    @GetMapping("receptionDetails/{receptionNumber}")
    public String list(@PathVariable Long receptionNumber,Model model,@SessionAttribute(required = false) String receptionMessage,@SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request,HttpSession session) {
        log.error("receptionMessage: " + receptionMessage);
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")){
            List<Reception> reception = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
            model.addAttribute("reception", reception);
            model.addAttribute("receptionHeader", receptionNumber);
            model.addAttribute("statusHeader", receptionRepository.getStatusByReceptionNumber(receptionNumber));

            model.addAttribute("receptionMessage", receptionMessage);
            String userName = "";
            if(SecurityUtils.username().equals("%")){
                userName = "admin";
            }
            else {
                userName = SecurityUtils.username();
            }
            String token = usersService.FindUsernameByToken(userName);
            model.addAttribute("token", token);
            model.addAttribute("localDateTime", LocalDateTime.now());

            List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
            model.addAttribute("companies", companies);
            return "wmsOperations/receptionDetails";
        }

        else{
            return "redirect:/selectWarehouse";
        }
    }

    @PostMapping("receptionFile")
    public String receptionFile(@RequestParam("receptionFile") MultipartFile file)
    {
        if(!file.isEmpty()) {
            try {
                String filePath = "input/receptions/";
                String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                byte[] bytes = file.getBytes();
                File fsFile = new File(filePath + fileName);
                while(fsFile.exists()){
                    int random = new Random().nextInt(100);
                    fsFile = new File(filePath + random + fileName);
                }
                fsFile.createNewFile();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(fsFile));
                stream.write(bytes);
                stream.close();

                log.info("File {} has been successfully uploaded as {}", file.getOriginalFilename(), fileName);
                receptionService.insertFileContentToDB(fsFile);
            } catch (Exception e) {
                log.error("File has not been uploaded", e);
            }
        }
            else {
                log.error("Uploaded file is empty");
            }

        return "redirect:/reception/reception";
    }


    @GetMapping("formReception")
    public String receptionForm(Model model, @SessionAttribute(required = false) String searchingWarehouse,@SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request,HttpSession session){
        String warehouseName = searchingWarehouse;
        if(searchingWarehouse==null){
            warehouseName = chosenWarehouse;
        }
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")) {
            List<Article> articles = articleService.getArticle(SecurityUtils.username());
            List<Integer> pallets = receptionService.pallets();
            List<Unit> units = unitService.getUnit();
            List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
            List<Warehouse> warehouses = warehouseService.getWarehouse();
            Warehouse warehouse = warehouseService.getWarehouseByName(warehouseName);
            model.addAttribute("lastReceptionNumber", receptionService.lastReception());
            model.addAttribute("nextPalletNbr", extremelyService.nextPalletNbr());
            model.addAttribute("reception", new Reception());
            model.addAttribute("articles", articles);
            model.addAttribute("vendors", vendors);
            model.addAttribute("warehouse", warehouse);
            model.addAttribute("warehouses", warehouses);
            model.addAttribute("units", units);
            model.addAttribute("pallets", pallets);
            model.addAttribute("searchingWarehouse", warehouseName);
            List<Company> activeCompany = companyService.getCompany();
            model.addAttribute("activeCompany", activeCompany);
            log.error("searchingWarehouse: " + warehouseName);
            usersService.loggedUserData(model,session);
            return "wmsOperations/formReception";
        }
        else{
            return "redirect:/selectWarehouse";
        }


    }

    @PostMapping("formReception")
    public String receptionAdd(Reception reception,HttpSession session) {
        receptionService.addNew(reception,session);
        return "redirect:/reception/reception";
    }



    @GetMapping("/openCreationReception/{id}")
    public String openCreationReception(@PathVariable Long id,HttpSession session) {
        receptionService.openCreation(id,session);
        return "redirect:/reception/reception";
    }


    @GetMapping("/unloadingDoor/{receptionNumber}")
    public String closeCreationAndStartUnloading(@PathVariable Long receptionNumber,
                                                 Model model,
                                                 @SessionAttribute(required = false) String chosenWarehouse,
                                                 HttpServletRequest request,HttpSession session) {
        if(usersService.warehouseSelection(session,chosenWarehouse,request).equals("warehouseSelected")) {
            receptionService.closeCreation(receptionNumber, session);
            List<Location> receptionDoorLocations = locationRepository.receptionDoorLocations(warehouseRepository.getWarehouseByName(chosenWarehouse).getId());
            model.addAttribute("locations", receptionDoorLocations);
            model.addAttribute(receptionNumber);
            usersService.loggedUserData(model,session);
            return "wmsOperations/unloadingDoor";
        }
        else{
            return "redirect:/selectWarehouse";
        }
    }

    @PostMapping("unloadingDoor")
    public String closeCreationAndStartUnloadingPost(Long receptionNumber, Long doorLocation,HttpSession session) {
        receptionService.assignDoorLocationToReception(receptionNumber,doorLocation, session);
        return "redirect:/reception/reception";
    }

    @GetMapping("/finishUnloadingReception/{receptionNumber}")
    public String finishUnloading(@PathVariable Long receptionNumber, HttpSession session) {
        receptionService.finishUnloading(receptionNumber,session);
        return "redirect:/reception/reception";
    }

    @GetMapping("/finishedReception/{receptionNumber}")
    public String finishedReception(@PathVariable Long receptionNumber,HttpSession session) {
        receptionService.finishReception(receptionNumber,session);
        return "redirect:/reception/reception";
    }

    //edit

    @GetMapping("/editReception/{receptionNumber}")
    public String updateReception(@PathVariable Long receptionNumber, Model model,HttpSession session) {
        Reception reception = receptionRepository.getOneReceptionByReceptionNumber(receptionNumber);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        Warehouse warehouse = warehouseService.getWarehouseByReceptionNumber(receptionNumber);
        model.addAttribute(reception);
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model,session);
        return "wmsOperations/editReception";
    }

    @PostMapping("editReception")
    public String updateReceptionPost(Reception reception,HttpSession session) {
        receptionService.edit(reception,session);
        return "redirect:/reception/reception";
    }

    @GetMapping("/editReceptionLine/{id}")
    public String updateReceptionLine(@PathVariable Long id, Model model,HttpSession session) {
        Reception reception = receptionService.findById(id);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        Warehouse warehouse = warehouseService.getWarehouseByReceptionNumber(reception.getReceptionNumber());
        model.addAttribute(reception);
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        model.addAttribute("localDateTime", LocalDateTime.now());

        usersService.loggedUserData(model,session);
        return "wmsOperations/editReceptionLine";
    }

    @PostMapping("editReceptionLine")
    public String updateReceptionLinePost(Reception reception,HttpSession session) {
        receptionService.edit(reception,session);
        return "redirect:/reception/receptionDetails/" + reception.getReceptionNumber();
    }

    @GetMapping("formReceptionLine/{receptionNumber}")
    public String formReceptionLine(@PathVariable Long receptionNumber,Model model,HttpSession session){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        Warehouse warehouse = warehouseService.getWarehouseByReceptionNumber(receptionNumber);
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouse", warehouse);
        model.addAttribute("units", units);
        model.addAttribute("receptionHeader", receptionNumber);
        List<Company> activeCompany = companyService.getCompany();
        model.addAttribute("activeCompany", activeCompany);
        log.error("WarehouseName: " + warehouse.getName());
        usersService.loggedUserData(model,session);
        return "wmsOperations/formReceptionLine";

    }

    @PostMapping("formReceptionLine")
    public String formReceptionLinePost(Reception reception,HttpSession session) {
        receptionService.addNewReceptionLine(reception,session);
        log.error("detail" + reception.getReceptionNumber());
        return "redirect:/reception/receptionDetails/" + reception.getReceptionNumber();
    }


    @GetMapping("/deleteReceptionLine/{id}")
    public String deleteReceptionLine(@PathVariable Long id) {
        receptionRepository.delete(receptionRepository.getOne(id));
        return "redirect:/reception/reception";
    }




}
