package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.repository.UsersRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UsersServiceImpl implements UsersService {
    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean resetPassword;

    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;

    }

    @Override
    public void add(Users users) {
        users.setPassword(passwordEncoder.encode(users.getPassword()));
        users.setActivateToken(SecurityUtils.uuidToken());
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
    public void loggedUserData(Model model) {
        String token = FindUsernameByToken(SecurityUtils.username());
        model.addAttribute("token", token);
        model.addAttribute("localDateTime", LocalDateTime.now());
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
            if (users.getPassword().contains(password2)) {
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
}
