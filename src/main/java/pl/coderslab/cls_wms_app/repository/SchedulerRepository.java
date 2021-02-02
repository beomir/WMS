package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Scheduler;

import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    @Query("Select  s from Scheduler s where s.active = true")
    List<Scheduler> getScheduler();


    @Query("Select s from Scheduler s where s.active = false")
    List<Scheduler> getDeactivatedScheduler();


    @Query("SELECT distinct c FROM Users u JOIN Company c on u.company = c.name join Scheduler s on s.company.id = c.id WHERE u.username like ?1 order by c.name")
    List<Scheduler> getSchedulerByUsername(String username);

}
