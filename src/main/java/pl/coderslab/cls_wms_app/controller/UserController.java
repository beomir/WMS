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
import pl.coderslab.cls_wms_app.service.CompanyService;

import pl.coderslab.cls_wms_app.service.UsersRolesService;
import pl.coderslab.cls_wms_app.service.UsersService;

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
    public String form(Model model) {
        List<Company> companies = companyService.getCompany();
        usersService.loggedUserData(model);
        model.addAttribute("users", new Users());
        model.addAttribute("companies", companies);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        model.addAttribute("users_Roles", usersRolesList);
        return "formUserCreation";
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
    public String usersList(Model model) {
        List<Company> companies = companyService.getCompany();
        List<Users> users = usersService.getUsers();
        model.addAttribute("companies", companies);
        model.addAttribute("user", users);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "usersList";
    }


    @GetMapping("userList")
    public String userList(Model model) {
        List<Company> companies = companyService.getCompany();
        List<Users> users = usersService.getUsers();
        model.addAttribute("companies", companies);
        model.addAttribute("user", users);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        usersService.loggedUserData(model);
        return "userList";
    }

    @GetMapping("/formUserEdit/{activateToken}")
    public String updateUser(@PathVariable String activateToken, Model model) {
        Users user = usersService.getUserByActivateToken(activateToken);
        List<Company> companies = companyService.getCompany();
        model.addAttribute(user);
        model.addAttribute("companies", companies);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        model.addAttribute("users_Roles", usersRolesList);
        usersService.loggedUserData(model);
        return "formUserEdit";
    }

    @PostMapping("formUserEdit")
    public String edit(Users users) {
        usersService.add(users);
        return "redirect:/config/userList";
    }


    @GetMapping("usersDeactivatedList")
    public String usersDeactivatedList(Model model) {
        List<Company> companies = companyService.getCompany();
        List<Users> users = usersService.getDeactivatedUsers();
        model.addAttribute("companies", companies);
        model.addAttribute("user", users);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        usersService.loggedUserData(model);
        return "usersDeactivatedList";
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
