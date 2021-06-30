package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.*;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsOperations.WorkDetailsService;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@Slf4j
public class WorkDetailsController {

    private final WorkDetailsService workDetailsService;
    private final WarehouseService warehouseService;
    private final CompanyService companyService;
    private final UsersService usersService;
    private final WorkDetailsRepository workDetailsRepository;


    @Autowired
    public WorkDetailsController(WorkDetailsService workDetailsService, WarehouseService warehouseService, CompanyService companyService, UsersService usersService, WorkDetailsRepository workDetailsRepository) {
        this.workDetailsService = workDetailsService;
        this.warehouseService = warehouseService;
        this.companyService = companyService;
        this.usersService = usersService;
        this.workDetailsRepository = workDetailsRepository;
    }

    @GetMapping("workDetails")
    public String list(Model model, HttpSession session, @SessionAttribute(required = false) String searchingWarehouse, HttpServletRequest request,
                       @SessionAttribute(required = false) String workDetailsWarehouse, @SessionAttribute(required = false) String workDetailsCompany,
                       @SessionAttribute(required = false) String workDetailsArticle, @SessionAttribute(required = false) String workDetailsType,
                       @SessionAttribute(required = false) String workDetailsHandle, @SessionAttribute(required = false) String workDetailsHandleDevice,
                       @SessionAttribute(required = false) String workDetailsStatus, @SessionAttribute(required = false) String workDetailsLocationFrom,
                       @SessionAttribute(required = false) String workDetailsLocationTo, @SessionAttribute(required = false) String workDetailsWorkNumber) {
        log.error("incoming URL: " + request.getHeader("Referer"));

        if (request.getHeader("Referer").contains("reception")) {
            model.addAttribute("searchingWarehouse", searchingWarehouse);

            if (session.getAttribute("searchingWarehouse") == null || session.getAttribute("searchingWarehouse").equals("")) {
                searchingWarehouse = "%";
                //TODO extract it to external method
                workDetailsWarehouse = searchingWarehouse;
                workDetailsStatus = "false";
                workDetailsCompany = companyService.getOneCompanyByUsername(SecurityUtils.username()).getName();
                workDetailsArticle = "";
                workDetailsType = "";
                workDetailsHandle = "";
                workDetailsHandleDevice = "";
                workDetailsLocationFrom = "";
                workDetailsLocationTo = "";
                workDetailsWorkNumber = "";
                log.error("1");
            }

            if (!searchingWarehouse.equals("%")) {
                Warehouse warehouse = warehouseService.getWarehouseByName(searchingWarehouse);
                model.addAttribute("warehouse", warehouse);
                List<WorkDetails> workDetails = workDetailsService.getWorkDetailsPerWarehouse(warehouse.getId());
                model.addAttribute("workDetails", workDetails);
                workDetailsWarehouse = searchingWarehouse;
                workDetailsStatus = "false";
                workDetailsCompany = companyService.getOneCompanyByUsername(SecurityUtils.username()).getName();
                workDetailsArticle = "";
                workDetailsType = "";
                workDetailsHandle = "";
                workDetailsHandleDevice = "";
                workDetailsLocationFrom = "";
                workDetailsLocationTo = "";
                workDetailsWorkNumber = "";
                log.error("2");
            }
            if (searchingWarehouse.equals("%")) {
                List<WorkDetails> workDetails = workDetailsService.getWorkDetails();
                model.addAttribute("workDetails", workDetails);
                workDetailsWarehouse = searchingWarehouse;
                workDetailsStatus = "false";
                workDetailsCompany = companyService.getOneCompanyByUsername(SecurityUtils.username()).getName();
                workDetailsArticle = "";
                workDetailsType = "";
                workDetailsHandle = "";
                workDetailsHandleDevice = "";
                workDetailsLocationFrom = "";
                workDetailsLocationTo = "";
                workDetailsWorkNumber = "";
                log.error("3");
            }
        }
        if (request.getHeader("Referer").contains("workDetails-browser")) {
            log.debug("workDetailsWarehouse: " + workDetailsWarehouse);
            log.debug("workDetailsCompany: " + workDetailsCompany);
            log.debug("workDetailsArticle: " + workDetailsArticle);
            log.debug("workDetailsType: " + workDetailsType);
            log.debug("workDetailsHandle: " + workDetailsHandle);
            log.debug("workDetailsHandleDevice: " + workDetailsHandleDevice);
            log.error("workDetailsStatus: " + workDetailsStatus);
            log.debug("workDetailsLocationFrom: " + workDetailsLocationFrom);
            log.debug("workDetailsLocationTo: " + workDetailsLocationTo);
            log.debug("workDetailsWorkNumber: " + workDetailsWorkNumber);

            log.error("4");
            List<WorkDetailsRepository.WorkHeaderList> workDetails = workDetailsService.workHeaderList(workDetailsWarehouse, workDetailsCompany, workDetailsArticle, workDetailsType, workDetailsHandle, workDetailsHandleDevice, workDetailsStatus, workDetailsLocationFrom, workDetailsLocationTo, workDetailsWorkNumber);
            model.addAttribute("workDetails", workDetails);

            if (workDetailsWarehouse.equals("%")) {
                searchingWarehouse = "%";
                model.addAttribute("searchingWarehouse", searchingWarehouse);
            }
            if (!workDetailsWarehouse.equals("%")) {
                searchingWarehouse = workDetailsWarehouse;
                Warehouse warehouse = warehouseService.getWarehouseByName(searchingWarehouse);
                model.addAttribute("warehouse", warehouse);
                model.addAttribute("searchingWarehouse", searchingWarehouse);
            }
        }
        if (!request.getHeader("Referer").contains("workDetails-browser") && !request.getHeader("Referer").contains("reception")) {
            searchingWarehouse = "%";
            workDetailsWarehouse = searchingWarehouse;
            workDetailsStatus = "false";
            workDetailsCompany = companyService.getOneCompanyByUsername(SecurityUtils.username()).getName();
            workDetailsArticle = "";
            workDetailsType = "";
            workDetailsHandle = "";
            workDetailsHandleDevice = "";
            workDetailsLocationFrom = "";
            workDetailsLocationTo = "";
            workDetailsWorkNumber = "";
            List<WorkDetailsRepository.WorkHeaderList> workDetails = workDetailsService.workHeaderList(workDetailsWarehouse, workDetailsCompany, workDetailsArticle, workDetailsType, workDetailsHandle, workDetailsHandleDevice, workDetailsStatus, workDetailsLocationFrom, workDetailsLocationTo, workDetailsWorkNumber);
            model.addAttribute("workDetails", workDetails);
            log.error("5");
        }


        model.addAttribute("workDetailsWarehouse", workDetailsWarehouse);
        model.addAttribute("workDetailsCompany", workDetailsCompany);
        model.addAttribute("workDetailsArticle", workDetailsArticle);
        model.addAttribute("workDetailsType", workDetailsType);
        model.addAttribute("workDetailsHandle", workDetailsHandle);
        model.addAttribute("workDetailsHandleDevice", workDetailsHandleDevice);
        model.addAttribute("workDetailsStatus", workDetailsStatus);
        model.addAttribute("workDetailsLocationFrom", workDetailsLocationFrom);
        model.addAttribute("workDetailsLocationTo", workDetailsLocationTo);
        model.addAttribute("workDetailsWorkNumber", workDetailsWorkNumber);

//        log.error("workDetailsWarehouse: " + workDetailsWarehouse);
//        log.error("workDetailsCompany: " + workDetailsCompany);
//        log.error("workDetailsArticle: " + workDetailsArticle);
//        log.error("workDetailsType: " + workDetailsType);
//        log.error("workDetailsHandle: " + workDetailsHandle);
//        log.error("workDetailsHandleDevice: " + workDetailsHandleDevice);
//        log.error("workDetailsStatus: " + workDetailsStatus);
//        log.error("workDetailsLocationFrom: " + workDetailsLocationFrom);
//        log.error("workDetailsLocationTo: " + workDetailsLocationTo);
//        log.error("workDetailsWorkNumber: " + workDetailsWorkNumber);



        usersService.loggedUserData(model,session);

        return "wmsOperations/workDetails";
    }

