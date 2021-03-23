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

    private double height;
    private double width;
    private double depth;

    private double volume;

    private double maxWeight;
    private double freeSpace;
    private double freeWeight;



    public Location(Long id, String locationName, String locationDesc, String locationType, Warehouse warehouse, StorageZone storageZone, String created, String last_update, boolean active, String changeBy,boolean multiItem,boolean hdControl,double height,double width,double depth, double volume, double maxWeight,double freeSpace,double freeWeight) {
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
        this.freeSpace = freeSpace;
        this.freeWeight = freeWeight;
    }

    @OneToMany(mappedBy="location")
    public List<Stock> stockList = new ArrayList<>();

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
                ", freeSpace='" + freeSpace + '\'' +
                ", freeWeight='" + freeWeight + '\'' +
                '}';
    }
}
