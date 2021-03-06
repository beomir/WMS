package pl.coderslab.cls_wms_app.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    private boolean userExists;
    private boolean activateTokenActive;
    private boolean unforeseenRestartPass;

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
        sendEmailService.sendEmailFromContactForm("<b>PL</b><br/><br/><i>Dzięki " + name + " za wypełnienie formularza. Właśnie zajmujemy się Twoim zgłoszeniem</i><br/><br/><b>GB</b><br/><br/><i>Thanks " + name + " for filling out the form. We are currently dealing with your application</i><br/><br/><b>FR</b><br/><br/><i>Merci  " + name + " de remplir le formulaire. Nous traitons actuellement votre candidature</i><br/><br/><b>ру</b><br/><br/><i>Спасибо " + name + " за заполнение формы. В настоящее время мы занимаемся вашей заявкой</i><br/><br/>",email,"WMS CLS Account Request");
        sendEmailService.sendEmailFromContactForm("Please create account for: <b>" + name + "</b>, <b>" + lastname + "</b>,<b>" + email +"</b>", "beomir89@gmail.com","Create account CLS WMS");
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
            sendEmailService.sendEmailFromContactForm("<p>Twoje dane po dokonanych zmianach:<br/><br/><b> Nazwa użytkownika:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Hasło:</b>" + users.getPassword() + "</p><p>Your data after changes:<br/><br/><b> Username:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Password:</b>" + users.getPassword() + "</p><p>Ваши данные после изменений:<br/><b> Имя пользователя:</b>" + users.getUsername() + "<br/><br/> <b>Эл. адрес:</b>" + users.getEmail() + "<br/><br/><b>пароль:</b>" + users.getPassword() + "</p><p>Vos données après modifications:<br/><b> Nom d'utilisateur:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Mot de passe:</b>" + users.getPassword() + "</p>",email,"CLS WMS data changed");
            usersService.add(users);
            return "redirect:/users/data-changed";
    }

    @RequestMapping("/users/data-changed")
    public String data_changed(){
        return "data-changed";
    }

    @GetMapping("/blog/resetPassword")
    public String resetPassword(Model model) {
        usersService.loggedUserData(model);
        return "resetPassword";
    }

    @PostMapping("/blog/resetPassword")
    public String resetPasswordPost(String email) {
        try {
            sendEmailService.sendEmailFromContactForm("<p>W celu zresetowania hasla kliknij: <a href='https://cls-wms.herokuapp.com/blog/passwordRestarted/" + usersService.getByEmail(email).getActivateToken() + "'>Tutaj</a></p><br/><p>To reset your password, click: <a href='https://cls-wms.herokuapp.com/blog/passwordRestarted/" + usersService.getByEmail(email).getActivateToken() + "'>Here</a></p><br/><p>Чтобы сбросить пароль, нажмите: <a href='https://cls-wms.herokuapp.com/blog/passwordRestarted/" + usersService.getByEmail(email).getActivateToken() + "'>Вот</a></p><br/><p>Pour réinitialiser votre mot de passe, cliquez sur: <a href='https://cls-wms.herokuapp.com/blog/passwordRestarted/" + usersService.getByEmail(email).getActivateToken() + "'>Ici</a></p>", email,"Reset Password");
            userExists = true;
            return "redirect:/blog/resetPassword-confirmation";
        }
        catch(NullPointerException e) {
            userExists = false;
            return "redirect:/blog/resetPassword-confirmation";
        }
    }

    @RequestMapping("/blog/resetPassword-confirmation")
    public String resetPasswordConfirmation(Model model){
        model.addAttribute("userExists", userExists);
        return "resetPassword-confirmation";
    }

    @GetMapping("/blog/passwordRestarted/{activateToken}")
    public String passwordRestarted(@PathVariable String activateToken, Model model){
        try {
            Users user = usersService.getUserByActivateToken(activateToken);
            model.addAttribute("user", user);
            usersService.setActivateUserAfterEmailValidation(activateToken);
            activateTokenActive = true;
            model.addAttribute("activateTokenActive", activateTokenActive);
            return "passwordRestarted";
        }
        catch(NullPointerException e) {
            activateTokenActive = false;
            model.addAttribute("activateTokenActive", activateTokenActive);
            return "passwordRestarted";
        }
    }

    @PostMapping("/blog/passwordRestarted")
    public String passwordRestartedPost(Users users,String password2) {
        usersService.resetPassword(users, password2);
        if(usersService.resetPasswordStatus()) {
            sendEmailService.sendEmailFromContactForm( "<p>Twoje nowe hasło to: <b>" + password2 + "</b>.<br/><br/>Jeżeli nie resetowałeś hasła kliknij: <a href='https://cls-wms.herokuapp.com/blog/blockMyAccount/" + usersService.getByEmail(users.getEmail()).getActivateToken() + "'>Tutaj</a></p><br/><p>Your new password is: <b>" + password2 + "</b>.<br/><br/>If you didnt restart your password click here: <a href='https://cls-wms.herokuapp.com/blog/blockMyAccount/" + usersService.getByEmail(users.getEmail()).getActivateToken() + "'>Here</a><br/><p>Your new password is: <b>" + password2 + "</b>.<br/><br/>Si vous n'avez pas redémarré votre mot de passe, cliquez ici: <a href='https://cls-wms.herokuapp.com/blog/blockMyAccount/" + usersService.getByEmail(users.getEmail()).getActivateToken() + "'>Ici</a><br/><p>Votre nouveau mot de passe est: <b>" + password2 + "</b>.<br/><br/>Если вы не перезапускали пароль, нажмите здесь: <a href='https://cls-wms.herokuapp.com/blog/blockMyAccount/" + usersService.getByEmail(users.getEmail()).getActivateToken() + "'>Вот</a><br/><p>Ваш новый пароль:: <b>" + password2 + "</b>.</p>",users.getEmail(), "New Password");
        }
        return "redirect:/blog/passwordRestartedStep2";
    }

    @GetMapping("/blog/passwordRestartedStep2")
    public String passwordRestartedFinish(Model model){
        model.addAttribute("resetPassword", usersService.resetPasswordStatus());
        return "passwordRestartedStep2";
    }

    @GetMapping("/blog/blockMyAccount/{activateToken}")
    public String blockAccountAfterUnforeseenRestartPass(@PathVariable String activateToken, Model model){
        try {
            Users user = usersService.getUserByActivateToken(activateToken);
            model.addAttribute("user", user);
            usersService.blockAccountAfterUnforeseenRestartPass(activateToken);
            unforeseenRestartPass = true;
            model.addAttribute("unforeseenRestartPass", unforeseenRestartPass);
            return "blockMyAccount";
        }
        catch(NullPointerException e) {
            unforeseenRestartPass = false;
            model.addAttribute("unforeseenRestartPass", unforeseenRestartPass);
            return "blockMyAccount";
        }
    }
}