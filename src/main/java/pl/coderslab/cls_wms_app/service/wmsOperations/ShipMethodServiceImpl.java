package pl.coderslab.cls_wms_app.service.wmsOperations;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.repository.ShipMethodRepository;

import java.util.List;

@Service
public class ShipMethodServiceImpl implements ShipMethodService{
    private final ShipMethodRepository shipMethodRepository;

    @Autowired
    public ShipMethodServiceImpl(ShipMethodRepository shipMethodRepository) {
        this.shipMethodRepository = shipMethodRepository;
    }

    @Override
    public void add(ShipMethod shipMethod) {
        shipMethodRepository.save(shipMethod);
    }

    @Override
    public List<ShipMethod> getShipMethod() {
        return shipMethodRepository.getShipMethod();
    }

}
