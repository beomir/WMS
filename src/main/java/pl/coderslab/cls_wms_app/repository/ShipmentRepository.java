package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("Select s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w where w.id =?1 ")
    List<Shipment> getShipment(Long id);

    @Query("Select shi from Shipment shi join fetch shi.article a join fetch shi.shipMethod sh join fetch shi.warehouse ")
    List<Shipment> getShipmenta();

}
