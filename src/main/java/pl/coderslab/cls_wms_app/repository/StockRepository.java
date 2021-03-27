package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;


import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("Select distinct b from Stock b join fetch b.company c join fetch b.article a join fetch b.status s join fetch b.warehouse w JOIN fetch Users u on u.company = c.name where w.id =?1 and u.username like ?2 order by b.location.locationName,c.name, b.hd_number")
    List<Stock> getStorage(Long id, String username);

    @Query("Select distinct w from Warehouse w where w.id =?1")
    List<Warehouse> getWarehouse(Long id);

    @Query("Select distinct s from Stock s left outer join fetch Reception r on s.receptionNumber = r.receptionNumber where s.company.name = ?1")
    List<Stock> getStockByCompanyName(String name);

    @Query(value="Select count(*) from storage where hd_number = ?1",nativeQuery = true)
    int checkIfHdNumberExistsOnStock(Long hd_number);

    @Query(value = "select s.id from storage s  join article a on (a.id = s.article_id) join status st on (st.id = s.status_id) join warehouse w on (w.id = s.warehouse_id) where a.article_number = ?1 and st.status = 'on_hand' and w.name = ?2 order by s.pieces_qty desc limit 1",nativeQuery = true)
    Long searchStockToSend(Long articleNumber, String warehouseName);

    @Query("Select s from Stock s where s.id = ?1")
    Stock getStockById(Long id);
//TODO think about List
    @Query("Select s from Stock s where s.location.locationName = ?1 ")
    Stock getStockByLocationName(String locationName);


    @Query("Select s from Stock s where s.shipmentNumber = ?1")
    List<Stock> getStockListByShipmentNumber(Long shipmentNumber);

    @Query("Select s from Stock s where s.hd_number = ?1")
    Stock getStockByHdNumber(Long hdNumber);

}
