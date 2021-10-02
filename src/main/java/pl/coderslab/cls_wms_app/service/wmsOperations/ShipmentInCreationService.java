package pl.coderslab.cls_wms_app.service.wmsOperations;

import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.entity.ShipmentInCreation;

import java.util.List;

public interface ShipmentInCreationService {

    void addShipmentInCreation(ShipmentInCreation shipmentInCreation);

    void editShipmentInCreation(ShipmentInCreation shipmentInCreation);

    List<ShipmentInCreation> getShipmentsListForLoggedUser(String warehouseName,String userName);

    List<ShipmentInCreation> getShipmentInCreation();

    ShipmentInCreation findById(Long id);

    int qtyOfOpenedShipmentsInCreation(String warehouseName, String username);

    Long lastShipment();

    List<ShipmentInCreation> openedShipments(String warehouseName, String username);

    List<Long> stockDifference(String warehouseName, String username);

    List<Long> stockDifferenceQty(String warehouseName, String username);

    List<Long> shipmentCreationSummary(String warehouseName, String username);


    int getCreatedShipmentById(Long shipmentNbr);

    void updateCloseCreationShipmentValue(Long shipmentNbr);

    void closeCreationShipment(Long shipmentNumber,String chosenWarehouse);

    Boolean validateTheCorrectnessOfShipment(@SessionAttribute String chosenWarehouse);

    void remove(Long id);

    String resultOfShipmentCreationValidation(String chosenWarehouse);


//    void deleteZerosOnStock(@SessionAttribute Long warehouseId,String username);
}
