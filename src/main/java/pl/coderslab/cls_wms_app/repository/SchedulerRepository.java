package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Scheduler;

import java.util.List;

@Repository
public interface SchedulerRepository extends JpaRepository<Scheduler, Long> {

    @Query("Select distinct s from Scheduler s join fetch s.company c join fetch Users u on u.company = c.name where s.active = true and u.username like ?1 order by u.company, 1")
    List<Scheduler> getScheduler(String username);


    @Query("Select s from Scheduler s where s.active = false")
    List<Scheduler> getDeactivatedScheduler();


    @Query("SELECT distinct c FROM Users u JOIN Company c on u.company = c.name join Scheduler s on s.company.id = c.id WHERE u.username like ?1 order by c.name")
    List<Scheduler> getSchedulerByUsername(String username);

    @Query("Select distinct s From Scheduler s where s.hour = ?1 and s.dayOfWeek like ?2 and s.type = ?3")
    List<Scheduler> getSchedulers(String hours,String dayOfWeek,String type);

    @Query("Select s from Scheduler s where s.company.name = ?1 and s.type = ?2")
    List<Scheduler> getSchedulerByCompanyName(String companyName, String type);

    @Query("Select s from Scheduler s where s.company.name = ?1 and s.type like ?2")
    Scheduler getOneSchedulerByCompanyName(String companyName, String type);
}
