package pl.coderslab.cls_wms_app.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity

@Table(name="users")
@EqualsAndHashCode(of = "username")
@ToString(exclude = "password")
public class Users {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true)
    private String username;
    @Column(nullable = false)
    private String password;
    private String email;

//    private String role;

    private String created;

    private String last_update;
    private boolean active;

//    @Transient
//    @OneToOne(mappedBy = "users")
//    private UsersDetails usersDetails;

    @ManyToOne
    private UsersRoles usersRoles;

    private String company;
    private String changeBy;

    public Users(Long id, String username, String password, String created, String last_update, UsersRoles usersRoles,String email, boolean active, String company, String changeBy) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.created = created;
        this.last_update = last_update;
        this.usersRoles = usersRoles;
        this.email = email;
        this.active = active;
        this.company = company;
        this.changeBy = changeBy;
    }

}
