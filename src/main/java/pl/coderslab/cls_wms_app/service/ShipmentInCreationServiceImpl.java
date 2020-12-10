package pl.coderslab.cls_wms_app.service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.SessionAttribute;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.ShipmentInCreation;
import pl.coderslab.cls_wms_app.repository.ShipmentInCreationRepository;

import java.util.List;

@Service
@Slf4j
public class ShipmentInCreationServiceImpl implements ShipmentInCreationService{
    private ShipmentInCreationRepository shipmentInCreationRepository;

    @Autowired
    public ShipmentInCreationServiceImpl(ShipmentInCreationRepository shipmentInCreationRepository) {
        this.shipmentInCreationRepository = shipmentInCreationRepository;
    }

    @Override
    public void addShipmentInCreation(ShipmentInCreation shipmentInCreation) {
        shipmentInCreationRepository.save(shipmentInCreation);
    }

    @Override
    public List<ShipmentInCreation> getShipmentInCreationById(Long id) {
        return shipmentInCreationRepository.getShipmentInCreationById(id);
    }

    @Override
    public List<ShipmentInCreation> getShipmentInCreation() {
        return shipmentInCreationRepository.getShipmentInCreation();
    }

    @Override
    public ShipmentInCreation findById(Long id) {
        return shipmentInCreationRepository.getOne(id);
    }


    @Override
    public int qtyOfOpenedShipmentsInCreation(Long id, String username) {
        return shipmentInCreationRepository.qtyOfOpenedShipmentsInCreation(id, username);
    }

    @Override
    public Long lastShipment() {
        return shipmentInCreationRepository.lastShipment();
    }

    @Override
    public List<ShipmentInCreation> openedShipments(Long id, String username) {
        return shipmentInCreationRepository.openedShipments(id, username);
    }

    @Override
    public List<Long> stockDifference(Long id, String username) {
        return shipmentInCreationRepository.stockDifference(id, username);
    }

    @Override
    public List<Long> stockDifferenceQty(Long id, String username) {
        return shipmentInCreationRepository.stockDifferenceQty(id, username);
    }

    @Override
    public List<Long> shipmentCreationSummary(Long id, String username) {
        return shipmentInCreationRepository.shipmentCreationSummary(id, username);
    }


    @Override
    public int getCreatedShipmentById(Long shipmentNbr) {
        return shipmentInCreationRepository.getCreatedShipmentById(shipmentNbr);
    }

    @Override
    public void updateCloseCreationShipmentValue(Long shipmentNbr) {
        if(getCreatedShipmentById(shipmentNbr)>0){
            shipmentInCreationRepository.updateCloseCreationShipmentValue(shipmentNbr);
        }
    }


    @Override
    public void closeCreationShipment(Long id, @SessionAttribute Long warehouseId) {
        ShipmentInCreation shipmentInCreation = shipmentInCreationRepository.getOne(id);
        String failedAttempt = "Change red underlined qty to pass the validation";
        String attemptSuccessful = "Validation passed succesfully. Your goods were transfer to shipment, status of marked pieces on stock were changed ";
        if(validateTheCorrectnessOfShipment(warehouseId)) {
            shipmentInCreation.setCreation_closed(true);
            Long shipmentNmbr = shipmentInCreationRepository.getOne(id).getShipmentNumber();
            shipmentInCreationRepository.updateCloseCreationShipmentValue(shipmentNmbr);
            shipmentInCreationRepository.insertDataToShipmentAfterCloseCreation(warehouseId,SecurityUtils.username());
            shipmentInCreationRepository.insertDataToStockAfterCloseCreationWithCorrectStatus(warehouseId,SecurityUtils.username());
            shipmentInCreationRepository.updateStockDataAboutShipmentQty(warehouseId,SecurityUtils.username());
            shipmentInCreationRepository.deleteQtyAfterCloseCreation(warehouseId,SecurityUtils.username());

            resultOfShipmentCreationValidation(attemptSuccessful);
        }
        else{
            resultOfShipmentCreationValidation(failedAttempt);
            System.out.println("check failedAttempt: " + failedAttempt);
        }

    }


    @Override
    public boolean validateTheCorrectnessOfShipment(@SessionAttribute Long warehouseId) {
        List<Long> checkShipments = shipmentInCreationRepository.stockDifferenceQty(warehouseId, SecurityUtils.username());
        boolean shipmentReadyToCloseCreation = true;
        for (int i = 0; i < checkShipments.size(); i++) {
            log.debug(String.valueOf(checkShipments.get(i)));
            System.out.println(checkShipments.get(i));
            if (checkShipments.get(i) < 0) {
                shipmentReadyToCloseCreation = false;
            }
        }
        log.debug(String.valueOf(shipmentReadyToCloseCreation));
        System.out.println(shipmentReadyToCloseCreation);
        return shipmentReadyToCloseCreation;
    }

    @Override
    public String resultOfShipmentCreationValidation(String message){
        String messageValidation = message;
        log.debug(message);
        System.out.println("message!: " + message);
        return messageValidation;
    }

    @Override
    public void remove(Long id) {
        shipmentInCreationRepository.deleteById(id);
    }

}
