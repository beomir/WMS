package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
public class IntermediateArticle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "article_id", unique=true)
    private Article article;

    @ManyToMany
    List<ProductionArticle> productionArticle = new ArrayList<>();

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

    private String productionArticleType; // intermediate or finished product
    private Long quantityForFinishedProduct;

    private String created;
    private String last_update;
    private String changeBy;

    public IntermediateArticle(Long id,Article article, StorageZone storageZone,List<ProductionArticle> productionArticle, Warehouse warehouse, Company company, Location location, String productionArticleType, Long quantityForFinishedProduct, String created, String last_update, String changeBy) {
        this.id = id;
        this.storageZone = storageZone;
        this.article = article;
        this.warehouse = warehouse;
        this.company = company;
        this.location = location;
        this.productionArticleType = productionArticleType;
        this.quantityForFinishedProduct = quantityForFinishedProduct;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.productionArticle = productionArticle;
    }
}
