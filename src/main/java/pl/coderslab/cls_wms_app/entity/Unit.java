package pl.coderslab.cls_wms_app.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity

@Table(name="unit")
public class Unit {



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    private String created;
    private String last_update;
    private boolean active;


    @OneToMany(mappedBy="unit")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="unit")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="unit")
    private List<Shipment> shipmentList = new ArrayList<>();

    public Unit(Long id, String name, String description, String created, String last_update, boolean active) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
    }
}