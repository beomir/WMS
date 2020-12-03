//package com.example.security;
//
//import lombok.*;
//import org.hibernate.annotations.JoinColumnOrFormula;
//
//import javax.persistence.*;
//import java.util.Set;
//
//@Entity
//@Table(name = "users")
//@Data @NoArgsConstructor @AllArgsConstructor @Builder
//@EqualsAndHashCode(of = "username")
//@ToString(exclude = "password")
//public class UserEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    @Column(nullable = false, unique = true)
//    private String username;
//    @Column(nullable = false)
//    private String password;
//
//
//    @ElementCollection
//    @CollectionTable(name = "users_roles", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
//    @Column(name = "role")
//    private Set<String> roles;
//
//}
