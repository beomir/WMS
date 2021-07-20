package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;

import java.util.List;


public interface StockService {


    List<Stock> getStorage(String warehouseName,String username);

    List<Warehouse> getWarehouse(Long id);

    Stock findById(Long id);

    void add(Stock stock);

    void addNewStock(Stock stock,String locationName);

    void changeStatus(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeArticleNumber(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeQty(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeQuality(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeUnit(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeComment(Stock stock, ChosenStockPositional chosenStockPositional);

    Stock get(Long id);

    void delete(Long id);

    void update(Stock stock);

    void sendStock(String company);

    void remove(Long id);

    void transfer(Stock stock, String locationName, ChosenStockPositional chosenStockPositional);

    void produceGoods(Long productionNumberToConfirm) throws CloneNotSupportedException;
}

