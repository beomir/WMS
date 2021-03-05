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
import pl.coderslab.cls_wms_app.service.*;

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
    private CustomerUserDetailsService customerUserDetailsService;

    @Autowired
    public ReceptionController(ReceptionService receptionService, WarehouseService warehouseService, ArticleService articleService, VendorService vendorService, CompanyService companyService, UnitService unitService, UsersService usersService, ReceptionServiceImpl receptionServiceImpl, CustomerUserDetailsService customerUserDetailsService) {
        this.receptionService = receptionService;
        this.warehouseService = warehouseService;
        this.articleService = articleService;
        this.vendorService = vendorService;
        this.companyService = companyService;
        this.unitService = unitService;
        this.usersService = usersService;
        this.receptionServiceImpl = receptionServiceImpl;
        this.customerUserDetailsService = customerUserDetailsService;
    }


    @GetMapping("reception")
    public String list(Model model) {
        List<Reception> receptions = receptionService.getReceptions(customerUserDetailsService.chosenWarehouse,SecurityUtils.username());
        List<Warehouse> warehouse = warehouseService.getWarehouse(customerUserDetailsService.chosenWarehouse);
        model.addAttribute("fileStatus", receptionServiceImpl.insertReceptionFileResult);
        model.addAttribute("receptions", receptions);
        model.addAttribute("warehouse", warehouse);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        if(customerUserDetailsService.chosenWarehouse == null){
            return "redirect:/warehouse";
        }
        else{
            return "reception";
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
            return "formReception";
        }
    }

    @PostMapping("formReception")
    public String receptionAdd(Reception reception) {
        receptionService.add(reception);
        receptionService.getCreatedReceptionById(reception.getReceptionNumber());
        receptionService.updateCloseCreationValue(reception.getReceptionNumber());
        return "redirect:/reception/reception";
    }

    @GetMapping("/finishedReception/{id}")
    public String finishedReception(@PathVariable Long id) {
        Long getReceptionById = receptionService.findById(id).getReceptionNumber();
        receptionService.updateFinishedReceptionValue(getReceptionById);
        receptionService.insertDataToStockAfterFinishedReception(getReceptionById);
        receptionService.finishReception(getReceptionById);
        return "redirect:/reception/reception";
    }

    @GetMapping("/closeCreationReception/{id}")
    public String closeCreationReception(@PathVariable Long id) {
        receptionService.closeCreation(id);
        return "redirect:/reception/reception";
    }

    @GetMapping("/openCreationReception/{id}")
    public String openCreationReception(@PathVariable Long id) {
        receptionService.openCreation(id);
        return "redirect:/reception/reception";
    }

    //edit

    @GetMapping("/editReceptionLine/{id}")
    public String updateReception(@PathVariable Long id, Model model,@SessionAttribute Long warehouseId) {
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
        return "editReceptionLine";
    }

    @PostMapping("editReceptionLine")
    public String updateReceptionPost(Reception reception) {
        receptionService.add(reception);
        receptionService.getCreatedReceptionById(reception.getReceptionNumber());
        receptionService.updateCloseCreationValue(reception.getReceptionNumber());
        return "redirect:/reception/reception";
    }

}
