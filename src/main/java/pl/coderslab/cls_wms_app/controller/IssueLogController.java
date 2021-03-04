package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.service.*;

import java.util.List;

@Slf4j
@Controller
public class IssueLogController {


    private final IssueLogService issueLogService;
    private final UsersService usersService;
    private final IssueLogSearch issueLogSearch;
    private final WarehouseService warehouseService;


    @Autowired
    public IssueLogController(IssueLogService issueLogService, UsersService usersService, IssueLogSearch issueLogSearch, WarehouseService warehouseService) {
        this.issueLogService = issueLogService;
        this.usersService = usersService;
        this.issueLogSearch = issueLogSearch;
        this.warehouseService = warehouseService;
    }

    @GetMapping("/user/issueLog")
    public String list(Model model) {
        List<IssueLog> issueLogs = issueLogService.getIssueLogByAllCriteria(issueLogSearch.getIssueLogFileName(),issueLogSearch.getCreatedFrom(),issueLogSearch.getCreatedTo(),issueLogSearch.getCreatedBy(),issueLogSearch.getIssueLogContent(),issueLogSearch.getWarehouse());
        log.debug(issueLogSearch.getIssueLogFileName() + " " + issueLogSearch.getCreatedFrom() + " " + issueLogSearch.getCreatedTo() + " " + issueLogSearch.getCreatedBy() + " " + issueLogSearch.getIssueLogContent() + " " + issueLogSearch.getWarehouse());
        model.addAttribute("issueLogs", issueLogs);
        model.addAttribute("issueLogSearch",issueLogSearch);
        usersService.loggedUserData(model);
        return "issueLog";
    }

    @GetMapping("/user/issueLog-browser")
    public String browser(Model model) {
        model.addAttribute("issueLogSearching", new IssueLogSearch());
        usersService.loggedUserData(model);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        return "issueLog-browser";
    }

    @PostMapping("/user/issueLog-browser")
    public String findIssues(IssueLogSearch issueLogSearching) {
        issueLogService.save(issueLogSearching);
        return "redirect:/user/issueLog";
    }

}
