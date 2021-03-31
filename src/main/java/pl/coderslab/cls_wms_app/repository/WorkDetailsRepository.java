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
}
