package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class WareohuseServiceImpl implements WarehouseService{
    private final WarehouseRepository warehouseRepository;

    @Autowired
    public WareohuseServiceImpl(WarehouseRepository warehouseRepository) {
        this.warehouseRepository = warehouseRepository;
    }

    @Override
    public void add(Warehouse warehouse) {
        warehouseRepository.save(warehouse);
    }

    @Override
    public List<Warehouse> getWarehouse() {
        return warehouseRepository.getWarehouse();
    }

    @Override
    public List<Warehouse> getDeactivatedWarehouse() {
        return warehouseRepository.getDeactivatedWarehouse();
    }

    @Override
    public List<Warehouse> getWarehouse(Long id) {
        return warehouseRepository.getWarehouse(id);
    }

    @Override
    public Warehouse getOneWarehouse(Long id) {
        return warehouseRepository.getOneWarehouse(id);
    }

    @Override
    public Warehouse findById(Long id) {
        return warehouseRepository.getOne(id);
    }

    @Override
    public Warehouse get(Long id) {
        return warehouseRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        Warehouse warehouse = warehouseRepository.getOne(id);
        warehouse.setActive(false);
        warehouse.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        warehouse.setChangeBy(SecurityUtils.usernameForActivations());
        warehouseRepository.save(warehouse);
    }

    @Override
    public void activate(Long id) {
        Warehouse warehouse = warehouseRepository.getOne(id);
        warehouse.setActive(true);
        warehouse.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        warehouse.setChangeBy(SecurityUtils.usernameForActivations());
        warehouseRepository.save(warehouse);
    }

}
