package pl.coderslab.cls_wms_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;

@Controller
@RequestMapping("")
public class StartController {

    private final SendEmailService sendEmailService;
    private final UsersService usersService;

    public StartController(SendEmailService sendEmailService, UsersService usersService) {
        this.sendEmailService = sendEmailService;
        this.usersService = usersService;
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
    public String contactFormPost(String email,String name,String lastname) {
        sendEmailService.sendEmailFromContactForm("<b>PL</b><br/><br/><i>Dzięki " + name + " za wypełnienie formularza. Właśnie zajmujemy się Twoim zgłoszeniem</i><br/><br/><b>GB</b><br/><br/><i>Thanks " + name + " for filling out the form. We are currently dealing with your application</i><br/><br/><b>FR</b><br/><br/><i>Merci  " + name + " de remplir le formulaire. Nous traitons actuellement votre candidature</i><br/><br/><b>ру</b><br/><br/><i>Спасибо " + name + " за заполнение формы. В настоящее время мы занимаемся вашей заявкой</i><br/><br/>",email);
        sendEmailService.sendEmailFromContactForm("Please create account for: <b>" + name + "</b>, <b>" + lastname + "</b>,<b>" + email +"</b>", "beomir89@gmail.com");
        return "redirect:/contactForm";
    }

    @RequestMapping("/contactForm")
    public String registerConfirmation(){
        return "contactForm";
    }

    @GetMapping("/users/myProfile/{token}")
    public String userPanel(@PathVariable String token, Model model) {
        Users users = usersService.getUserByActivateToken(token);
        model.addAttribute(users);

        model.addAttribute("localDateTime", LocalDateTime.now());
        return "myProfile";
    }

    @PostMapping("/users/myProfile")
    public String usersDataChanges(Users users,String email) {
            sendEmailService.sendEmailFromContactForm("<p>Twoje dane po dokonanych zmianach:<br/><br/><b> Nazwa użytkownika:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Hasło:</b>" + users.getPassword() + "</p><p>Your data after changes:<br/><br/><b> Username:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Password:</b>" + users.getPassword() + "</p><p>Ваши данные после изменений:<br/><b> Имя пользователя:</b>" + users.getUsername() + "<br/><br/> <b>Эл. адрес:</b>" + users.getEmail() + "<br/><br/><b>пароль:</b>" + users.getPassword() + "</p><p>Vos données après modifications:<br/><b> Nom d'utilisateur:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Mot de passe:</b>" + users.getPassword() + "</p>",email);
            usersService.add(users);
            return "redirect:/users/data-changed";
    }

    @RequestMapping("/users/data-changed")
    public String data_changed(){
        return "data-changed";
    }
}