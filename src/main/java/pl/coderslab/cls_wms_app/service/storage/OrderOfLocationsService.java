package pl.coderslab.cls_wms_app.service.storage;


import pl.coderslab.cls_wms_app.entity.OrderOfLocationsDetails;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsHeader;

import java.util.List;

public interface OrderOfLocationsService {

    void addDetails(OrderOfLocationsDetails orderOfLocationsDetails);

    void addHeader(OrderOfLocationsHeader orderOfLocationsHeader);

    void deactivate(Long id);

    void activate(Long id);

    void remove(Long id);

    List<OrderOfLocationsHeader> orderOfLocationsHeaderList();

}
