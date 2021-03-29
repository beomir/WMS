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
    private boolean status;

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

    public WorkDetails(Long id, Long workNumber, String workDescription, String workType,String handle, Long hdNumber, Long piecesQty, boolean status, String created, String last_update, String changeBy) {
        this.id = id;
        this.workNumber = workNumber;
        this.workDescription = workDescription;
        this.workType = workType;
        this.handle = handle;
        this.hdNumber = hdNumber;
        this.piecesQty = piecesQty;
        this.status = status;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
    }

}
