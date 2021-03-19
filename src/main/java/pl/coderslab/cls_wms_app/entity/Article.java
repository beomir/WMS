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
@Table(name="article")
public class Article {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long article_number;

    private String article_desc;
    private String article_logistic_variant;
    private Long pieces_per_carton;

    @NotNull
    @ManyToOne
    private Company company;

    private String created;

    private String last_update;

    private boolean active;
    private String changeBy;

    private int height;
    private int width;
    private int depth;

    private int volume;
    private double weight;

    @NotNull
    @ManyToOne
    private ArticleTypes articleTypes;

    public Article(Long id, Long article_number, String article_desc, String article_logistic_variant, Long pieces_per_carton, Company company, String created, String last_update,boolean active,String changeBy,int height,int width,int depth, int volume, double weight,ArticleTypes articleTypes) {
        this.id = id;
        this.article_number = article_number;
        this.article_desc = article_desc;
        this.article_logistic_variant = article_logistic_variant;
        this.pieces_per_carton = pieces_per_carton;
        this.company = company;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.volume = volume;
        this.weight = weight;
        this.articleTypes = articleTypes;
    }

    @OneToMany(mappedBy="article")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="article")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="article")
    private List<Shipment> shipmentList = new ArrayList<>();

    @OneToMany(mappedBy="article")
    private List<ShipmentInCreation> ShipmentInCreationList = new ArrayList<>();

    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", article_number=" + article_number +
                ", article_desc='" + article_desc + '\'' +
                ", article_logistic_variant='" + article_logistic_variant + '\'' +
                ", pieces_per_carton=" + pieces_per_carton +
                ", company=" + company +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                ", active=" + active +
                ", changeBy=" + changeBy +
                ", height=" + height +
                ", width=" + width +
                ", depth=" + depth +
                ", volume=" + volume +
                '}';
    }
}
