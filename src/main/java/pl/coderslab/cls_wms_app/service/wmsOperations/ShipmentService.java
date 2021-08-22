package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;
import pl.coderslab.cls_wms_app.repository.ShipmentRepository;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface ShipmentService {

    void add(Shipment shipment);


    List<Shipment> getShipments();

    Shipment findById(Long id);

    List<Shipment> getShipmentsForLoggedUser(String warehouseName, String username);

    List<Shipment> getShipmentsForLoggedUserByShipmentNumber(String warehouseName, String username,Long shipmentNumber);

    void finishShipment(Long id) throws IOException, MessagingException;

    int checkHowManyNotFinishedShipments(String warehouseName,String username);

    Map<String,Integer> surveyMap(Long id, String username);

    void sentShipments(String company);

    void assignDoorLocationToShipment(Long shipmentNumber, Long doorLocation, HttpSession session);

    List<ShipmentRepository.ShipmentViewObject>shipmentSummary(String shipmentCompany, String shipmentWarehouse,
                                                               String shipmentCustomer, String shipmentStatus, String shipmentLocation,
                                                               String shipmentShipmentNumber, String shipmentHdNumber,
                                                               String shipmentCreatedFrom, String shipmentCreatedTo, String shipmentCreatedBy);

}
