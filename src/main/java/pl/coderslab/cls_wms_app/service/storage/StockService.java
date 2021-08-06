package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;
import pl.coderslab.cls_wms_app.temporaryObjects.ChosenStockPositional;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface StockService {


    List<Stock> getStorage(String warehouseName,String username);

    List<Warehouse> getWarehouse(Long id);

    Stock findById(Long id);

    void add(Stock stock);

    void addNewStock(Stock stock,String locationName);

    void changeStatus(Stock stock, String newStatus);

    void changeArticleNumber(Stock stock, ChosenStockPositional chosenStockPositional);

    void changeQty(Stock stock, String newQuantity);

    void changeQuality(Stock stock, String newQuality);

    void changeUnit(Stock stock, String newUnit);

    void changeComment(Stock stock, String newComment);

    Stock get(Long id);

    void delete(Long id);

    void update(Stock stock);

    void sendStock(String company);

    void remove(Long id);

    void transfer(Stock stock, String locationName, Stock chosenStockPositional);

    void produceGoods(Long productionNumberToConfirm) throws CloneNotSupportedException;
}

