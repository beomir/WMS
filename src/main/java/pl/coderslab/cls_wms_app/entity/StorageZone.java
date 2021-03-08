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
public class StorageZone {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String storageZoneName;
    private String storageZoneDesc;
    private String storageZoneType;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    private String created;
    private String last_update;
    private boolean active;
    private String changeBy;

    @OneToMany(mappedBy="storageZone")
    private List<Location> locationsList = new ArrayList<>();

    public StorageZone(Long id, String storageZoneName, String storageZoneDesc, String storageZoneType, Warehouse warehouse, String created, String last_update, boolean active, String changeBy) {
        this.id = id;
        this.storageZoneName = storageZoneName;
        this.storageZoneDesc = storageZoneDesc;
        this.storageZoneType = storageZoneType;
        this.warehouse = warehouse;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
    }
}
