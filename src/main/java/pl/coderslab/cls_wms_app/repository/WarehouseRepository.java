package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

@Repository
public interface WarehouseRepository extends JpaRepository<Warehouse, Long> {

    @Query("Select w from Warehouse w where w.active = true")
    List<Warehouse> getWarehouse();

    @Query("Select w from Warehouse w where w.active = false")
    List<Warehouse> getDeactivatedWarehouse();

    @Query("Select distinct w from Warehouse w where w.id =?1 order by w.name")
    List<Warehouse> getWarehouse(Long id);

}
