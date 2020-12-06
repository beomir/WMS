package pl.coderslab.cls_wms_app.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@Entity
@Table(name="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean european_union;

    private boolean active;

    public Company(Long id, String name, String city, String street, String post_code, String country, boolean european_union, boolean active) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.european_union = european_union;
        this.active = active;
    }

    @OneToMany(mappedBy="company")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="company")
    private List<Shipment> shipmentList = new ArrayList<>();

//    @OneToMany(mappedBy="company")
//    private List<Users> UsersList = new ArrayList<>();

    @OneToMany(mappedBy="company")
    private List<Stock> stockList = new ArrayList<>();

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", post_code='" + post_code + '\'' +
                ", country='" + country + '\'' +
                ", european_union=" + european_union +
                ", active=" + active +
                '}';
    }
}
