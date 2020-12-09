package pl.coderslab.cls_wms_app.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
@Table(name="ship_methods")
public class ShipMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;
    private String method_desc;

    @OneToMany(mappedBy="shipMethod")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="shipMethod")
    private List<ShipmentInCreation> ShipmentInCreationList = new ArrayList<>();

    public ShipMethod(Long id, String method, String method_desc) {
        this.id = id;
        this.method = method;
        this.method_desc = method_desc;
    }
}
