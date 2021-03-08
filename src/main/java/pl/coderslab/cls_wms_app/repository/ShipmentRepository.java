package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Shipment;
import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;
import java.util.Map;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w join s.company c join Users u on u.company = c.name  where w.id =?1 and u.username like ?2 order by s.finished,s.shipmentNumber")
    List<Shipment> getShipment(Long id,String username);

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

    @Query(value = "select  c2.name, sum(pieces_qty) from shipments s join company c on s.company_id = c.id join users u on u.company = c.name join customers c2 on c2.id = s.customer_id where creation_closed = true and finished = true and  s.warehouse_id = ?1 and u.username like ?2 group by c2.name",nativeQuery = true)
    Map<String,Integer> surveyMap(Long id, String username);

    @Query("Select s from Shipment s where s.shipmentNumber = ?1")
    List<Shipment> getShipmentByShipmentNumber(Long shipmentNbr);

    @Query(value="Select distinct c.name from shipments s inner join company c on s.company_id = c.id where s.shipment_number = ?1",nativeQuery = true)
    String getConmpanyNameByShipmentNumber(Long shipmentNbr);

    @Query(value="Select distinct w.name from shipments s inner join warehouse w on s.warehouse_id = w.id where s.shipment_number = ?1",nativeQuery = true)
    String getWarehouseByShipmentNumber(Long shipmentNbr);

    @Query("Select r from Shipment r where substring(r.last_update,1,10) >= ?1 and r.company.name = ?2 order by r.warehouse.name")
    List<Shipment> getShipmentsFromXDayBack(String dateBack, String company);

    @Query("Select shi from Shipment shi where shi.shipmentNumber = ?1")
    Shipment getOneShipmentByShipmentNumber(Long shipmentNumber);
}
