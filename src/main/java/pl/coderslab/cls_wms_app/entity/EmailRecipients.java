package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class EmailRecipients {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @NotNull
    @ManyToOne
    private Company company;

    private String name;
    private String lastname;
    private String phone;


    private boolean active;
    private String created;
    private String last_update;
    private String changeBy;

    private String token;

    @ManyToMany
    List<EmailTypes> emailTypes = new ArrayList<>();

//    public EmailRecipients(Long id, String email, String name, String lastname, String phone, boolean active, String created, String last_update, String changeBy, Company company,String token) {
//        this.id = id;
//        this.email = email;
//        this.name = name;
//        this.lastname = lastname;
//        this.phone = phone;
//        this.active = active;
//        this.created = created;
//        this.last_update = last_update;
//        this.changeBy = changeBy;
//        this.company = company;
//        this.token = token;
//    }


    @Override
    public String toString() {
        return "EmailRecipients{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", active=" + active +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                ", changeBy='" + changeBy + '\'' +
                ", token='" + token + '\'' +
                ", emailTypes='" + emailTypes + '\'' +
                '}';
    }
}
