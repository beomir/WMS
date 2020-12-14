package pl.coderslab.cls_wms_app.entity;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="customers")
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Company company;

    private String name;
    private boolean active;
    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean european_union;

    private String created;

    private String last_update;
    private String changeBy;


    public Customer(Long id, Company company, String city, String street, String post_code, String country, boolean european_union, String name, boolean active,String created,String last_update, String changeBy) {
        this.id = id;
        this.company = company;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.european_union = european_union;
        this.name = name;
        this.active = active;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
    }
}
