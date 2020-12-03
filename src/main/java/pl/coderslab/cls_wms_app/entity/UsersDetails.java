//package pl.coderslab.cls_wms_app.entity;
//
//import com.sun.istack.NotNull;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import javax.persistence.*;
//
//@Data
//@NoArgsConstructor
//@Entity
//@AllArgsConstructor
//@Table(name = "users_details")
//public class UsersDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    private String firstName;
//    private String lastName;
//    private String phoneNumber;
//
//    @NotNull
//    @ManyToOne
//    private Company company;
//
////    @Transient
//    @OneToOne
//    @JoinColumn(name = "user_id")
//    private Users users;
//
//    public UsersDetails(String firstName, String lastName, String phoneNumber,  Company company, Users users) {
//        this.firstName = firstName;
//        this.lastName = lastName;
//        this.phoneNumber = phoneNumber;
//        this.company = company;
//        this.users = users;
//    }
//}
//
