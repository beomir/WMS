package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.entity.UsersRoles;
import pl.coderslab.cls_wms_app.service.UsersRolesService;
import pl.coderslab.cls_wms_app.service.UsersService;

import java.util.Arrays;
import java.util.List;

@Component
public class UsersFixture {
    private UsersService usersService;
    private UsersRolesService usersRolesService;

    private List<Users> usersList = Arrays.asList(
             new Users(null, "MagdaM", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"StrojemMagdaM@meta.ua",true,"Strojem","system"),
             new Users(null, "PiotrP", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"buDUJEmPiotrP@wp.pl",true,"buDUJEm","system"),
             new Users(null, "GawelG", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"buDUJEmGawelG@wp.pl",true,"buDUJEm","system"),
             new Users(null, "RuslanR", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"RuslanR@BuldMate.com",true,"BuildMate","system"),
             new Users(null, "FernandosF", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"FernandosF@FNdMdAL.pt",true,"Fábrica Nacional de Munições de Armas Ligeiras","system"),
             new Users(null, "BenitoB", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"BenitoB@Eni.it",true,"Eni","system"),
             new Users(null, "ZladkoZ", "123", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"ZladkoZ@HS.sb",true,"Hesteel Serbia","system"),
             new Users(null, "admin", "admin", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),null,"beomir89@gmail.com",true,"all","system")
    );

    @Autowired
    public UsersFixture(UsersService usersService, UsersRolesService usersRolesService) {
        this.usersService = usersService;
        this.usersRolesService = usersRolesService;
    }

    public void loadIntoDB() {


        for (Users users : usersList) {

            usersService.add(users);
        }
        List<UsersRoles> usersRolesList = usersRolesService.getUsersRoles();
        Users users1 = usersList.get(0);
        Users users2 = usersList.get(1);
        Users users3 = usersList.get(2);
        Users users4 = usersList.get(3);
        Users users5 = usersList.get(4);
        Users users6 = usersList.get(5);
        Users users7 = usersList.get(6);
        Users users8 = usersList.get(7);
//
        users1.setUsersRoles(usersRolesList.get(0));
        users2.setUsersRoles(usersRolesList.get(1));
        users3.setUsersRoles(usersRolesList.get(0));
        users4.setUsersRoles(usersRolesList.get(0));
        users5.setUsersRoles(usersRolesList.get(0));
        users6.setUsersRoles(usersRolesList.get(0));
        users7.setUsersRoles(usersRolesList.get(2));
        users8.setUsersRoles(usersRolesList.get(4));
//
        usersService.addWithoutCodePass(users1);
        usersService.addWithoutCodePass(users2);
        usersService.addWithoutCodePass(users3);
        usersService.addWithoutCodePass(users4);
        usersService.addWithoutCodePass(users5);
        usersService.addWithoutCodePass(users6);
        usersService.addWithoutCodePass(users7);
        usersService.addWithoutCodePass(users8);
    }
}
