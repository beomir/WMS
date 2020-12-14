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
@Table(name="warehouse")
public class Warehouse {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String steward;
    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean european_union;

    private boolean active;

    private String created;

    private String last_update;
    private String changeBy;


    public Warehouse(Long id, String name, String steward, String city, String street, String post_code, String country, boolean european_union, String created, String last_update, boolean active,String changeBy) {
        this.id = id;
        this.steward = steward;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.european_union = european_union;
        this.created = created;
        this.last_update = last_update;
        this.name = name;
        this.active = active;
        this.changeBy = changeBy;
    }

    @OneToMany(mappedBy="warehouse")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="warehouse")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="warehouse")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="warehouse")
    private List<ShipmentInCreation> ShipmentInCreationList = new ArrayList<>();
}
