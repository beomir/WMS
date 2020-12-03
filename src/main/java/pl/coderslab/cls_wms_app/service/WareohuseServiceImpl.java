package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.WarehouseRepository;

import java.util.List;

@Service
public class WareohuseServiceImpl implements WarehouseService{
    private WarehouseRepository warehouseRepository;

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
    public List<Warehouse> getWarehouse(Long id) {
        return warehouseRepository.getWarehouse(id);
    }

    @Override
    public Warehouse findById(Long id) {
        return null;
    }

    @Override
    public Warehouse get(Long id) {
        return warehouseRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Warehouse warehouse) {

    }
}
