package pl.coderslab.cls_wms_app.entity;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Company company;

    private String city;
    private String street;
    private String post_code;
    private String country;
    private boolean europen_union;

    public Vendor(Long id, Company company, String city, String street, String post_code, String country, boolean europen_union) {
        this.id = id;
        this.company = company;
        this.city = city;
        this.street = street;
        this.post_code = post_code;
        this.country = country;
        this.europen_union = europen_union;
    }
}

