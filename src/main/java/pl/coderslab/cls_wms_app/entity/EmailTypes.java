package pl.coderslab.cls_wms_app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
public class EmailTypes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String type;

    private String created;
    private String last_update;
    private boolean active;
    private String changeBy;

    @ManyToMany(mappedBy="emailTypes")
    List<EmailRecipients> emailRecipients = new ArrayList<>();


    public EmailTypes(Long id, String type,String created,String last_update,boolean active,String changeBy) {
        this.id = id;
        this.type = type;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
    }

    @Override
    public String toString() {
        return "EmailTypes{" +
                "type='" + type + '\'' +
                '}';
    }
}
