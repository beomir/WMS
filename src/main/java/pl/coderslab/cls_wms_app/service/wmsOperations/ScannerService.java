package pl.coderslab.cls_wms_app.service.wmsOperations;


import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Stock;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface ScannerService {

    List<Stock> locationPreview(String locationName,String companyName, String warehouseName);

    boolean locationsForArticleFound(String locationName, String articleNumber, String inHowManyLocations, HttpSession session, Company company);

    List<Stock> findArticleInNearbyLocations(String locationName,String articleNumberInLocations,String inHowManyLocations,HttpSession session,Company company,String warehouseName);
}
