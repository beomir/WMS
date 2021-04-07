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
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;
import pl.coderslab.cls_wms_app.service.wmsValues.VendorService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;
import pl.coderslab.cls_wms_app.temporaryObjects.CustomerUserDetailsService;
import pl.coderslab.cls_wms_app.temporaryObjects.ReceptionSearch;

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
    private CustomerUserDetailsService customerUserDetailsService;
    public ReceptionSearch receptionSearch;

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService, CompanyService companyService, UnitService unitService, UsersService usersService, ReceptionServiceImpl receptionServiceImpl, ReceptionRepository receptionRepository, LocationRepository locationRepository, StatusRepository statusRepository, CustomerUserDetailsService customerUserDetailsService,ReceptionSearch receptionSearch) {
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
        this.customerUserDetailsService = customerUserDetailsService;
        this.receptionSearch = receptionSearch;
    }


    @GetMapping("reception")
    public String list(Model model) {
        List<ReceptionRepository.ReceptionViewObject> receptions = receptionRepository.getReceptionSummary(receptionSearch.getCompany(),receptionSearch.getWarehouse(),receptionSearch.getVendor(),receptionSearch.getStatus(),receptionSearch.getLocation(),receptionSearch.getReceptionNumber(),receptionSearch.getHdNumber(),receptionSearch.getCreatedFrom(),receptionSearch.getCreatedTo(),receptionSearch.getCreatedBy());
        model.addAttribute("receptions", receptions);


        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("warehouse", warehouse);

        model.addAttribute("fileStatus", receptionServiceImpl.insertReceptionFileResult);
        model.addAttribute("receptionSearch", receptionSearch);

        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        String token = usersService.FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());
        log.error("reception get receptionSearch.message: " + receptionSearch.message);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/reception";
        }
    }

    @GetMapping("receptionDetails/{receptionNumber}")
    public String list(@PathVariable Long receptionNumber,Model model) {
        List<Reception> reception = receptionRepository.getReceptionByReceptionNumber(receptionNumber);
        model.addAttribute("reception", reception);
        model.addAttribute("receptionHeader", receptionNumber);
        model.addAttribute("statusHeader", receptionRepository.getStatusByReceptionNumber(receptionNumber));
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        String token = usersService.FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/receptionDetails";
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
    public String receptionForm(Model model){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Reception> openedReceptions = receptionService.openedReceptions(customerUserDetailsService.chosenWarehouse,SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        int qtyOfOpenedReceptions = receptionService.qtyOfOpenedReceptions(customerUserDetailsService.chosenWarehouse,SecurityUtils.username());
        model.addAttribute("lastReceptionNumber", receptionService.lastReception());
        model.addAttribute("nextPalletNbr", receptionService.nextPalletNbr());
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        model.addAttribute("qtyOfOpenedReceptions", qtyOfOpenedReceptions);
        model.addAttribute("openedReception", openedReceptions);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/formReception";
        }
    }

    @PostMapping("formReception")
    public String receptionAdd(Reception reception) {
        receptionService.addNew(reception);
        return "redirect:/reception/reception";
    }



    @GetMapping("/openCreationReception/{id}")
    public String openCreationReception(@PathVariable Long id) {
        receptionService.openCreation(id);
        return "redirect:/reception/reception";
    }


    @GetMapping("/unloadingDoor/{receptionNumber}")
    public String closeCreationAndStartUnloading(@PathVariable Long receptionNumber,Model model) {
        receptionService.closeCreation(receptionNumber);
        Long doorLocation = 0L;
        List<Location> receptionDoorLocations = locationRepository.receptionDoorLocations(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("locations", receptionDoorLocations);
        model.addAttribute(receptionNumber);
        model.addAttribute(doorLocation);
        usersService.loggedUserData(model);
        return "wmsOperations/unloadingDoor";
    }

    @PostMapping("unloadingDoor")
    public String closeCreationAndStartUnloadingPost(Long receptionNumber, Long doorLocation) {
        receptionService.assignDoorLocationToReception(receptionNumber,doorLocation);
        return "redirect:/reception/reception";
    }

    @GetMapping("/finishUnloadingReception/{receptionNumber}")
    public String finishUnloading(@PathVariable Long receptionNumber) {
        receptionService.finishUnloading(receptionNumber);
        return "redirect:/reception/reception";
    }

    @GetMapping("/finishedReception/{receptionNumber}")
    public String finishedReception(@PathVariable Long receptionNumber) {
        receptionService.insertDataToStockAfterFinishedReception(receptionNumber);
        receptionService.finishReception(receptionNumber);
        return "redirect:/reception/reception";
    }

    //edit

    @GetMapping("/editReception/{receptionNumber}")
    public String updateReception(@PathVariable Long receptionNumber, Model model,@SessionAttribute Long warehouseId) {
        Reception reception = receptionRepository.getOneReceptionByReceptionNumber(receptionNumber);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        model.addAttribute(reception);
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "wmsOperations/editReception";
    }

    @PostMapping("editReception")
    public String updateReceptionPost(Reception reception) {
        receptionService.edit(reception);
        return "redirect:/reception/reception";
    }

    @GetMapping("/editReceptionLine/{id}")
    public String updateReceptionLine(@PathVariable Long id, Model model,@SessionAttribute Long warehouseId) {
        Reception reception = receptionService.findById(id);
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Integer> pallets = receptionService.pallets();
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(warehouseId);
        model.addAttribute(reception);
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("pallets", pallets);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "wmsOperations/editReceptionLine";
    }

    @PostMapping("editReceptionLine")
    public String updateReceptionLinePost(Reception reception) {
        receptionService.edit(reception);
        return "redirect:/reception/receptionDetails/" + reception.getReceptionNumber();
    }

    @GetMapping("formReceptionLine/{receptionNumber}")
    public String formReceptionLine(@PathVariable Long receptionNumber,Model model){
        List<Article> articles = articleService.getArticle(SecurityUtils.username());
        List<Unit> units = unitService.getUnit();
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        List<Warehouse> warehouses = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("reception", new Reception());
        model.addAttribute("articles", articles);
        model.addAttribute("vendors", vendors);
        model.addAttribute("warehouses", warehouses);
        model.addAttribute("units", units);
        model.addAttribute("receptionHeader", receptionNumber);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "wmsOperations/formReceptionLine";
        }
    }

    @PostMapping("formReceptionLine")
    public String formReceptionLinePost(Reception reception) {
        receptionService.addNewReceptionLine(reception);
        log.error("detail" + reception.getReceptionNumber());
        return "redirect:/reception/receptionDetails/" + reception.getReceptionNumber();
    }


    @GetMapping("/deleteReceptionLine/{id}")
    public String deleteReceptionLine(@PathVariable Long id) {
        receptionRepository.delete(receptionRepository.getOne(id));
        return "redirect:/reception/reception";
    }

    @GetMapping("receptions-browser")
    public String browser(Model model) {
        model.addAttribute("receptionSearching", new ReceptionSearch());
        Company companys = companyService.getOneCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        List<Vendor> vendors = vendorService.getVendor(SecurityUtils.username());
        model.addAttribute("vendors", vendors);
        List<Status> status = statusRepository.getStatusesByProcess("Reception");
        model.addAttribute("status", status);
        return "wmsOperations/receptions-browser";
    }

    @PostMapping("receptions-browser")
    public String findReceptions(ReceptionSearch receptionSearching) {
        log.error("Post createdBy: " + receptionSearching.createdBy);
        log.error("Post warehouse: " + receptionSearching.warehouse);
        log.error("Post company: " + receptionSearching.company);
        log.error("Post vendor: " + receptionSearching.vendor);
        log.error("Post receptionNumber: " + receptionSearching.receptionNumber);
        log.error("Post hdNumber: " + receptionSearching.hdNumber);
        log.error("Post status: " + receptionSearching.status);
        log.error("Post location: " + receptionSearching.location);
        log.error("Post createdFrom: " + receptionSearching.createdFrom);
        log.error("Post createdTo: " + receptionSearching.createdTo);
        receptionService.save(receptionSearching);
        return "redirect:/reception/reception";
    }


}
