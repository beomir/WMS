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

    public Article(Long id, Long article_number, String article_desc, String article_logistic_variant, Long pieces_per_carton, Company company, String created, String last_update) {
        this.id = id;
        this.article_number = article_number;
        this.article_desc = article_desc;
        this.article_logistic_variant = article_logistic_variant;
        this.pieces_per_carton = pieces_per_carton;
        this.company = company;
        this.created = created;
        this.last_update = last_update;
    }

    @OneToMany(mappedBy="article")
    private List<Stock> stockList = new ArrayList<>();

    @OneToMany(mappedBy="article")
    private List<Reception> receptionList = new ArrayList<>();

    @OneToMany(mappedBy="article")
    private List<Shipment> shipmentList = new ArrayList<>();
}
