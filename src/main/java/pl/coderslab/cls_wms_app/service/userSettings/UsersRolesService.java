package pl.coderslab.cls_wms_app.service.userSettings;

import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.UsersRoles;

import java.util.List;

public interface UsersRolesService {

    void add(UsersRoles usersroles);

    List<UsersRoles> getUsersRoles();

    List<UsersRoles> getDeactivatedUsersRoles();

    UsersRoles findById(Long id);

    void delete(Long id);

    void activate(Long id);

    List<Long> getUsersRolesId();

}
