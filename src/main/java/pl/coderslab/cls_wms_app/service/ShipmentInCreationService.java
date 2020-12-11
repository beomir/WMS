package pl.coderslab.cls_wms_app.service;

import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.ShipmentInCreation;

import java.util.List;

public interface ShipmentInCreationService {

    void addShipmentInCreation(ShipmentInCreation shipmentInCreation);

    List<ShipmentInCreation> getShipmentInCreationById(Long id);

    List<ShipmentInCreation> getShipmentInCreation();

    ShipmentInCreation findById(Long id);

    int qtyOfOpenedShipmentsInCreation(Long id, String username);

    Long lastShipment();

    List<ShipmentInCreation> openedShipments(Long id, String username);

    List<Long> stockDifference(Long id, String username);

    List<Long> stockDifferenceQty(Long id, String username);

    List<Long> shipmentCreationSummary(Long id, String username);


    int getCreatedShipmentById(Long shipmentNbr);

    void updateCloseCreationShipmentValue(Long shipmentNbr);

    void closeCreationShipment(Long id,@SessionAttribute Long warehouseId);

    Boolean validateTheCorrectnessOfShipment(@SessionAttribute Long warehouseId);

    void remove(Long id);

    String resultOfShipmentCreationValidation(Long id);

//    void deleteZerosOnStock(@SessionAttribute Long warehouseId,String username);
}
