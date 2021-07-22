package pl.coderslab.cls_wms_app.repository;

import org.hibernate.jdbc.Work;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.entity.WorkDetails;

import java.util.List;

@Repository
public interface WorkDetailsRepository extends JpaRepository<WorkDetails, Long> {


    @Query("Select w from WorkDetails w ")
    List<WorkDetails> getAll();


    @Query("Select w from WorkDetails w where  w.handle = ?1 and w.warehouse.name = ?2 and w.status = 'open'")
    List<WorkDetails> getWorkListByWarehouseAndHandle(String handle,String warehouseName);

    @Query("Select w from WorkDetails w where w.handle = ?1")
    List<WorkDetails> getWorkDetailsByHandle(String handle);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.handle = ?1 and w2.name = ?2 and w.status = 'open'",nativeQuery = true)
    int checkIfWorksExistsForHandle(String handle,String warehouseName);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.handle = ?1 and w2.name = ?2 and w.status = ?3",nativeQuery = true)
    int checkIfWorksExistsForHandleWithStatusUser(String handle,String warehouseName,String status);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.work_number = ?1 and w2.name = ?2 and w.status = 'open' and w.work_description = ?3",nativeQuery = true)
    int checkIfWorksExistsForHandleProduction(Long workNumber,String warehouseName,String work_description);

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.work_number = ?1 and w2.name = ?2 and w.status = ?3 and w.work_description = ?4",nativeQuery = true)
    int checkIfWorksExistsForHandleWithStatusUserProduction(String handle,String warehouseName,String status,String work_description);

    @Query(value="Select count(*) from work_details w where w.handle = ?1 and w.status = 'open'",nativeQuery = true)
    int checkIfWorksExistsOnlyByHandle(String handle);

    @Query("Select w from WorkDetails w where w.hdNumber = ?1 and w.warehouse.name = ?2 and w.handle = ?3 and w.article.article_number = ?4")
    WorkDetails workLineFinish(Long hdNumber, String warehouseName, String handle, Long articleNumber);

    @Query("Select w from WorkDetails w where w.hdNumber = ?1 and w.warehouse.name = ?2 and w.workNumber = ?3 and w.article.article_number = ?4")
    WorkDetails workLineFinish(Long hdNumber, String warehouseName, Long workNumber, Long articleNumber);

    @Query("Select w from WorkDetails w where w.hdNumber = ?1 and w.warehouse.name = ?2 and w.workNumber = ?3 and w.article.article_number = ?4")
    WorkDetails workLineFinishByWorkNumber(Long hdNumber, String warehouseName, Long workNumber, Long articleNumber);

    @Query(value="select a.rowNumber workDescription,a.id,a.handle,a.hdNumber,a.fromLocation,a.toLocation,a.piecesQty,a.article,a.status,max(a.rowNumber) created from (select row_number() over () rowNumber,w.id,w.handle,hd_number hdNumber,lFrom.location_name fromLocation,lTo.location_name toLocation,pieces_qty piecesQty,a.article_number article,w.status from work_details w inner join location lFrom on w.from_location_id = lFrom.id inner join location lTo on w.to_location_id = lTo.id inner join warehouse w2 on w.warehouse_id = w2.id inner join article a on w.article_id = a.id where w.handle = ?1 and w2.name = ?2 ) a where a.status = ?3 order by article limit 1",nativeQuery = true)
    WorkToDoFound workToDoFound(String handle,String warehouseName, String status);

    public static interface WorkToDoFound {
        Integer getWorkDescription();
        Long getId();
        String getHandle();
        String getHdNumber();
        String getFromLocation();
        String getToLocation();
        String getPiecesQty();
        String getArticle();
        String getStatus();
        String getCreated();
    }

    @Query(value="select a.rowNumber workDescription,a.id,a.handle,a.hdNumber,a.fromLocation,a.toLocation,a.piecesQty,a.article,a.status,max(a.rowNumber) created from (select row_number() over () rowNumber,w.id,w.handle,hd_number hdNumber,lFrom.location_name fromLocation,lTo.location_name toLocation,pieces_qty piecesQty,a.article_number article,w.status from work_details w inner join location lFrom on w.from_location_id = lFrom.id inner join location lTo on w.to_location_id = lTo.id inner join warehouse w2 on w.warehouse_id = w2.id inner join article a on w.article_id = a.id where w.work_number = ?1 and w2.name = ?2 ) a where a.status = ?3 order by article limit 1",nativeQuery = true)
    WorkToDoFoundByWorkNumber workToDoFoundByWorkNumber(Long workNumber,String warehouseName, String status);

    public static interface WorkToDoFoundByWorkNumber {
        Integer getWorkDescription();
        Long getId();
        String getHandle();
        String getHdNumber();
        String getFromLocation();
        String getToLocation();
        String getPiecesQty();
        String getArticle();
        String getStatus();
        String getCreated();
    }

    @Query(value="select w.id,w.handle, hd_number hdNumber, lFrom.location_name fromLocation,lTo.location_name toLocation,pieces_qty piecesQty,a.article_number article,w.status from work_details w inner join location lFrom on w.from_location_id = lFrom.id inner join location lTo on w.to_location_id = lTo.id inner join warehouse w2 on w.warehouse_id = w2.id inner join article a on w.article_id = a.id where w.work_number = ?1 and status = ?3 and w2.name = ?2 order by article_number limit 1",nativeQuery = true)
    WorkToDoFoundProduction workToDoFoundProduction(Long workNumber,String warehouseName, String status);

