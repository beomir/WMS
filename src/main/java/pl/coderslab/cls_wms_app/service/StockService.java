package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;


public interface StockService {


    List<Stock> getStorage(Long id,String username);

    List<Warehouse> getWarehouse(Long id);


    Stock findById(Long id);

    void add(Stock stock);

    void addNewStock(Stock stock);

    void changeStatus(Stock stock);

    void changeArticleNumber(Stock stock);

    void changeQty(Stock stock);

    void changeQuality(Stock stock);

    void changeUnit(Stock stock);

    void changeComment(Stock stock);

    Stock get(Long id);

    void delete(Long id);

    void update(Stock stock);

    void sendStock(String company);

    void remove(Long id);
}

