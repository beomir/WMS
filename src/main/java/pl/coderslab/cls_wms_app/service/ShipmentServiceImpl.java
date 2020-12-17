package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.repository.ShipmentRepository;

import java.util.List;
import java.util.Map;

@Service
public class ShipmentServiceImpl implements ShipmentService{
    private ShipmentRepository shipmentRepository;

    @Autowired
    public ShipmentServiceImpl(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    @Override
    public void add(Shipment shipment) {
        shipmentRepository.save(shipment);
    }

    @Override
    public List<Shipment> getShipment(Long id, String username) {
        return shipmentRepository.getShipment(id,username);
    }

    @Override
    public List<Shipment> getShipments() {
        return shipmentRepository.getShipmenta();
    }

    @Override
    public Shipment findById(Long id) {
        return shipmentRepository.getOne(id);
    }

    @Override
    public void finishShipment(Long shipmentNbrtoFinish) {
        shipmentRepository.updateFinishedShipmentValue(shipmentNbrtoFinish);
        shipmentRepository.deleteStockAfterFinishShipment(shipmentNbrtoFinish);
    }

    @Override
    public int checkHowManyNotfinishedShipments(Long id, String username) {
        return shipmentRepository.checkHowManyNotfinishedShipments(id,username);
    }

    @Override
    public Map<String, Integer> surveyMap(Long id, String username) {
        return shipmentRepository.surveyMap(id,username);
    }


}
