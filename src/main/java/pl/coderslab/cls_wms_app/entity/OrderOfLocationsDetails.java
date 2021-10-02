package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
public class OrderOfLocationsDetails {


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
    @ManyToOne
    private Location location;

    @NotNull
    @ManyToOne
    private OrderOfLocationsHeader orderOfLocationsHeader;

    private String created;
    private String last_update;
    private String changeBy;

    @NotNull
    @Column(unique = true)
    private Long locationNumberInSequence;

    public OrderOfLocationsDetails(Long id, Warehouse warehouse, Company company, Location location, OrderOfLocationsHeader orderOfLocationsHeader, String created, String last_update, String changeBy,Long locationNumberInSequence) {
        this.id = id;
        this.warehouse = warehouse;
        this.company = company;
        this.location = location;
        this.orderOfLocationsHeader = orderOfLocationsHeader;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.locationNumberInSequence = locationNumberInSequence;
    }
}
