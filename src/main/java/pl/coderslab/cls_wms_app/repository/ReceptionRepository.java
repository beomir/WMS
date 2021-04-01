package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query("Select r from Reception r join fetch r.article a join fetch r.warehouse w where w.id =?1")
    List<Reception> getReception(Long id);

    @Query(value = "Select reception_number + 1 From receptions order by 1 DESC Limit 1", nativeQuery = true)
    Long lastReception();

    @Query("Select distinct r From Reception r join fetch r.company c join fetch r.warehouse w JOIN fetch Users u on u.company = c.name where r.status.status = 'creation_pending' and w.id =?1 and u.username like ?2 order by 1 DESC")
    List<Reception> openedReceptions(Long id, String username);

    @Query("Select distinct r from Reception r join fetch r.company c join fetch r.warehouse w JOIN fetch Users u on u.company = c.name where w.id =?1 and u.username like ?2 order by r.receptionNumber")
    List<Reception> getReceptions(Long id, String username);

    //TODO Think about replace getReceptions on query like below or make a logic by Thymeleaf to display proper data in view
    // Select reception_number, sum(pieces_qty), s.status,max(r.created),max(r.last_update),r.change_by from receptions r inner join status s on r.status_id = s.id group by reception_number, s.status, change_by

    @Query(value ="Select count(distinct r.reception_number) From receptions r inner join status s on r.status_id = s.id join  company c on r.company_id = c.id join  warehouse w on r.warehouse_id = w.id JOIN  users u on u.company = c.name where s.status = 'creation_pending' and w.id =?1 and u.username like ?2", nativeQuery = true)
    int qtyOfOpenedReceptions(Long id, String username);

//    @Query(value = "Select count(r.reception_number) from receptions r inner join status s on r.status_id = s.id where r.reception_number = ?1 and s.status = 'creation_pending' ", nativeQuery = true)
//    int getCreatedReceptionById(Long receptionNbr);

    @Modifying
    @Transactional
    @Query(value = "update receptions SET creation_closed = true where reception_number = ?1",nativeQuery = true)
    void updateCloseCreationValue(Long receptionNbr);

    @Modifying
    @Transactional
    @Query(value = "update receptions SET finished = true where reception_number = ?1",nativeQuery = true)
    void updateFinishedReceptionValue(Long receptionNbr);

    @Modifying
    @Transactional
    @Query(value = "INSERT INTO storage (created, hd_number, last_update, pieces_qty, quality, article_id, company_id, status_id, unit_id, warehouse_id, change_by,comment,reception_number) select localtime,hd_number,localtime,pieces_qty,quality,article_id,company_id,status_id,unit_id,warehouse_id, change_by,concat('Reception number: ',reception_number),reception_number from receptions where reception_number = (?1);",nativeQuery = true)
    void insertDataToStockAfterFinishedReception(Long receptionNbr);

    @Query(value = "Select a.hd_number + 1 from ( Select hd_number from receptions union select hd_number from storage where hd_number < 202000001800000000) a order by 1 desc LIMIT 1;",nativeQuery = true)
    Long nextPalletNbr();

    @Query(value="Select distinct c.name from receptions r inner join company c on r.company_id = c.id where r.reception_number = ?1",nativeQuery = true)
    String getCompanyNameByReceptionNumber(Long receptionNmbr);

    @Query("Select distinct r from Reception r where r.receptionNumber = ?1")
    List<Reception> getReceptionByReceptionNumber(Long receptionNbr);

    @Query(value="Select distinct w.name from receptions r inner join warehouse w on r.warehouse_id = w.id where r.reception_number = ?1",nativeQuery = true)
    String getWarehouseByReceptionNumber(Long receptionNbr);

    @Query("Select r from Reception r where substring(r.last_update,1,10) >= ?1 and r.company.name = ?2 order by r.warehouse.name")
    List<Reception> getReceptionsFromXDayBack(String dateBack, String company);

    @Query(value="Select count(*) from receptions where reception_number = ?1",nativeQuery = true)
    int checkIfReceptionAlreadyExists(Long receptionNbr);

    @Query(value="Select count(*) from receptions where hd_number = ?1",nativeQuery = true)
    int checkIfHdNumberExistsOnReception(Long hd_number);

    @Query("Select r from Reception r where r.receptionNumber = ?1")
    Reception getOneReceptionByReceptionNumber(Long receptionNbr);

    @Query(value="Select distinct s.status from receptions r inner join status s on s.id = r.status_id where reception_number = ?1",nativeQuery = true)
    String getStatusByReceptionNumber(Long receptionNumber);

}
