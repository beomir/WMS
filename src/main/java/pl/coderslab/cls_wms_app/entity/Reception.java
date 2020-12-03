package pl.coderslab.cls_wms_app.entity;


import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "receptions")
public class Reception {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Article article;

    @NotNull
    @ManyToOne
    private Company company;

    private Long receptionNumber;

    private Long pieces_qty;
    private Long cartons_qty;
    private Long hd_number;
    private String quality;

    @NotNull
    @ManyToOne
    private Vendor vendor;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private boolean closed;

    public Reception(Long id, Article article, Company company,  Long pieces_qty, Long cartons_qty, Long hd_number, String quality, Vendor vendor, Warehouse warehouse, boolean closed,Long receptionNumber) {
        this.id = id;
        this.article = article;
        this.company = company;

        this.pieces_qty = pieces_qty;
        this.cartons_qty = cartons_qty;
        this.hd_number = hd_number;
        this.quality = quality;
        this.vendor = vendor;
        this.warehouse = warehouse;
        this.closed = closed;
        this.receptionNumber = receptionNumber;
    }
}
