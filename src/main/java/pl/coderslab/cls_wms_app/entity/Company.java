package pl.coderslab.cls_wms_app.entity;


import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String company;
    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean europen_union;

    public Company(Long id, String company, String city, String street, String post_code, String country, boolean europen_union) {
        this.id = id;
        this.company = company;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.europen_union = europen_union;
    }


    @OneToMany(mappedBy="company")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="company")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="company")
    private List<Users> UsersList = new ArrayList<>();

    @OneToMany(mappedBy="company")
    private List<Stock> stockList = new ArrayList<>();

    @Override
    public String toString() {
        return "Company{" +
                "id=" + id +
                ", company='" + company + '\'' +
                ", city='" + city + '\'' +
                ", street='" + street + '\'' +
                ", post_code='" + post_code + '\'' +
                ", country='" + country + '\'' +
                ", europen_union='" + europen_union + '\'' +
                '}';
    }

}
