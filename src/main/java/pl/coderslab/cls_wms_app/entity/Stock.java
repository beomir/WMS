package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity

@Table(name="storage")
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long hd_number;

    private String comment;

    @NotNull
    @ManyToOne
    private Article article;

    private Long pieces_qty;

    @NotNull
    @ManyToOne
    private Status status;

    private String quality;

    private Long shipmentNumber;

    private Long receptionNumber;

    public Stock(Long id, Long hd_number, Article article,  Long pieces_qty,Unit unit, Status status, String quality, Warehouse warehouse, String created, String last_update, Company company,String comment,Long shipmentNumber, Long receptionNumber,String changeBy) {
        this.id = id;
        this.hd_number = hd_number;
        this.article = article;
        this.pieces_qty = pieces_qty;
        this.unit = unit;
        this.status = status;
        this.quality = quality;
        this.warehouse = warehouse;
        this.created = created;
        this.last_update = last_update;
        this.company = company;
        this.comment = comment;
        this.shipmentNumber = shipmentNumber;
        this.receptionNumber = receptionNumber;
        this.changeBy = changeBy;
    }

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private String created;

    private String last_update;
    private String changeBy;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Unit unit;

    @Override
    public String toString() {
        return "Stock{" +
                "id=" + id +
                ", hd_number=" + hd_number +
                ", comment='" + comment + '\'' +
                ", article=" + article +
                ", pieces_qty=" + pieces_qty +
                ", status=" + status +
                ", quality='" + quality + '\'' +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                '}';
    }
}