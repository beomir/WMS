package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ReceptionSearch {
    public String warehouse;
    public String company;
    public String vendor;
    public String receptionNumber;
    public String hdNumber;
    public String status;
    public String location;
    public String createdBy;
    public String createdFrom;
    public String createdTo;
//TODO Create cleaning message after change view reception
    public String message;

}
