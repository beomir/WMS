package pl.coderslab.cls_wms_app.service.wmsValues;

import pl.coderslab.cls_wms_app.entity.Status;

import java.util.List;

public interface StatusService {

    void add(Status status);

    List<Status> getStatus();

    List<Status> getStockStatuses();

}
