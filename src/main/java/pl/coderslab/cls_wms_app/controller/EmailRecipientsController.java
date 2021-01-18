package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.entity.EmailTypes;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.EmailRecipientsService;
import pl.coderslab.cls_wms_app.service.EmailTypesService;
import pl.coderslab.cls_wms_app.service.UsersService;
import java.util.List;

@Controller
public class EmailRecipientsController {

    private final CompanyService companyService;
    private final EmailRecipientsService emailRecipientsService;
    private final EmailTypesService emailTypesService;
    private final UsersService usersService;

    @Autowired
    public EmailRecipientsController(CompanyService companyService, EmailRecipientsService emailRecipientsService, EmailTypesService emailTypesService, UsersService usersService) {
        this.companyService = companyService;
        this.emailRecipientsService = emailRecipientsService;
        this.emailTypesService = emailTypesService;
        this.usersService = usersService;
    }

    @GetMapping("emailRecipients")
    public String emailRecipientsList(Model model) {
        List<EmailRecipients> emailRecipients = emailRecipientsService.getEmailRecipientsForCompanyByUsername(SecurityUtils.username());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("emailRecipients", emailRecipients);
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "emailRecipients";
    }

    @GetMapping("/config/emailRecipientsDeactivatedList")
    public String emailRecipientsDeactivatedList(Model model) {
        List<EmailRecipients> emailRecipients = emailRecipientsService.getEmailRecipients();
        model.addAttribute("emailRecipients", emailRecipients);

        List<EmailTypes> emailTypes = emailTypesService.getEmailTypes();
        model.addAttribute("emailTypes", emailTypes);
        usersService.loggedUserData(model);

        return "emailRecipientsDeactivatedList";
    }


    @GetMapping("/user/formEmailRecipients")
    public String emailRecipientsForm(Model model){
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<EmailTypes> emailTypes = emailTypesService.getEmailTypes();
        model.addAttribute("emailTypes", emailTypes);
        model.addAttribute("emailRecipients", new EmailRecipients());
        model.addAttribute("companies", companies);
        usersService.loggedUserData(model);
        return "formEmailRecipients";
    }

    @PostMapping("formEmailRecipients")
    public String emailRecipientsAdd(EmailRecipients emailRecipients) {
        emailRecipientsService.saveFromForm(emailRecipients);
        return "redirect:/emailRecipients";
    }

    @GetMapping("/user/deactivateEmailRecipients/{token}")
    public String deactivateEmailRecipients(@PathVariable String token) {
        emailRecipientsService.deactivate(token);
        return "redirect:/emailRecipients";
    }

    @GetMapping("/config/activateEmailRecipients/{token}")
    public String activateEmailRecipients(@PathVariable String token) {
        emailRecipientsService.activate(token);
        return "redirect:/config/emailRecipientsDeactivatedList";
    }

    @GetMapping("/user/editEmailRecipients/{token}")
    public String updateEmailRecipients(@PathVariable String token, Model model) {
        EmailRecipients emailRecipients = emailRecipientsService.findByToken(token);
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        List<EmailTypes> emailTypes = emailTypesService.getEmailTypes();
        model.addAttribute("emailTypes", emailTypes);
        model.addAttribute(emailRecipients);
        model.addAttribute("companies", companies);
        usersService.loggedUserData(model);
        return "editEmailRecipients";
    }

    @PostMapping("editEmailRecipients")
    public String editEmailRecipients(EmailRecipients emailRecipients) {
        emailRecipientsService.editFromForm(emailRecipients);
        return "redirect:/emailRecipients";
    }

}
