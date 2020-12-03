package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.Warehouse;


import java.util.List;

@Repository
public interface StockRepository extends JpaRepository<Stock, Long> {

    @Query("Select b from Stock b join fetch b.company c join fetch b.article a join fetch b.status s join fetch b.warehouse w where w.id =?1")
    List<Stock> getStorage(Long id);

    @Query("Select distinct w from Warehouse w where w.id =?1")
    List<Warehouse> getWarehouse(Long id);
}
