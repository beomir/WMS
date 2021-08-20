package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Shipment;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ShipmentService {

    void add(Shipment shipment);


    List<Shipment> getShipments();

    Shipment findById(Long id);

    List<Shipment> getShipmentsForLoggedUser(String warehouseName, String username);

    void finishShipment(Long id) throws IOException, MessagingException;

    int checkHowManyNotFinishedShipments(String warehouseName,String username);

    Map<String,Integer> surveyMap(Long id, String username);

    void sentShipments(String company);

}
