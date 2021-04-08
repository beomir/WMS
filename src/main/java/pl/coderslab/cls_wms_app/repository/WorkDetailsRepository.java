package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.entity.WorkDetails;

import java.util.List;

@Repository
public interface WorkDetailsRepository extends JpaRepository<WorkDetails, Long> {

    @Query("Select w from WorkDetails w where w.status = true")
    List<WorkDetails> getWorkDetailsOpen();

    @Query("Select w from WorkDetails w where w.status = false")
    List<WorkDetails> getWorkDetailsClosed();

    @Query("Select w from WorkDetails w ")
    List<WorkDetails> getAll();

    @Query(value="Select count(*) from work_details w inner join warehouse w2 on w.warehouse_id = w2.id where w.handle = ?1 and w2.name = ?2",nativeQuery = true)
    int checkIfWorksExistsForHandle(String handle,String warehouseName);

    @Query(value="select w.id,w.handle, hd_number, lFrom.location_name fromLocation,lTo.location_name toLocation,pieces_qty,a.article_number article,w.status from work_details w inner join location lFrom on w.from_location_id = lFrom.id inner join location lTo on w.to_location_id = lTo.id inner join warehouse w2 on w.warehouse_id = w2.id inner join article a on w.article_id = a.id where w.handle = ?1 and status = false and w2.name = ?2 order by article_number limit 1",nativeQuery = true)
    WorkToDoFound workToDoFound(String handle,String warehouseName);

    public static interface WorkToDoFound {
        Long getId();
        String getHandle();
        String getHdNumber();
        String getFromLocation();
        String getToLocation();
        String piecesQty();
        Long article();
        String status();
    }
}
