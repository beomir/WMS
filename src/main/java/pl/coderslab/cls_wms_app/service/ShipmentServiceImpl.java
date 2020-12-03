package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.repository.ShipmentRepository;

import java.util.List;

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
    public List<Shipment> getShipment(Long id) {
        return shipmentRepository.getShipment(id);
    }

    @Override
    public List<Shipment> getShipments() {
        return shipmentRepository.getShipmenta();
    }

    @Override
    public Shipment findById(Long id) {
        return null;
    }

    @Override
    public Shipment get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Shipment shipment) {

    }
}
