package pl.coderslab.cls_wms_app.service.wmsOperations;


import pl.coderslab.cls_wms_app.entity.Stock;

import java.util.List;

public interface ScannerService {

    List<Stock> locationPreview(String locationName,String companyName, String warehouseName);
}
