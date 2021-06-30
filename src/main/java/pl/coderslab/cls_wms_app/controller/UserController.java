package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;

import pl.coderslab.cls_wms_app.entity.UsersRoles;
import pl.coderslab.cls_wms_app.repository.UsersRepository;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;

import pl.coderslab.cls_wms_app.service.userSettings.UsersRolesService;
import pl.coderslab.cls_wms_app.service.userSettings.UsersService;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/config")
public class UserController {



    private final UsersService usersService;
    private final CompanyService companyService;
    private final UsersRolesService usersRolesService;
    private final SendEmailService sendEmailService;
    private final UsersRepository usersRepository;



    @Autowired
    public UserController(UsersService usersService, CompanyService companyService, UsersRolesService usersRolesService, SendEmailService sendEmailService, UsersRepository usersRepository) {
        this.usersService = usersService;
        this.companyService = companyService;
        this.usersRolesService = usersRolesService;
        this.sendEmailService = sendEmailService;
        this.usersRepository = usersRepository;
    }

    @GetMapping("formUserCreation")
    public String form(Model model, HttpSession session) {
        List<Company> activeCompanies = companyService.getCompany();
        model.addAttribute("activeCompanies", activeCompanies);
        usersService.loggedUserData(model,session);
        model.addAttribute("users", new Users());
        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);

        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        model.addAttribute("users_Roles", usersRolesList);
        return "userSettings/formUserCreation";
    }

    @PostMapping("formUserCreation")
    public String add(Users users) {
        if(usersRepository.checkIfUserExists(users.getUsername()) == 0){
            sendEmailService.sendEmailFromContactForm("<p><span style='font-size:18px;color:darkslategray'>Twoje konto zostało utworzone z poniższymi danymi:</span><br/><b> Nazwa użytkownika:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Hasło:</b>" + users.getPassword() + "<br/><i>Zaleca się zmienić hasło po zalogowaniu wchodząc w opcje <b>my profile</b></i></p><br/><p><span style='font-size:18px;color:darkslategray'>Your account was created with below data:</span><br/><b> Username:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Password:</b>" + users.getPassword() + "<br/><i>It is recommended to change the password after entering the options <b>my profile</b></i></p><br/><p><span style='font-size:18px;color:darkslategray'>Ваша учетная запись была создана с использованием следующих данных:</span><br/><b> Имя пользователя:</b>" + users.getUsername() + "<br/><br/> <b>Эл. адрес:</b>" + users.getEmail() + "<br/><br/><b>пароль:</b>" + users.getPassword() + "<br/><i>После ввода параметров рекомендуется сменить пароль <b>my profile</b></i></p><br/><p><span style='font-size:18px;color:darkslategray'>Votre compte a été créé avec les données ci-dessous:</span><br/><b> Nom d'utilisateur:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Mot de passe:</b>" + users.getPassword() + "<br/><i>Il est recommandé de changer le mot de passe après avoir saisi les options <b>my profile</b></i></p>",users.getEmail(),"Create Account WMS CLS");
            usersService.add(users);
            return "redirect:/config/userList";
        }
        else {
            return "redirect:/config/usersList";
        }
    }

    @GetMapping("usersList")
    public String usersList(Model model,HttpSession session) {

        List<Users> users = usersService.getUsers();
        model.addAttribute("user", users);

        usersService.loggedUserData(model,session);
        return "userSettings/usersList";
    }


    @GetMapping("userList")
    public String userList(Model model,HttpSession session) {

        List<Users> users = usersService.getUsers();
        model.addAttribute("user", users);

        usersService.loggedUserData(model,session);
        return "userSettings/userList";
    }

    @GetMapping("/formUserEdit/{activateToken}")
    public String updateUser(@PathVariable String activateToken, Model model,HttpSession session) {
        Users user = usersService.getUserByActivateToken(activateToken);
        List<Company> activeCompanies = companyService.getCompany();
        model.addAttribute("activeCompanies", activeCompanies);
        model.addAttribute(user);

        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        model.addAttribute("users_Roles", usersRolesList);
        usersService.loggedUserData(model,session);
        return "userSettings/formUserEdit";
    }

    @PostMapping("formUserEdit")
    public String edit(Users users) {
        usersService.add(users);
        return "redirect:/config/userList";
    }


    @GetMapping("usersDeactivatedList")
    public String usersDeactivatedList(Model model,HttpSession session) {
        List<Users> users = usersService.getDeactivatedUsers();
        model.addAttribute("user", users);


        usersService.loggedUserData(model,session);
        return "/userSettings/usersDeactivatedList";
    }


    @GetMapping("/deleteUser/{activateToken}")
    public String deactivateUser(@PathVariable String activateToken) {
        usersService.delete(activateToken);
        return "redirect:/config/userList";
    }

    @GetMapping("/removeUser/{id}")
    public String removeUser(@PathVariable Long id) {
        usersService.remove(id);
        return "redirect:/config/userList";
    }

    @GetMapping("/activateUser/{activateToken}")
    public String activateUser(@PathVariable String activateToken) {
        usersService.activate(activateToken);
        return "redirect:/config/usersDeactivatedList";
    }



}