    public static interface WorkToDoFoundProduction {
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

    @Query("Select wd from WorkDetails wd where wd.workNumber = ?1")
    WorkDetails getOneWorkDetailsByWorkNumber(Long workNumber);

    @Query(value = "Select wd.work_number workNumber,w.name status,c.name changeBy,wd.work_type workType, count(*) piecesQty from  work_details wd inner join warehouse w on wd.warehouse_id = w.id inner join company c on wd.company_id = c.id where wd.work_number = ?1 group by wd.work_number,w.name,c.name,wd.work_type order by 1 limit 1",nativeQuery = true)
    WorkNumberDetailsInfo workNumberDetailsInfo(Long workNumber);

    public static interface WorkNumberDetailsInfo{
        Long getWorkNumber();
        String getStatus();
        String getChangeBy();
        String getWorkType();
        Long getPiecesQty();
    }

    @Query(value = "select distinct a2.article_number from work_details wd inner join intermediate_article ia on wd.article_id = ia.article_id inner join intermediate_article_production_article iapa on ia.id = iapa.intermediate_article_id inner join production_article pa on iapa.production_article_id = pa.id join article a2 on a2.id = pa.article_id join warehouse w on w.id = wd.warehouse_id where work_number = ?1 and w.name = ?2",nativeQuery = true)
    Long finishProductNumber(String handle, String warehouseName);


    @Query(value = "select work_type from work_details where work_number = ?1 order by 1 limit 1",nativeQuery = true)
    String workTypeByWorkNumber(Long workNumber);

    @Query("Select w from WorkDetails w where  w.workNumber = ?1 and w.warehouse.name = ?2 and w.status = ?3")
    List<WorkDetails> getWorkListByWarehouseAndWorkNumber(Long workNumber,String warehouseName, String status);


    @Query(value="select handle from work_details where work_number = ?1 and work_description like ?2 order by 1 limit 1",nativeQuery = true)
    String workDetailHandle(Long workNumber,String workDescription);



    @Query(value="select distinct work_number workNumber, work_description workDescription,work_type workType,handle,sum(pieces_qty) piecesQty,if(count(distinct status)>1,'%',status) status, w.name changeBy, count(work_number) hdNumber from work_details wd join warehouse w on wd.warehouse_id = w.id join location lFrom on wd.from_location_id = lFrom.id join location lTo on wd.to_location_id = lTo.id join company c on wd.company_id = c.id join article a on a.id = wd.article_id where w.name like ?1 and c.name like ?2 and CONCAT(a.article_number,'') like ?3 and wd.work_type like ?4 and wd.handle like ?5 and CONCAT(wd.hd_Number,'') like ?6 and (wd.status = ?7 || wd.status = 'open') and lFrom.location_Name like ?8 and lTo.location_Name like ?9 and CONCAT(wd.work_Number,'') like ?10 and wd.work_description like ?11 group by work_number, work_description,work_type,handle order by 1 limit 5",nativeQuery = true)
    List<WorkHeaderListProduction> workHeaderListProduction(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType,String workDetailsHandle,String workDetailsHandleDevice,String workDetailsStatus,String workDetailsLocationFrom,String workDetailsLocationTo,String workDetailsWorkNumber,String workDescription);

    public static interface WorkHeaderListProduction{
        Long getWorkNumber();
        String getWorkDescription();
        String getWorkType();
        Long getHandle();
        Long getPiecesQty();
        String getStatus();
        String getChangeBy();
        Long getHdNumber();
    }

    @Query(value="select work_number from work_details wd inner join warehouse w on w.id = wd.warehouse_id inner join company c on c.id = wd.company_id where c.name = ?1 and w.name = ?2 and (wd.status = 'open' || wd.status = ?4 ) and wd.work_description = ?3 order by 1 limit 1",nativeQuery = true)
    Long workNumberByCompanyWarehouseWorkDescriptionStatusUser(String companyName,String warehouseName, String workDescription,String statusUser);

    @Query(value="select handle from work_details wd inner join warehouse w on w.id = wd.warehouse_id inner join company c on c.id = wd.company_id where c.name = ?1 and w.name = ?2 and (wd.status = 'open' || wd.status = ?4 ) and wd.work_description = ?3 order by 1 limit 1",nativeQuery = true)
    Long handleByCompanyWarehouseWorkDescriptionStatusUser(String companyName,String warehouseName, String workDescription,String statusUser);

    @Query(value="Select count(*) from work_details where hd_number = ?1",nativeQuery = true)
    int checkIfHdNumberExistsInWorkDetails(Long hd_number);

    @Query(value ="select round(max(pieces_qty * a.weight),2) handle,round(max(pieces_qty * a.volume),2) piecesQty from work_details inner join article a on work_details.article_id = a.id where handle = ?1",nativeQuery = true)
    MaxVolumeAndWeightForWork maxVolumeAndWeightForWork(String handle);

    public static interface MaxVolumeAndWeightForWork{
        Long getHandle();
        Long getPiecesQty();
    }

    @Query(value ="select round(max(pieces_qty * a.weight),2) handle,round(max(pieces_qty * a.volume),2) piecesQty from work_details inner join article a on work_details.article_id = a.id where work_number = ?1",nativeQuery = true)
    MaxVolumeAndWeightForWorkByWorkNumber maxVolumeAndWeightForWorkByWorkNumber(String handle);

    public static interface MaxVolumeAndWeightForWorkByWorkNumber{
        Long getHandle();
        Long getPiecesQty();
    }
}
