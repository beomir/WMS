package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;
import java.util.Map;

public interface ShipmentService {

    void add(Shipment shipment);

    List<Shipment> getShipment(Long id, String username);

    List<Shipment> getShipments();

    Shipment findById(Long id);


    void finishShipment(Long id);

    int checkHowManyNotfinishedShipments(Long id,String username);

    Map<String,Integer> surveyMap(Long id, String username);
}
