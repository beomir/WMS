package pl.coderslab.cls_wms_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.coderslab.cls_wms_app.entity.Users;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("")
public class StartController {


    @GetMapping("/index")
    public String login() {
        return "index";
    }

    @GetMapping("/accessDenied")
    public String accessDenied() {
        return "accessDenied";
    }

//    @PostMapping("contactForm")
//    public String contactForm( String message, String name, String surname, String email) {
//        sendEmailService.sendEmailFromContactForm("<span style=\"font-size: 16px;\"><b>Name: </b><i>" + name + "</i><br/><b> Surname:</b><i> " + surname + "</i><br/><b>Email:</b> " + email + "<br/><br/><b>Message:</b><i></span> " + message + "</i>");
//        sendEmailService.sendEmail(email,"Witaj " + name + " " + surname + ", otrzymaliśmy od Ciebię prośbę o kontakt. Skontaktujemy sie z Tobą tak szybko jak będzie to możliwe","Potwierdzenie prośby o kontakt");
//        return "redirect:/contactForm-confirmation";
//    }

    @RequestMapping(value="/logout", method = RequestMethod.GET)
    public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null){
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
        return "redirect:/login?logout";
    }

}