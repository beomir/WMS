package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

public interface WarehouseService {

    void add(Warehouse warehouse);

    List<Warehouse> getWarehouse();

    List<Warehouse> getDeactivatedWarehouse();

    List<Warehouse> getWarehouse(Long id);

    Warehouse getOneWarehouse(Long id);

    Warehouse findById(Long id);

    Warehouse get(Long id);

    void delete(Long id);

    void activate(Long id);

}
