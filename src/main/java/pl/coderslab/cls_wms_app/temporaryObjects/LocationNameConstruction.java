package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class LocationNameConstruction {
    public String firstSepStock;
    public String secondSepStock;
    public String thirdSepStock;
    public String fourthSepStock;

    public String firstSepDoor;
    public String secondSepDoor;
    public String thirdSepDoor;

}
