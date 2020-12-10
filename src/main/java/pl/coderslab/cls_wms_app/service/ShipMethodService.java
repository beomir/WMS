package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.ShipMethod;

import java.util.List;

public interface ShipMethodService {

    void add(ShipMethod shipMmethod);

    List<ShipMethod> getShipMethod();


}
