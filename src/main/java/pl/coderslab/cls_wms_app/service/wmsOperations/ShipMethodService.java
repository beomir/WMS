package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.ShipMethod;

import java.util.List;

public interface ShipMethodService {

    void add(ShipMethod shipMethod);

    List<ShipMethod> getShipMethod();


}
