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
    private String process;
    private String created;

    private String last_update;
    private String changeBy;

    public Status(Long id, String status, String status_desc,String process, String created, String last_update,String changeBy) {
        this.id = id;
        this.status = status;
        this.status_desc = status_desc;
        this.process = process;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
    }

    @OneToMany(mappedBy="status")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="status")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="status")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="status")
    private List<ShipmentInCreation> ShipmentInCreationList = new ArrayList<>();

    @Override
    public String toString() {
        return "Status{" +
                "id=" + id +
                ", status='" + status + '\'' +
                ", status_desc='" + status_desc + '\'' +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                '}';
    }
}
