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
public class Production {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long productionNumber;

    private Long finishProductNumber;
    private Long finishProductPieces;
    private Long intermediateArticleNumber;
    private Long intermediateArticlePieces;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private String created;
    private String last_update;
    private String changeBy;
    private String status;

    public Production(Long id,Long productionNumber, Long finishProductNumber, Long finishProductPieces, Long intermediateArticleNumber, Long intermediateArticlePieces, Company company, Warehouse warehouse, String created, String last_update, String changeBy, String status) {
        this.id = id;
        this.productionNumber = productionNumber;
        this.finishProductNumber = finishProductNumber;
        this.finishProductPieces = finishProductPieces;
        this.intermediateArticleNumber = intermediateArticleNumber;
        this.intermediateArticlePieces = intermediateArticlePieces;
        this.company = company;
        this.warehouse = warehouse;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.status = status;
    }

}
