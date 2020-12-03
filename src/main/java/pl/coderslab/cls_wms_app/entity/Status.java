package pl.coderslab.cls_wms_app.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="status")
public class Status {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;
    private String status_desc;
    private String created;

    private String last_update;

    public Status(Long id, String status, String status_desc, String created, String last_update) {
        this.id = id;
        this.status = status;
        this.status_desc = status_desc;
        this.created = created;
        this.last_update = last_update;
    }

    @OneToMany(mappedBy="status")
    private List<Stock> stockList = new ArrayList<>();

}
