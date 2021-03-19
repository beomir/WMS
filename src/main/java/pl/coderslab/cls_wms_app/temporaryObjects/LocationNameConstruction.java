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
    public String secondSepRackTo;
    public String thirdSepRack;
    public String thirdSepRackTo;
    public String fourthSepRack;
    public String fourthSepRackTo;

    public String firstSepFloor;
    public String secondSepFloor;
    public String secondSepFloorTo;

    public String firstSepDoor;
    public String secondSepDoor;
    public String thirdSepDoor;
    public String thirdSepDoorTo;

    public String message ="";


}
