package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.SchedulerService;
import pl.coderslab.cls_wms_app.service.UsersService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class SchedulerController {

    private final CompanyService companyService;
    private final SchedulerService schedulerService;
    private final UsersService usersService;

    @Autowired
    public SchedulerController(CompanyService companyService, SchedulerService schedulerService, UsersService usersService) {
        this.companyService = companyService;
        this.schedulerService = schedulerService;
        this.usersService = usersService;
    }

    @GetMapping("/user/scheduler")
    public String list(Model model) {
        List<Scheduler> scheduler = schedulerService.getScheduler(SecurityUtils.username());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("scheduler", scheduler);
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "scheduler";
    }

    @GetMapping("/config/schedulersDeactivatedList")
    public String schedulerDeactivatedList(Model model) {
        List<Scheduler> scheduler = schedulerService.getDeactivatedScheduler();
        model.addAttribute("scheduler", scheduler);
        return "schedulersDeactivatedList";
    }

    @GetMapping("/user/formScheduler")
    public String schedulerForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("localDateTime", LocalDateTime.now());
        model.addAttribute("scheduler", new Scheduler());
        model.addAttribute("companies", companies);
        usersService.loggedUserData(model);
        return "formScheduler";
    }

    @PostMapping("formScheduler")
    public String schedulerAdd(Scheduler scheduler) {
        schedulerService.add(scheduler);
        return "redirect:/user/scheduler";
    }

    @GetMapping("/user/deleteScheduler/{id}")
    public String removeScheduler(@PathVariable Long id) {
        schedulerService.delete(id);
        return "redirect:/user/scheduler";
    }

    @GetMapping("/config/activateScheduler/{id}")
    public String activateScheduler(@PathVariable Long id) {
        schedulerService.activate(id);
        return "redirect:/config/schedulersDeactivatedList";
    }

    @GetMapping("/user/formEditScheduler/{id}")
    public String updateScheduler(@PathVariable Long id, Model model) {
        Scheduler scheduler = schedulerService.findById(id);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute(scheduler);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        usersService.loggedUserData(model);
        return "formEditScheduler";
    }

    @PostMapping("formEditScheduler")
    public String edit(Scheduler scheduler) {
        schedulerService.add(scheduler);
        return "redirect:/user/scheduler";
    }

}
