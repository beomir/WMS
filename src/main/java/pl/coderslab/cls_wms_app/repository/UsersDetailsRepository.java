//package pl.coderslab.cls_wms_app.repository;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//import pl.coderslab.cls_wms_app.entity.Company;
//import pl.coderslab.cls_wms_app.entity.Users;
//import pl.coderslab.cls_wms_app.entity.UsersDetails;
//
//import java.util.List;
//
//@Repository
//public interface UsersDetailsRepository extends JpaRepository<UsersDetails, Long> {
//
//    @Query("Select ud from UsersDetails ud")
//    List<UsersDetails> getUsersDetails();
//
//    @Query("Select ud from UsersDetails ud join fetch ud.company c join fetch ud.users u")
//    List<Company> getCompanyByUsersDetails();

//    @Modifying
//    @Query("update Users u set u.email = :email where u.id = :id")
//    void edit(@Param(value = "id") long id, @Param(value = "email") String email);

//}
