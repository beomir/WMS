package pl.coderslab.cls_wms_app.entity;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@NoArgsConstructor
@Entity
public class Location {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String locationName;

    private String locationDesc;
    private String locationType;

    @NotNull
    @ManyToOne
    private Warehouse warehouse;

    @NotNull
    @ManyToOne
    private StorageZone storageZone;

    private String created;
    private String last_update;

    private boolean active;
    private String changeBy;

    private boolean multiItem;
    private boolean hdControl;

    private int height;
    private int width;
    private int depth;

    private int volume;
    private double maxWeight;



    public Location(Long id, String locationName, String locationDesc, String locationType, Warehouse warehouse, StorageZone storageZone, String created, String last_update, boolean active, String changeBy,boolean multiItem,boolean hdControl,int height,int width,int depth, int volume, double maxWeight) {
        this.id = id;
        this.locationName = locationName;
        this.locationDesc = locationDesc;
        this.locationType = locationType;
        this.warehouse = warehouse;
        this.storageZone = storageZone;
        this.created = created;
        this.last_update = last_update;
        this.active = active;
        this.changeBy = changeBy;
        this.multiItem = multiItem;
        this.hdControl = hdControl;
        this.height = height;
        this.width = width;
        this.depth = depth;
        this.volume = volume;
        this.maxWeight = maxWeight;
    }

    @Override
    public String toString() {
        return "Location{" +
                "id=" + id +
                ", locationName='" + locationName + '\'' +
                ", locationDesc='" + locationDesc + '\'' +
                ", locationType='" + locationType + '\'' +
                ", warehouse=" + warehouse +
                ", storageZone=" + storageZone +
                ", created='" + created + '\'' +
                ", last_update='" + last_update + '\'' +
                ", active=" + active +
                ", changeBy='" + changeBy + '\'' +
                ", multiItem='" + multiItem + '\'' +
                ", hdControl='" + hdControl + '\'' +
                ", height='" + height + '\'' +
                ", width='" + width + '\'' +
                ", depth='" + depth + '\'' +
                ", volume='" + volume + '\'' +
                ", maxWeight='" + maxWeight + '\'' +
                '}';
    }
}
