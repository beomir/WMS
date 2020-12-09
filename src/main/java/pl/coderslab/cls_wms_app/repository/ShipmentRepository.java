package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w where w.id =?1 ")
    List<Shipment> getShipment(Long id);

    @Query("Select shi from Shipment shi join fetch shi.article a join fetch shi.shipMethod sh join fetch shi.warehouse ")
    List<Shipment> getShipmenta();

    @Modifying
    @Transactional
    @Query(value = "update shipments SET finished = true where shipment_number = ?1",nativeQuery = true)
    void updateFinishedShipmentValue(Long shipmentNbr);

}
