//package pl.coderslab.cls_wms_app.fixtures;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.stereotype.Component;
//import pl.coderslab.cls_wms_app.entity.Company;
//import pl.coderslab.cls_wms_app.entity.Users;
//import pl.coderslab.cls_wms_app.entity.UsersDetails;
//import pl.coderslab.cls_wms_app.service.CompanyService;
//import pl.coderslab.cls_wms_app.service.UsersDetailsService;
//import pl.coderslab.cls_wms_app.service.UsersService;
//
//import java.util.Arrays;
//import java.util.List;
//
//@Component
//public class UsersDetailsFixture {
//    private UsersDetailsService usersDetailsService;
//    private UsersService usersService;
//    private CompanyService companyService;
//
//    @Autowired
//    public UsersDetailsFixture(UsersDetailsService usersDetailsService, UsersService usersService, CompanyService companyService) {
//        this.usersDetailsService = usersDetailsService;
//        this.usersService = usersService;
//        this.companyService = companyService;
//    }
//
//    private List<UsersDetails> usersDetailsList = Arrays.asList(
//            new UsersDetails("Eustachy", "Gniewomirowski", "555-666-777",  null, null),
//            new UsersDetails("Dobromir", "Igrekowski", "777-888-999",  null, null),
//            new UsersDetails("Andrzej", "Leszka", "666-555-666",  null, null)
//    );
//
//    public void loadIntoDB() {
//        List<Users> userList = usersService.getUsers();
//        List<Company> companies = companyService.getCompany();
//
//        for (UsersDetails usersDetails : usersDetailsList) {
//            usersDetailsService.add(usersDetails);
//        }
//        UsersDetails usersDetails1 = usersDetailsList.get(0);
//        UsersDetails usersDetails2 = usersDetailsList.get(1);
//        UsersDetails usersDetails3 = usersDetailsList.get(2);
//
//        usersDetails1.setUsers(userList.get(0));
//        usersDetails2.setUsers(userList.get(1));
//        usersDetails3.setUsers(userList.get(2));
//
//        usersDetailsService.add(usersDetails1);
//        usersDetailsService.add(usersDetails2);
//        usersDetailsService.add(usersDetails3);
//
//        usersDetails1.setCompany(companies.get(0));
//        usersDetails2.setCompany(companies.get(1));
//        usersDetails3.setCompany(companies.get(6));
//
//        usersDetailsService.add(usersDetails1);
//        usersDetailsService.add(usersDetails2);
//        usersDetailsService.add(usersDetails3);
//    }
//}
//
//
//
//
