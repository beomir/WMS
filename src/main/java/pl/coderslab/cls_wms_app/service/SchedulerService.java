package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Scheduler;

import java.util.List;

public interface SchedulerService {

    void add(Scheduler scheduler);

    void addFixture(Scheduler scheduler);

    List<Scheduler> getScheduler(String scheduler);

    List<Scheduler> getDeactivatedScheduler();

    Scheduler findById(Long id);

    void delete(Long id);

    void activate(Long id);

    List<Scheduler> getSchedulerByUsername(String username);
}
