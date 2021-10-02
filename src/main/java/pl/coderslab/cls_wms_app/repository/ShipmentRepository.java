package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;
import java.util.Map;

@Repository
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w join s.company c join Users u on u.company = c.name  where w.id =?1 and u.username like ?2 order by s.finished,s.shipmentNumber")
    List<Shipment> getShipment(Long id,String username);

    @Query("Select shi from Shipment shi join fetch shi.article a join fetch shi.shipMethod sh join fetch shi.warehouse ")
    List<Shipment> getShipments();

    @Modifying
    @Transactional
    @Query(value = "update shipments SET finished = true where shipment_number = ?1",nativeQuery = true)
    void updateFinishedShipmentValue(Long shipmentNbr);

    @Modifying
    @Transactional
    @Query(value = "DELETE s.* from storage s inner join shipments shi on shi.shipment_number = s.shipment_number where shi.shipment_number = ?1 and shi.finished = true",nativeQuery = true)
    void deleteStockAfterFinishShipment(Long shipmentNbr);

    @Query(value = "select count(*) from shipments s join warehouse w on w.id = s.warehouse_id join company c on s.company_id = c.id join users u on u.company = c.name where creation_closed = true and finished = false and  w.name = ?1 and u.username like ?2",nativeQuery = true)
    int checkHowManyNotFinishedShipments(String warehouseName, String username);

    @Query(value = "select  c2.name, sum(pieces_qty) from shipments s join company c on s.company_id = c.id join users u on u.company = c.name join customers c2 on c2.id = s.customer_id where creation_closed = true and finished = true and  s.warehouse_id = ?1 and u.username like ?2 group by c2.name",nativeQuery = true)
    Map<String,Integer> surveyMap(Long id, String username);

    @Query("Select s from Shipment s where s.shipmentNumber = ?1")
    List<Shipment> getShipmentByShipmentNumber(Long shipmentNbr);

    @Query(value="Select distinct c.name from shipments s inner join company c on s.company_id = c.id where s.shipment_number = ?1",nativeQuery = true)
    String getCompanyNameByShipmentNumber(Long shipmentNbr);

    @Query(value="Select distinct w.name from shipments s inner join warehouse w on s.warehouse_id = w.id where s.shipment_number = ?1",nativeQuery = true)
    String getWarehouseByShipmentNumber(Long shipmentNbr);

    @Query("Select r from Shipment r where substring(r.last_update,1,10) >= ?1 and r.company.name = ?2 order by r.warehouse.name")
    List<Shipment> getShipmentsFromXDayBack(String dateBack, String company);

    @Query("Select shi from Shipment shi where shi.shipmentNumber = ?1")
    Shipment getOneShipmentByShipmentNumber(Long shipmentNumber);

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w join s.company c join Users u on u.company = c.name  where w.name =?1 and u.username like ?2 order by s.finished,s.shipmentNumber")
    List<Shipment> getShipmentsForLoggedUser(String warehouseName,String userName);

    @Query("Select distinct s from Shipment s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w join s.company c join Users u on u.company = c.name  where w.name =?1 and u.username like ?2 and s.shipmentNumber = ?3 order by s.finished,s.shipmentNumber")
    List<Shipment> getShipmentsForLoggedUserByShipmentNumber(String warehouseName,String userName,Long shipmentNumber);

    @Query(value ="select shipmentNumber,sum(pieces_qty) pieces_qty,status quality,max(created) created,max(last_update) last_update,max(change_by) changeBy,company,location,sum(case when hd_number = '' then 0 else 1 end) Hd_number,warehouse,finished from ( Select shipment_number shipmentNumber ,pieces_qty,s.status,shi.created,shi.last_update,shi.change_by,c.name company,IFNULL(l.location_name,'') location,IFNULL(Hd_number,'') Hd_number,w.name warehouse,shi.finished from shipments shi inner join status s on shi.status_id = s.id inner join warehouse w on shi.warehouse_id = w.id inner join company c on shi.company_id = c.id left join location l on shi.location_id = l.id inner join vendors v on c.id = v.company_id where c.name like ?1 and w.name like ?2 and v.name like ?3 and s.status like ?4 and CONCAT(shi.shipment_number,'') like ?6 and shi.created > ?8 and shi.created < ?9 and shi.change_by like ?10) a where location like ?5  and CONCAT(hd_number,'') like ?7 group by company, location, status, shipmentNumber,warehouse",nativeQuery = true)
    List<ShipmentRepository.ShipmentViewObject> getShipmentSummary(String companyName, String warehouseName, String vendorName, String status, String locationName, String receptionNumber, String hdNumber, String createdFrom, String createdTo, String createdBy);

    public static interface ShipmentViewObject {
        Long getShipmentNumber();
        Long getPieces_qty();
        String getQuality();
        String getCreated();
        String getLast_update();
        String getChangeBy();
        String getCompany();
        String getLocation();
        Long getHd_number();
        String getWarehouse();
        String getFinished();
    }
}
