package pl.coderslab.cls_wms_app.entity;


import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
public class Extremely {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String extremelyName;
    private String extremelyDesc;
    private String extremelyValue;
    private String created;

    private String last_update;
    private String changeBy;

    @NotNull
    @ManyToOne
    private Company company;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    public Extremely(Long id, String extremelyName, String extremelyDesc, String extremelyValue, String created, String last_update, String changeBy,Company company, Warehouse warehouse) {
        this.id = id;
        this.extremelyName = extremelyName;
        this.extremelyDesc = extremelyDesc;
        this.extremelyValue = extremelyValue;
        this.created = created;
        this.last_update = last_update;
        this.changeBy = changeBy;
        this.company = company;
        this.warehouse = warehouse;
    }
}
