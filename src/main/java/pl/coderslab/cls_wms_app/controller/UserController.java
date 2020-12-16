package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;

import pl.coderslab.cls_wms_app.entity.UsersRoles;
import pl.coderslab.cls_wms_app.service.CompanyService;

import pl.coderslab.cls_wms_app.service.UsersRolesService;
import pl.coderslab.cls_wms_app.service.UsersService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/config")
public class UserController {



    private final UsersService usersService;
    private final CompanyService companyService;
    private final UsersRolesService usersRolesService;


    @Autowired
    public UserController(UsersService usersService, CompanyService companyService, UsersRolesService usersRolesService) {
        this.usersService = usersService;
        this.companyService = companyService;
        this.usersRolesService = usersRolesService;
    }

    @GetMapping("formUserCreation")
    public String form(Model model) {
        List<Company> companies = companyService.getCompany();
        model.addAttribute("localDateTime", LocalDateTime.now());
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
        usersService.add(users);
        return "redirect:/config/userList";
    }


    @GetMapping("userList")
    public String userList(Model model) {
        List<Company> companies = companyService.getCompany();
        List<Users> users = usersService.getUsers();
        model.addAttribute("companies", companies);
        model.addAttribute("user", users);
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);
        return "userList";
    }

    @GetMapping("/formUserEdit/{id}")
    public String updateUser(@PathVariable Long id, Model model) {
        Users user = usersService.findById(id);
        List<Company> companies = companyService.getCompany();
        model.addAttribute(user);
        model.addAttribute("companies", companies);
        model.addAttribute("localDateTime", LocalDateTime.now());
        List<Company> companys = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companys", companys);

        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        model.addAttribute("users_Roles", usersRolesList);
        return "formUserEdit";
    }

    @PostMapping("formUserEdit")
    public String edit(Users users) {
        usersService.add(users);
//        usersDetailsService.add(usersDetails);
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
        return "usersDeactivatedList";
    }


    @GetMapping("/deleteUser/{id}")
    public String deactivateUser(@PathVariable Long id) {
        usersService.delete(id);
        return "redirect:/config/userList";
    }

    @GetMapping("/removeUser/{id}")
    public String removeUser(@PathVariable Long id) {
        usersService.remove(id);
        return "redirect:/config/userList";
    }

    @GetMapping("/activateUser/{id}")
    public String activateUser(@PathVariable Long id) {
        usersService.activate(id);
        return "redirect:/config/usersDeactivatedList";
    }

}
