package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;

public interface ShipmentService {

    void add(Shipment shipment);

    List<Shipment> getShipment(Long id);

    List<Shipment> getShipments();

    Shipment findById(Long id);


    Shipment get(Long id);

    void delete(Long id);

    void update(Shipment shipment);
}
