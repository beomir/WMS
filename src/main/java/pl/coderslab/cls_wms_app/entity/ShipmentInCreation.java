package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class ShipmentInCreation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Article article;

    @NotNull
    @ManyToOne
    private Company company;

    private Long shipmentNumber;

    private Long pieces_qty;

    @NotNull
    @ManyToOne
    private Unit unit;

    private String quality;

    @NotNull
    @ManyToOne
    private Customer customer;

    @NotNull
    @ManyToOne
    private ShipMethod shipMethod;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private boolean creation_closed;


    @NotNull
    @ManyToOne
    private Status status;

    public ShipmentInCreation(Long id, Article article, Company company, Long pieces_qty, Unit unit,  String quality, Customer customer, ShipMethod shipMethod,Warehouse warehouse, boolean creation_closed,Long shipmentNumber, Status status) {
        this.id = id;
        this.article = article;
        this.company = company;
        this.pieces_qty = pieces_qty;
        this.unit = unit;
        this.quality = quality;
        this.customer = customer;
        this.shipMethod = shipMethod;
        this.warehouse = warehouse;
        this.creation_closed = creation_closed;
        this.shipmentNumber = shipmentNumber;
        this.status = status;
    }
}
