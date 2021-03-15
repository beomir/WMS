package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.entity.IssueLog;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.temporaryObjects.IssueLogSearch;
import pl.coderslab.cls_wms_app.service.wmsSettings.IssueLogService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.wmsValues.WarehouseService;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
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
        return "/wmsSettings/issueLog/issueLog";
    }

    @GetMapping("/user/issueLog-browser")
    public String browser(Model model) {
        model.addAttribute("issueLogSearching", new IssueLogSearch());
        usersService.loggedUserData(model);
        List<Warehouse> warehouses = warehouseService.getWarehouse();
        model.addAttribute("warehouses", warehouses);
        return "/wmsSettings/issueLog/issueLog-browser";
    }

    @PostMapping("/user/issueLog-browser")
    public String findIssues(IssueLogSearch issueLogSearching) {
        issueLogService.save(issueLogSearching);
        return "redirect:/user/issueLog";
    }


        @RequestMapping("files/input/receptions/errors/{name}")
        @ResponseBody
        public void show(@PathVariable ("name") String name, HttpServletResponse response) {
            if(name.contains(".txt")) response.setContentType("application/txt");
            response.setHeader("Content-Disposition","attachment; filename=" + name);
            response.setHeader("Content-Transfer-Encoding","binary");
            try{
                BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
                FileInputStream fis = new FileInputStream("D:\\repository\\WMS\\src\\main\\resources\\static\\files\\input\\receptions\\errors\\" + name);
//                FileInputStream fis = new FileInputStream("https://cls-wms.herokuapp.com/src/main/resources/static/files/input/receptions/errors/" + name);
                int len;
                byte[] buf = new byte[1024];
                while((len = fis.read(buf))>0){
                    bos.write(buf,0,len);
                }
                bos.close();
                response.flushBuffer();
            }
            catch (IOException e){
                e.printStackTrace();
            }
        }

}
