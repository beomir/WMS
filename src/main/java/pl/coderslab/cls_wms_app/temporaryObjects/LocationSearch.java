package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class LocationSearch {
    public String warehouse;
    public String storageZoneName;
    public String locationType;
    public String locationName;
}
