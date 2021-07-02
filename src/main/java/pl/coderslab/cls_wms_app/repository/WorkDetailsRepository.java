package pl.coderslab.cls_wms_app.repository;

import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.entity.WorkDetails;

import java.util.List;

@Repository
public interface WorkDetailsRepository extends JpaRepository<WorkDetails, Long> {


    @Query("Select w from WorkDetails w ")
    List<WorkDetails> getAll();

    @Query("Select w from WorkDetails w join w.warehouse wh where wh.id = ?1")
    List<WorkDetails> getWorkDetailsPerWarehouse(Long warehouseId);

    @Query("Select w from WorkDetails w where  w.handle = ?1 and w.warehouse.name = ?2 and w.status = 'open'")
    List<WorkDetails> getWorkListByWarehouseAndHandle(String warehouseName, String handle);

    @Query("Select w from WorkDetails w where w.handle = ?1")
    List<WorkDetails> getWorkDetailsByHandle(String handle);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.handle = ?1 and w2.name = ?2 and w.status = 'open'",nativeQuery = true)
    int checkIfWorksExistsForHandle(String handle,String warehouseName);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.handle = ?1 and w2.name = ?2 and w.status = ?3",nativeQuery = true)
    int checkIfWorksExistsForHandleWithStatusUser(String handle,String warehouseName,String status);

    @Query(value="Select count(*) from work_details w where w.handle = ?1 and w.status = 'open'",nativeQuery = true)
    int checkIfWorksExistsOnlyByHandle(String handle);

    @Query("Select w from WorkDetails w where w.hdNumber = ?1 and w.warehouse.name = ?2 and w.handle = ?3 and w.article.article_number = ?4")
    WorkDetails workLineFinish(Long hdNumber, String warehouseName, String handle, Long articleNumber);

    @Query("Select w from WorkDetails w where w.warehouse.name like ?1 and w.company.name like ?2 and CONCAT(w.article.article_number,'') like ?3 and w.workType like ?4 and w.handle like ?5 and CONCAT(w.hdNumber,'') like ?6 and CONCAT(w.status,'') like ?7 and w.fromLocation.locationName like ?8 and w.toLocation.locationName like ?9 and CONCAT(w.workNumber,'') like ?10")
    List<WorkDetails> getWorkDetailsByCriteria(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType,String workDetailsHandle,String workDetailsHandleDevice,String workDetailsStatus,String workDetailsLocationFrom,String workDetailsLocationTo,String workDetailsWorkNumber);

    @Query(value="select w.id,w.handle, hd_number hdNumber, lFrom.location_name fromLocation,lTo.location_name toLocation,pieces_qty piecesQty,a.article_number article,w.status from work_details w inner join location lFrom on w.from_location_id = lFrom.id inner join location lTo on w.to_location_id = lTo.id inner join warehouse w2 on w.warehouse_id = w2.id inner join article a on w.article_id = a.id where w.handle = ?1 and status = ?3 and w2.name = ?2 order by article_number limit 1",nativeQuery = true)
    WorkToDoFound workToDoFound(String handle,String warehouseName, String status);

    public static interface WorkToDoFound {
        Long getId();
        String getHandle();
        String getHdNumber();
        String getFromLocation();
        String getToLocation();
        String getPiecesQty();
        String getArticle();
        String getStatus();
    }

    @Query(value="select distinct work_number workNumber, work_description workDescription,work_type workType,handle,sum(pieces_qty) piecesQty,if(count(distinct status)>1,'%',status) status, w.name changeBy, count(work_number) hdNumber from work_details wd join warehouse w on wd.warehouse_id = w.id join location lFrom on wd.from_location_id = lFrom.id join location lTo on wd.to_location_id = lTo.id join company c on wd.company_id = c.id join article a on a.id = wd.article_id where w.name like ?1 and c.name like ?2 and CONCAT(a.article_number,'') like ?3 and wd.work_type like ?4 and wd.handle like ?5 and CONCAT(wd.hd_Number,'') like ?6 and wd.status like ?7 and lFrom.location_Name like ?8 and lTo.location_Name like ?9 and CONCAT(wd.work_Number,'') like ?10 group by work_number, work_description,work_type,handle",nativeQuery = true)
    List<WorkHeaderList> workHeaderList(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType,String workDetailsHandle,String workDetailsHandleDevice,String workDetailsStatus,String workDetailsLocationFrom,String workDetailsLocationTo,String workDetailsWorkNumber);

    public static interface WorkHeaderList{
        Long getWorkNumber();
        String getWorkDescription();
        String getWorkType();
        Long getHandle();
        Long getPiecesQty();
        String getStatus();
        String getChangeBy();
        Long getHdNumber();
    }

    @Query("Select wd from WorkDetails wd where wd.workNumber = ?1")
    List<WorkDetails> getWorkDetailsByWorkNumber(Long workNumber);

    @Query(value = "Select wd.work_number workNumber,w.name status,c.name changeBy,wd.work_type workType, count(*) piecesQty from  work_details wd inner join warehouse w on wd.warehouse_id = w.id inner join company c on wd.company_id = c.id where wd.work_number = ?1 group by wd.work_number,w.name,c.name,wd.work_type order by 1 limit 1",nativeQuery = true)
    WorkNumberDetailsInfo workNumberDetailsInfo(Long workNumber);

    public static interface WorkNumberDetailsInfo{
        Long getWorkNumber();
        String getStatus();
        String getChangeBy();
        String getWorkType();
        Long getPiecesQty();
    }

}
