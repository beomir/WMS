package pl.coderslab.cls_wms_app.service.userSettings;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.SendEmailService;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.repository.UsersRepository;
import pl.coderslab.cls_wms_app.service.storage.ArticleServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionServiceImpl;
import pl.coderslab.cls_wms_app.service.wmsValues.CompanyService;
import pl.coderslab.cls_wms_app.temporaryObjects.AddLocationToStorageZone;
import pl.coderslab.cls_wms_app.temporaryObjects.CheckPassword;
import pl.coderslab.cls_wms_app.temporaryObjects.LocationNameConstruction;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean resetPassword;
    private final ReceptionServiceImpl receptionServiceImpl;
    private final LocationNameConstruction locationNameConstruction;
    private final AddLocationToStorageZone addLocationToStorageZone;
    public String alertMessage = "";
    public String oldPass;
    private SendEmailService sendEmailService;
    private ArticleServiceImpl articleServiceImpl;
    private CompanyService companyService;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder, ReceptionServiceImpl receptionServiceImpl, LocationNameConstruction locationNameConstruction, AddLocationToStorageZone addLocationToStorageZone,  SendEmailService sendEmailService, ArticleServiceImpl articleServiceImpl, CompanyService companyService) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
        this.receptionServiceImpl = receptionServiceImpl;
        this.locationNameConstruction = locationNameConstruction;
        this.addLocationToStorageZone = addLocationToStorageZone;
        this.sendEmailService = sendEmailService;
        this.articleServiceImpl = articleServiceImpl;
        this.companyService = companyService;
    }

    @Override
    public void add(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(users);
    }

    @Override
    public void edit(Users users) {
        usersRepository.save(users);
    }

    @Override
    public void addWithoutCodePass(Users users) {
        users.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(users);
    }


    @Override
    public List<Users> getUser(Long id) {
        return usersRepository.getUser(id);
    }


    @Override
    public List<Users> getUsers() {
        return usersRepository.getUsers();
    }

    @Override
    public List<Users> getDeactivatedUsers() {
        return usersRepository.getDeactivatedUsers();
    }

    @Override
    public Users findById(Long id) {
        return usersRepository.getOne(id);
    }

    @Override
    public Users getUserByActivateToken(String activateToken) {
        return usersRepository.getUserByActivateToken(activateToken);
    }


    @Override
    public void delete(String activateToken) {
        Users users = usersRepository.getUserByActivateToken(activateToken);
        users.setActive(false);
        users.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        users.setChangeBy(SecurityUtils.usernameForActivations());
        usersRepository.save(users);
    }

    @Override
    public void remove(Long id) {
        usersRepository.deleteById(id);
    }

    @Override
    public void activate(String activateToken) {
        Users users = usersRepository.getUserByActivateToken(activateToken);
        users.setActive(true);
        users.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        users.setChangeBy(SecurityUtils.usernameForActivations());
        usersRepository.save(users);
    }

    @Override
    public void loggedUserData(Model model,HttpSession session) {
        receptionServiceImpl.insertReceptionFileResult = "";
        locationNameConstruction.message = "";
        addLocationToStorageZone.message = "";
        alertMessage = "";
        session.setAttribute("articleMessage","");
        session.setAttribute("productionMessage","");
        session.setAttribute("receptionMessage","");
        session.setAttribute("stockMessage","");
        session.setAttribute("productionArticleMessage","");
        session.setAttribute("stockTransferMessage","");
        String userName = "";
        if(SecurityUtils.username().equals("%")){
            userName = "admin";
        }
        else {
            userName = SecurityUtils.username();
        }
        String token = FindUsernameByToken(userName);
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());

        List<Company> companies = companyService.getCompanyByUsername(SecurityUtils.username());
        model.addAttribute("companies", companies);

        log.debug("token: " + token);

    }

    @Override
    public String warehouseSelection(HttpSession session, @SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request) {
        if(chosenWarehouse == null){
            if(request.getRequestURL().toString().contains("localhost:8080")){
                session.setAttribute("goingToURL",request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf("localhost:8080") + 14));
                log.error("localHost: " + session.getAttribute("goingToURL").toString());
            }
            else{
                session.setAttribute("goingToURL",request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf("https://cls-wms.herokuapp.com") + 28));
                log.error("Heroku: " +session.getAttribute("goingToURL").toString());
            }
            return "noDataAboutSelectedWarehouse";
        }
        else{
            return "warehouseSelected";
        }
    }

    @Override
    public void nextURL(HttpServletRequest request,HttpSession session){
        if(request.getRequestURL().toString().contains("localhost:8080")){
            session.setAttribute("nextURL",request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf("localhost:8080") + 14));
            log.error("localHost: " + session.getAttribute("nextURL").toString());
        }
        else{
            session.setAttribute("nextURL",request.getRequestURL().toString().substring(request.getRequestURL().toString().lastIndexOf("https://cls-wms.herokuapp.com") + 28));
            log.error("Heroku: " +session.getAttribute("nextURL").toString());
        }
    }

    @Override
    public String FindUsernameByToken(String username) {
        return usersRepository.FindUsernameByToken(username);
    }

    @Override
    public Users getByEmail(String email) {
        return usersRepository.getByEmail(email);
    }

    @Override
    public void setActivateUserAfterEmailValidation(String activateToken) {
        Users user = usersRepository.getUserByActivateToken(activateToken);
        user.setActive(true);
        user.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.setChangeBy("Activation");
        user.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(user);
    }

    @Override
    public void blockAccountAfterUnforeseenRestartPass(String activateToken) {
        Users user = usersRepository.getUserByActivateToken(activateToken);
        user.setActive(false);
        user.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        user.setChangeBy("Deactivated by User");
        user.setActivateToken(SecurityUtils.uuidToken());
        usersRepository.save(user);
    }

    @Override
    public boolean resetPasswordStatus() {
        return resetPassword;
    }

    @Override
    public void resetPassword(Users users, String password2) {
        if (usersRepository.getByEmail(users.getEmail()) != null) {
            if (users.getPassword().equals(password2)) {
                users.setId(usersRepository.getByEmail(users.getEmail()).getId());
                users.setChangeBy("Reset Password by: " + SecurityUtils.usernameForActivations());
                users.setLast_update(TimeUtils.timeNowLong());
                users.setUsersRoles(usersRepository.getByEmail(users.getEmail()).getUsersRoles());
                users.setUsername(usersRepository.getByEmail(users.getEmail()).getUsername());
                users.setCreated(usersRepository.getByEmail(users.getEmail()).getCreated());
                users.setPassword(passwordEncoder.encode(users.getPassword()));
                users.setActivateToken(SecurityUtils.uuidToken());
                users.setActive(true);
                resetPassword = true;
                resetPasswordStatus();
                usersRepository.save(users);
            } else {
                resetPassword = false;
                resetPasswordStatus();
            }
        }
    }

    @Override
    public String changePassword(Users users,String email, CheckPassword check ) {

        String enteredPassword = check.password1;
        String password =  oldPass.substring(8);

        System.out.println(BCrypt.checkpw(enteredPassword, password));
        if(check.password1.equals(check.password2) == true && check.password1 != null && check.password2 != null && BCrypt.checkpw(enteredPassword,password) ) {
            sendEmailService.sendEmailFromContactForm("<p>Twoje dane po dokonanych zmianach:<br/><br/><b> Nazwa użytkownika:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Hasło:</b>" + users.getPassword() + "</p><p>Your data after changes:<br/><br/><b> Username:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Password:</b>" + users.getPassword() + "</p><p>Ваши данные после изменений:<br/><b> Имя пользователя:</b>" + users.getUsername() + "<br/><br/> <b>Эл. адрес:</b>" + users.getEmail() + "<br/><br/><b>пароль:</b>" + users.getPassword() + "</p><p>Vos données après modifications:<br/><b> Nom d'utilisateur:</b>" + users.getUsername() + "<br/><br/> <b>Email:</b>" + users.getEmail() + "<br/><br/><b>Mot de passe:</b>" + users.getPassword() + "</p>",email,"CLS WMS data changed");
            add(users);
            return "redirect:/users/data-changed";
        }
        else if(check.password1 == null || check.password2 == null){
            alertMessage = "ERROR: One from old credentials field to fill were empty";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }
        else if(!check.password1.equals(check.password2)){
            alertMessage = "ERROR: Old credentials are not the same";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }
        else{
            alertMessage = "ERROR: The old password entered is incorrect";
            return "redirect:/users/myProfile/" + usersRepository.FindUsernameByToken(users.getUsername());
        }

    }
}
