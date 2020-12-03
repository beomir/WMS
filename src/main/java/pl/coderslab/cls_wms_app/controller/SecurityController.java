package pl.coderslab.cls_wms_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.service.UsersService;

@Controller
@RequestMapping("")
public class SecurityController {
    private UsersService usersService;

    @Autowired
    public SecurityController(UsersService usersService) {
        this.usersService = usersService;
    }

    @GetMapping("/login")
    public String form(Model model) {
        model.addAttribute("users", new Users());
        return "login";
    }

    @PostMapping("/login")
    public String formx(Users user) {
        return "redirect:/warehouse";
    }
}