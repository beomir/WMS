package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pl.coderslab.cls_wms_app.entity.Reception;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query(value = "Select reception_number + 1 From receptions order by 1 DESC Limit 1", nativeQuery = true)
    Long lastReception();

    @Query("Select distinct r From Reception r join fetch r.company c join fetch r.warehouse w JOIN fetch Users u on u.company = c.name where r.status.status = 'creation_pending' and w.id =?1 and u.username like ?2 order by 1 DESC")
    List<Reception> openedReceptions(Long id, String username);


    @Query(value ="Select count(distinct r.reception_number) From receptions r inner join status s on r.status_id = s.id join  company c on r.company_id = c.id join  warehouse w on r.warehouse_id = w.id JOIN  users u on u.company = c.name where s.status = 'creation_pending' and w.id =?1 and u.username like ?2", nativeQuery = true)
    int qtyOfOpenedReceptions(Long id, String username);


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

    @Query(value="Select distinct v.name from receptions r inner join vendors v on v.id = r.vendor_id where reception_number = ?1",nativeQuery = true)
    String getVendorNameByReceptionNumber(Long receptionNumber);

//    @Query(value ="Select reception_number receptionNumber, sum(pieces_qty) pieces_qty, s.status,max(r.created) created,max(r.last_update) last_update,max(r.change_by) changeBy,c.name company, l.location_name location, count(hd_number) Hd_number from receptions r inner join status s on r.status_id = s.id inner join warehouse w on r.warehouse_id = w.id inner join company c on r.company_id = c.id left join location l on r.location_id = l.id where c.name =?1 and w.name = ?2 group by c.name, l.location_name, s.status, reception_number",nativeQuery = true)
    @Query(value ="select receptionNumber,sum(pieces_qty) pieces_qty,status,max(created) created,max(last_update) last_update, max(change_by) changeBy,company,location, sum(case when hd_number = '' then 0 else 1 end) Hd_number, warehouse from ( Select reception_number receptionNumber , pieces_qty, s.status,r.created,r.last_update,r.change_by,c.name company,IFNULL(l.location_name,'') location,IFNULL(Hd_number,'') Hd_number,w.name warehouse from receptions r inner join status s on r.status_id = s.id inner join warehouse w on r.warehouse_id = w.id inner join company c on r.company_id = c.id left join location l on r.location_id = l.id inner join vendors v on c.id = v.company_id where c.name = ?1 and w.name like ?2 and v.name like ?3 and s.status like ?4 and CONCAT(r.reception_number,'') like ?6 and r.created > ?8 and r.created < ?9 and r.change_by like ?10) a where location like ?5  and CONCAT(hd_number,'') like ?7 group by company, location, status, receptionNumber,warehouse",nativeQuery = true)
    List<ReceptionViewObject> getReceptionSummary(String companyName,String warehouseName,String vendorName, String status, String locationName, String receptionNumber, String hdNumber, String createdFrom, String createdTo,String createdBy);

    public static interface ReceptionViewObject {
         Long getReceptionNumber();
         Long getPieces_qty();
         String getStatus();
         String getCreated();
         String getLast_update();
         String getChangeBy();
         String getCompany();
         String getLocation();
         Long getHd_number();
         String getWarehouse();
    }

}
