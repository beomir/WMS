package pl.coderslab.cls_wms_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.coderslab.cls_wms_app.app.SendEmailService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("")
public class StartController {

    private final SendEmailService sendEmailService;

    public StartController(SendEmailService sendEmailService) {
        this.sendEmailService = sendEmailService;
    }


    @GetMapping("/index")
    public String login() {
        return "index";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }


    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

    @PostMapping("contactForm")
    public String contactFormPost(String email,String name) {
        sendEmailService.sendEmailFromContactForm("<b>PL</b><br/><br/><i>Dzięki " + name + " za wypełnienie formularza. Właśnie zajmujemy się Twoim zgłoszeniem</i><br/><br/><b>GB</b><br/><br/><i>Thanks " + name + " for filling out the form. We are currently dealing with your application</i><br/><br/><b>FR</b><br/><br/><i>Merci  " + name + " de remplir le formulaire. Nous traitons actuellement votre candidature</i><br/><br/><b>ру</b><br/><br/><i>Спасибо " + name + " за заполнение формы. В настоящее время мы занимаемся вашей заявкой</i><br/><br/>",email);

        return "redirect:/contactForm";
    }

    @RequestMapping("/contactForm")
    public String registerConfirmation(){
        return "contactForm";
    }

}