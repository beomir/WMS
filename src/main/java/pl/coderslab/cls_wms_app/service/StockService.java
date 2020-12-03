package pl.coderslab.cls_wms_app.service;

import org.springframework.data.jpa.repository.Query;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;


import java.util.List;


public interface StockService {


    List<Stock> getStorage(Long id);

    List<Warehouse> getWarehouse(Long id);

    Stock findById(Long id);

    void add(Stock stock);

    Stock get(Long id);

    void delete(Long id);

    void update(Stock stock);
}

