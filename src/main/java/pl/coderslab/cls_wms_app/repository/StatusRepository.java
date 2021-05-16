package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Status;

import java.util.List;

@Repository
public interface StatusRepository extends JpaRepository<Status, Long> {

    @Query("Select s from Status s")
    List<Status> getStatus();

    @Query("Select s from Status s where s.id = ?1")
    Status getStatusById(Long id);

    @Query("Select s from Status s where s.status = ?1 and s.process =?2")
    Status getStatusByStatusName(String statusName,String process);

    @Query("Select s from Status s where s.process =?1")
    List<Status> getStatusesByProcess(String process);

    @Query("Select s from Status s where s.process = 'stock'")
    List<Status> getStockStatuses();

    @Query("Select s from Status s where s.process = 'stock' and s.status = ?1")
    Status checkIfStockStatusExists(String status);

    @Query("Select s from Status s where s.id = ?1")
    Status checkIfStatusExists(Long statusId);
}
