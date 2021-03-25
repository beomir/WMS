package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.UsersRoles;
import pl.coderslab.cls_wms_app.service.userSettings.UsersRolesService;

import java.util.Arrays;
import java.util.List;


@Component
//@Profile("local")
public class UsersRolesFixture {
    private UsersRolesService usersRolesService;



    private List<UsersRoles> usersRolesList = Arrays.asList(
            new UsersRoles(null, "ROLE_USER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to all options within Company","system"),
            new UsersRoles(null, "ROLE_RECEPTION_USER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to receptions modules within Company","system"),
            new UsersRoles(null, "ROLE_SHIPMENT_USER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to shipment modules within Company","system"),
            new UsersRoles(null, "ROLE_STOCK_USER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to stock modules within Company","system"),
            new UsersRoles(null, "ROLE_ADMIN", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access to all options","system"),
            new UsersRoles(null, "ROLE_SCANNER", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(), true, "User with access only to scanner area","system")
    );

    @Autowired
    public UsersRolesFixture(UsersRolesService usersRolesService) {
        this.usersRolesService = usersRolesService;
    }

    public void loadIntoDB() {
        for (UsersRoles usersRoles : usersRolesList) {
            usersRolesService.add(usersRoles);
        }
    }
}