    @GetMapping("/workNumberDetails/{workNumber}")
    public String workNumberDetails(@PathVariable Long workNumber, Model model) {
        List<WorkDetails> workDetailsList = workDetailsRepository.getWorkDetailsByWorkNumber(workNumber);
        model.addAttribute("workDetailsList",workDetailsList);

        WorkDetailsRepository.WorkNumberDetailsInfo workNumberDetailsInfo = workDetailsRepository.workNumberDetailsInfo(workNumber);
        model.addAttribute("workNumberDetailsInfo",workNumberDetailsInfo);

        return "wmsOperations/workNumberDetails";
    }

    @GetMapping("/deleteWork/{id}")
    public String removeWork(@PathVariable Long id) {
        workDetailsService.delete(id);
        return "redirect:/workDetails";
    }

    @GetMapping("formEditWork/{id}")
    public String workEdit(@PathVariable Long id, Model model,HttpSession session) {
        WorkDetails workDetails = workDetailsService.findById(id);
        model.addAttribute(workDetails);
        model.addAttribute("localDateTime", LocalDateTime.now());


        usersService.loggedUserData(model,session);
        return "wmsOperations/formEditWork";
    }

    @PostMapping("formEditWork")
    public String editPost(WorkDetails workDetails) {
        workDetailsService.add(workDetails);
        return "redirect:/workDetails";
    }

    @GetMapping("workDetails-browser")
    public String browser(Model model,HttpSession session) {
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        usersService.loggedUserData(model,session);
        return "wmsOperations/workDetails-browser";
    }

    @PostMapping("workDetails-browser")
    public String findWorks(HttpSession session, String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType, String workDetailsHandle, String workDetailsHandleDevice, String workDetailsStatus, String workDetailsLocationFrom, String workDetailsLocationTo, String workDetailsWorkNumber) {
        session.setAttribute("workDetailsWarehouse", workDetailsWarehouse);
        session.setAttribute("workDetailsCompany", workDetailsCompany);
        session.setAttribute("workDetailsArticle", workDetailsArticle);
        session.setAttribute("workDetailsType", workDetailsType);
        session.setAttribute("workDetailsHandle", workDetailsHandle);
        session.setAttribute("workDetailsHandleDevice", workDetailsHandleDevice);
        session.setAttribute("workDetailsStatus", workDetailsStatus);
        session.setAttribute("workDetailsLocationFrom", workDetailsLocationFrom);
        session.setAttribute("workDetailsLocationTo", workDetailsLocationTo);
        session.setAttribute("workDetailsWorkNumber", workDetailsWorkNumber);
        return "redirect:/workDetails";
    }


}
