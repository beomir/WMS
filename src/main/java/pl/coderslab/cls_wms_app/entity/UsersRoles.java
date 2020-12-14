package pl.coderslab.cls_wms_app.entity;


import lombok.Data;

import lombok.NoArgsConstructor;


import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

@Table(name="users_roles")
public class UsersRoles {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String role;
    private String description;

    private String created;
    private String last_update;
    private String changeBy;
    private boolean active;


    public UsersRoles(Long id, String role, String created, String last_update, boolean active,String description,String changeBy) {
        this.id = id;
        this.role = role;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.description = description;
        this.changeBy = changeBy;
    }

    @OneToMany(fetch = FetchType.EAGER,mappedBy="usersRoles")
    private List<Users> usersList = new ArrayList<>();

    @Override
    public String toString() {
        return "UsersRoles{" +
                "id=" + id +
                ", role='" + role + '\'' +
                ", description='" + description + '\'' +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                ", changeBy='" + changeBy + '\'' +
                ", active=" + active +
                '}';
    }
}
