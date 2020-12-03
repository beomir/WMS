package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;

public interface ShipMethodService {

    void add(ShipMethod shipMmethod);

    List<ShipMethod> getShipMethod();

    ShipMethod findById(Long id);


    ShipMethod get(Long id);

    void delete(Long id);

    void update(ShipMethod shipMethod);
}
