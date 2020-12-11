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

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w where w.id =?1 order by s.finished,s.shipmentNumber")
    List<Shipment> getShipment(Long id);

    @Query("Select shi from Shipment shi join fetch shi.article a join fetch shi.shipMethod sh join fetch shi.warehouse ")
    List<Shipment> getShipmenta();

    @Modifying
    @Transactional
    @Query(value = "update shipments SET finished = true where shipment_number = ?1",nativeQuery = true)
    void updateFinishedShipmentValue(Long shipmentNbr);

    @Modifying
    @Transactional
    @Query(value = "DELETE s.* from storage s inner join shipments shi on shi.shipment_number = s.shipment_number where shi.shipment_number = ?1 and shi.finished = true",nativeQuery = true)
    void deleteStockAfterFinishShipment(Long shipmentNbr);

    @Query(value = "select count(*) from shipments s join company c on s.company_id = c.id join users u on u.company = c.name where creation_closed = true and finished = false and  s.warehouse_id = ?1 and u.username like ?2",nativeQuery = true)
    int checkHowManyNotfinishedShipments(Long id, String username);
}
