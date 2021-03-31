package pl.coderslab.cls_wms_app.temporaryObjects;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;



@Data
@NoArgsConstructor
@AllArgsConstructor
@Service
public class ChosenStockPositional {

    public Long idObj;
    public Long hd_numberObj;
    public String commentObj;
    public Long articleId;

    public Long pieces_qtyObj;
    public Long statusId;
    public String qualityObj;
    public Long shipmentNumberObj;

    public Long receptionNumberObj;
    public Long warehouseId;
    public String createdObj;
    public String last_updateObj;

    public String changeByObj;
    public Long companyId;
    public Long unitId;
    public Long locationId;


}
