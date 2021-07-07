package pl.coderslab.cls_wms_app.service.userSettings;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.temporaryObjects.CheckPassword;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

public interface UsersService {

    void add(Users users);

    void addWithoutCodePass(Users users);


    List<Users> getUsers();

    List<Users> getDeactivatedUsers();

    Users findById(Long id);

    Users getUserByActivateToken(String activateToken);

    List<Users> getUser(Long id);

    void delete(String activateToken);

    void remove(Long id);

    void activate(String activateToken);

    void loggedUserData(Model model,HttpSession session);

    String warehouseSelection(HttpSession session,  @SessionAttribute(required = false) String chosenWarehouse, HttpServletRequest request);

    String FindUsernameByToken(String username);

    Users getByEmail(String email);

    void setActivateUserAfterEmailValidation(String activateToken);

    void blockAccountAfterUnforeseenRestartPass(String activateToken);

    boolean resetPasswordStatus();

    void resetPassword(Users users,String password2);

    String changePassword(Users users,String email, CheckPassword check);

    void edit(Users users);
}
