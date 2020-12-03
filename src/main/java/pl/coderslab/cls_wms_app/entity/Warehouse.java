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

    private String warehouse;
    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean europen_union;

    private String created;

    private String last_update;


    public Warehouse(Long id, String warehouse, String city, String street, String post_code, String country, boolean europen_union, String created, String last_update) {
        this.id = id;
        this.warehouse = warehouse;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.europen_union = europen_union;
        this.created = created;
        this.last_update = last_update;
    }

    @OneToMany(mappedBy="warehouse")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="warehouse")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="warehouse")
    private List<Reception> receptionList = new ArrayList<>();
}
