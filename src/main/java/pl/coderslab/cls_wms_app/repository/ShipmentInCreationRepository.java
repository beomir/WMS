package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.ShipmentInCreation;

import java.util.List;

@Repository
public interface ShipmentInCreationRepository extends JpaRepository<ShipmentInCreation, Long> {


    @Query("Select distinct s from ShipmentInCreation s join fetch s.article a join fetch s.shipMethod sh join fetch s.warehouse w where w.id =?1")
    List<ShipmentInCreation> getShipmentInCreationById(Long id);

    @Query("Select shi from Shipment shi join fetch shi.article a join fetch shi.shipMethod sh join fetch shi.warehouse ")
    List<ShipmentInCreation> getShipmentInCreation();

    @Query(value ="Select count(distinct sic.shipment_number) From shipment_in_creation sic join  company c on sic.company_id = c.id join  warehouse w on sic.warehouse_id = w.id JOIN  users u on u.company = c.name where sic.creation_closed = false and w.id =?1 and u.username like ?2", nativeQuery = true)
    int qtyOfOpenedShipmentsInCreation(Long id, String username);

    @Query(value = "select tT.shipNbr + 1 from ( Select sic.shipment_number shipNbr From shipment_in_creation sic union Select s.shipment_number shipNbr From shipments s ) tT order by 1 DESC Limit 1", nativeQuery = true)
    Long lastShipment();

    @Query("Select distinct sic From ShipmentInCreation sic join fetch sic.company c join fetch sic.warehouse w JOIN fetch Users u on u.company = c.name where sic.creation_closed = false and w.id =?1 and u.username like ?2 order by 1 DESC")
    List<ShipmentInCreation> openedShipments(Long id, String username);

    @Query(value = "select article_number,  sum(a.on_stock) - sum(a.to_sent) diff from (select  article_number, s.pieces_qty on_stock,0 to_sent from storage s inner join article a on s.article_id = a.id inner join shipment_in_creation sic on s.article_id = sic.article_id join company c on a.company_id = c.id join users u on u.company = c.name where  s.warehouse_id = ?1 and u.username like ?2 union select article_number, 0,sic.pieces_qty to_sent from shipment_in_creation sic inner join article a on sic.article_id = a.id join company c on a.company_id = c.id join users u on u.company = c.name where  sic.warehouse_id = ?1 and u.username like ?2 ) a group by article_number order by article_number" , nativeQuery = true)
    List<Long> stockDifference(Long id, String username);

    @Query(value = "select  sum(a.on_stock) - sum(a.to_sent) diff, article_number from (select  article_number, s.pieces_qty on_stock,0 to_sent from storage s inner join article a on s.article_id = a.id inner join shipment_in_creation sic on s.article_id = sic.article_id join company c on a.company_id = c.id join users u on u.company = c.name where  s.warehouse_id = ?1 and u.username like ?2 union select article_number, 0,sic.pieces_qty to_sent from shipment_in_creation sic inner join article a on sic.article_id = a.id join company c on a.company_id = c.id join users u on u.company = c.name where  sic.warehouse_id = ?1 and u.username like ?2 ) a group by article_number order by article_number" , nativeQuery = true)
    List<Long> stockDifferenceQty(Long id, String username);

    @Query(value = "select sum(sic.pieces_qty) to_sent, article_number from shipment_in_creation sic inner join article a on sic.article_id = a.id join company c on a.company_id = c.id join users u on u.company = c.name where  sic.warehouse_id = ?1 and u.username like ?2  group by article_number order by article_number" , nativeQuery = true)
    List<Long> shipmentCreationSummary(Long id, String username);

//    @Query("Select distinct sic From ShipmentInCreation sic join fetch sic.article a join fetch Stock s on s.article = sic.article and s.warehouse = sic.warehouse and sic.company = s.company join fetch sic.company c join fetch sic.warehouse w JOIN fetch Users u on u.company = c.name where sic.creation_closed = false and w.id =?1 and u.username like ?2 order by 1 DESC")
//    List<ShipmentInCreation> stockDifferencesTest(Long id, String username);


    @Query(value = "Select count(creation_closed) from shipment_in_creation where shipment_number = ?1 and creation_closed is true", nativeQuery = true)
    int getCreatedShipmentById(Long shipmentNbr);

    @Modifying
    @Transactional
    @Query(value = "update shipment_in_creation SET creation_closed = true where shipment_number = ?1",nativeQuery = true)
    void updateCloseCreationShipmentValue(Long shipmentNbr);




}
