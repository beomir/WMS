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
public class ProductionArticle {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne
    @JoinColumn(name = "article_id", unique=true)
    private Article article;

    @ManyToOne
    private StorageZone storageZone; // storageZone to putaway after reception or start picking for make production

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    @NotNull
    @ManyToOne
    private Company company;

    @ManyToMany(mappedBy="productionArticle")
    List<IntermediateArticle> intermediateArticle = new ArrayList<>();

    @ManyToOne
    private Location location; // production location

    private String productionArticleType; // intermediate or finished product
    private String productionArticleConnection; // information about connection between intermediate articles and finished product

    private Long quantityForFinishedProduct;

    private String created;
    private String last_update;
    private String changeBy;

    public ProductionArticle(Long id, Article article, StorageZone storageZone, Warehouse warehouse, String created, String last_update, String changeBy, Company company,Location location, String productionArticleType,String productionArticleConnection,Long quantityForFinishedProduct) {
        this.id = id;
        this.article = article;
        this.storageZone = storageZone;
        this.warehouse = warehouse;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.company = company;
        this.location = location;
        this.productionArticleType = productionArticleType;
        this.productionArticleConnection = productionArticleConnection;
        this.quantityForFinishedProduct = quantityForFinishedProduct;
    }

    @Override
    public String toString() {
        return "ProductionArticle{" +
                "id=" + id +
                ", article=" + article +
                ", storageZone=" + storageZone +
                ", warehouse=" + warehouse +
                ", company=" + company +
                ", intermediateArticle=" + intermediateArticle +
                ", productionArticleType='" + productionArticleType + '\'' +
                ", productionArticleConnection='" + productionArticleConnection + '\'' +
                ", quantityForFinishedProduct=" + quantityForFinishedProduct +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                ", changeBy='" + changeBy + '\'' +
                '}';
    }
}
