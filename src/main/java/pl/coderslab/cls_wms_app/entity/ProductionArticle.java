package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class ProductionArticle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne
    private Article article;

    @NotNull
    @ManyToOne
    private StorageZone storageZone; // storageZone to putaway after reception or start picking for make production

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Location location; // production location

    private String created;
    private String last_update;
    private String changeBy;

    public ProductionArticle(Long id, Article article, StorageZone storageZone, Warehouse warehouse, String created, String last_update, String changeBy, Company company,Location location) {
        this.id = id;
        this.article = article;
        this.storageZone = storageZone;
        this.warehouse = warehouse;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.company = company;
        this.location = location;
    }
}
