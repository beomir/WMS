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
public class OrderOfLocationsHeader {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @OneToMany(mappedBy="orderOfLocationsHeader")
    private List<OrderOfLocationsDetails> orderOfLocationsDetailsList = new ArrayList<>();

    private String orderName;

    private String created;
    private String last_update;

    private boolean active;
    private String changeBy;


    public OrderOfLocationsHeader(Long id, Warehouse warehouse, Company company, String orderName, String created, String last_update, boolean active, String changeBy) {
        this.id = id;
        this.warehouse = warehouse;
        this.company = company;
        this.orderName = orderName;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
    }
}
