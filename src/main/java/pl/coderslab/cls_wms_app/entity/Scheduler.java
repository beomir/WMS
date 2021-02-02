package pl.coderslab.cls_wms_app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity

@Table(name="scheduler")
public class Scheduler {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String minutes;
    private String hour;
    private String dayOfWeek;

    private String created;
    private String last_update;
    private String changeBy;
    private boolean active;

    @ManyToOne
    private Company company;

    public Scheduler(Long id, String type, String minutes, String hour, String dayOfWeek, String created, String last_update, String changeBy, boolean active, Company company) {
        this.id = id;
        this.type = type;
        this.minutes = minutes;
        this.hour = hour;
        this.dayOfWeek = dayOfWeek;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.active = active;
        this.company = company;
    }
}
