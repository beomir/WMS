package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Users;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.UsersService;

import java.util.Arrays;
import java.util.List;

@Component
public class UsersFixture {
    private UsersService usersService;


    private List<Users> usersList = Arrays.asList(
             new Users(null, "MagdaM", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","StrojemMagdaM@meta.ua",true,"Strojem"),
             new Users(null, "PiotrP", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","buDUJEmPiotrP@wp.pl",true,"buDUJEm"),
             new Users(null, "GawelG", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","buDUJEmGawelG@wp.pl",true,"buDUJEm"),
             new Users(null, "RuslanR", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","RuslanR@BuldMate.com",true,"BuildMate"),
             new Users(null, "FernandosF", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","FernandosF@FNdMdAL.pt",true,"Fábrica Nacional de Munições de Armas Ligeiras"),
             new Users(null, "BenitoB", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","BenitoB@Eni.it",true,"Eni"),
             new Users(null, "ZladkoZ", "123", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_USER","ZladkoZ@HS.sb",true,"Hesteel Serbia"),
             new Users(null, "admin", "admin", "2020-11-28:T10:00:00","2020-11-28:T10:00:00","ROLE_ADMIN","beomir89@gmail.com",true,"all")
    );

    @Autowired
    public UsersFixture(UsersService usersService) {
        this.usersService = usersService;
    }

    public void loadIntoDB() {


        for (Users users : usersList) {

            usersService.add(users);
        }
//        List<Company> companies = companyService.getCompany();
//        Users users1 = usersList.get(0);
//        Users users2 = usersList.get(1);
//        Users users3 = usersList.get(2);
//
//        users1.setCompany(companies.get(0));
//        users2.setCompany(companies.get(1));
//        users3.setCompany(companies.get(6));

//        usersService.add(users1);
//        usersService.add(users2);
//        usersService.add(users3);

    }
}
