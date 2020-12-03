package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.ShipMethod;

import java.util.List;

@Repository
public interface ShipMethodRepository extends JpaRepository<ShipMethod, Long> {

    @Query("Select sm from ShipMethod sm")
    List<ShipMethod> getShipMethod();

}
