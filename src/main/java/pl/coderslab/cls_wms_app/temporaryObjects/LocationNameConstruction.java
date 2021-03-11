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
    public String firstSepRack;
    public String secondSepRack;
    public String thirdSepRack;
    public String fourthSepRack;

    public String firstSepFloor;
    public String secondSepFloor;

    public String firstSepDoor;
    public String secondSepDoor;
    public String thirdSepDoor;

    public String message;


}
