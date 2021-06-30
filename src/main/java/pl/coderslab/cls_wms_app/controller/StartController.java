package pl.coderslab.cls_wms_app.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersServiceImpl;
import pl.coderslab.cls_wms_app.temporaryObjects.CheckPassword;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;

@Slf4j
@Controller
@RequestMapping("")
public class StartController {

    private final SendEmailService sendEmailService;
    private final UsersService usersService;
    private final UsersServiceImpl usersServiceImpl;
    private boolean userExists;
    private boolean activateTokenActive;
    private boolean unforeseenRestartPass;
    public CheckPassword checkPassword;



    public StartController(SendEmailService sendEmailService, UsersService usersService, UsersServiceImpl usersServiceImpl) {
        this.sendEmailService = sendEmailService;
        this.usersService = usersService;
        this.usersServiceImpl = usersServiceImpl;
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
        return "userSettings/contactForm";
    }

    @GetMapping("/users/myProfile/{token}")
    public String userPanel(@PathVariable String token, Model model) {

        Users users = usersService.getUserByActivateToken(token);
        log.debug(users.getPassword());
        model.addAttribute(users);
        CheckPassword checkPass = new CheckPassword();
        model.addAttribute("checkPassword", checkPass);
        model.addAttribute("localDateTime", LocalDateTime.now());
        log.debug(usersServiceImpl.alertMessage);
        model.addAttribute("alertMessage", usersServiceImpl.alertMessage);
        usersServiceImpl.oldPass = users.getPassword();
        return "userSettings/myProfile";
    }

    @PostMapping("/users/myProfile")
    public String usersDataChanges(Users users,String email, CheckPassword check) {
        return usersService.changePassword(users,email,check);
    }

    @GetMapping("/users/data-changed")
    public String data_changed(Model model,HttpSession session){
        usersService.loggedUserData(model,session);
        return "/userSettings/data-changed";
    }

    @GetMapping("/blog/resetPassword")
    public String resetPassword(Model model, HttpSession session) {
        usersService.loggedUserData(model,session);
        return "userSettings/resetPassword";
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
        return "userSettings/resetPassword-confirmation";
    }

    @GetMapping("/blog/passwordRestarted/{activateToken}")
    public String passwordRestarted(@PathVariable String activateToken, Model model){
        try {
            Users user = usersService.getUserByActivateToken(activateToken);
            model.addAttribute("user", user);
            usersService.setActivateUserAfterEmailValidation(activateToken);
            activateTokenActive = true;
            model.addAttribute("activateTokenActive", activateTokenActive);
            return "userSettings/passwordRestarted";
        }
        catch(NullPointerException e) {
            activateTokenActive = false;
            model.addAttribute("activateTokenActive", activateTokenActive);
            return "userSettings/passwordRestarted";
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
        return "userSettings/passwordRestartedStep2";
    }

    @GetMapping("/blog/blockMyAccount/{activateToken}")
    public String blockAccountAfterUnforeseenRestartPass(@PathVariable String activateToken, Model model){
        try {
            Users user = usersService.getUserByActivateToken(activateToken);
            model.addAttribute("user", user);
            usersService.blockAccountAfterUnforeseenRestartPass(activateToken);
            unforeseenRestartPass = true;
            model.addAttribute("unforeseenRestartPass", unforeseenRestartPass);
            return "userSettings/blockMyAccount";
        }
        catch(NullPointerException e) {
            unforeseenRestartPass = false;
            model.addAttribute("unforeseenRestartPass", unforeseenRestartPass);
            return "userSettings/blockMyAccount";
        }
    }
}