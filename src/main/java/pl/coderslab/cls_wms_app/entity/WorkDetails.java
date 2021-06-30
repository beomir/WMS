package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class WorkDetails {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workNumber;

    private String workDescription;
    private String workType;
    private String handle;
    private Long hdNumber;
    private Long piecesQty;
    private String status;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private String created;

    private String last_update;
    private String changeBy;

    @NotNull
    @ManyToOne
    private Article article;


    @NotNull
    @ManyToOne
    private Location fromLocation;

    @NotNull
    @ManyToOne
    private Location toLocation;



}
