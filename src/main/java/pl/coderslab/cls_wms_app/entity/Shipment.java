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

@Table(name="shipments")
public class Shipment {

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

    private Long hd_number;
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

    private boolean closed;

    public Shipment(Long id, Article article, Company company, Long pieces_qty, Unit unit, Long hd_number, String quality, Customer customer, ShipMethod shipMethod,Warehouse warehouse, boolean closed,Long shipmentNumber) {
        this.id = id;
        this.article = article;
        this.company = company;
        this.pieces_qty = pieces_qty;
        this.unit = unit;
        this.hd_number = hd_number;
        this.quality = quality;
        this.customer = customer;
        this.shipMethod = shipMethod;
        this.warehouse = warehouse;
        this.closed = closed;
        this.shipmentNumber = shipmentNumber;
    }
}
