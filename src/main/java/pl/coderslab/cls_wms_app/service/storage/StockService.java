package pl.coderslab.cls_wms_app.service.storage;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import javax.servlet.http.HttpSession;
import java.util.List;


public interface StockService {


    List<Stock> getStorage(String warehouseName,String username);

    List<Warehouse> getWarehouse(Long id);

    Stock findById(Long id);

    void add(Stock stock);

    void addNewStock(Stock stock,String locationName,HttpSession session);

    void changeStatus(Stock stock, String newStatus,HttpSession session);

    void changeArticleNumber(Stock stock, String newArticleNumber,HttpSession session);

    void changeQty(Stock stock, String newQuantity,HttpSession session);

    void changeQuality(Stock stock, String newQuality,HttpSession session);

    void changeUnit(Stock stock, String newUnit,HttpSession session);

    void changeComment(Stock stock, String newComment,HttpSession session);

    Stock get(Long id);

    void delete(Long id);

    void update(Stock stock);

    void sendStock(String company);

    void remove(Long id);

    boolean transfer(Stock stock, String locationName, Stock chosenStockPositional,HttpSession session) throws CloneNotSupportedException;

    void produceGoods(Long productionNumberToConfirm) throws CloneNotSupportedException;
}

