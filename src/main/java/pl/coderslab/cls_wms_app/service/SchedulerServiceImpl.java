package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.repository.SchedulerRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SchedulerServiceImpl implements SchedulerService{
    private final SchedulerRepository schedulerRepository;

    @Autowired
    public SchedulerServiceImpl(SchedulerRepository schedulerRepository) {
        this.schedulerRepository = schedulerRepository;
    }

    @Override
    public void add(Scheduler scheduler) {
        schedulerRepository.save(scheduler);
    }

    @Override
    public List<Scheduler> getScheduler(String scheduler) {
        return schedulerRepository.getScheduler(scheduler);
    }


    @Override
    public List<Scheduler> getDeactivatedScheduler() {
        return schedulerRepository.getDeactivatedScheduler();
    }

    @Override
    public Scheduler findById(Long id) {
        return schedulerRepository.getOne(id);
    }


    @Override
    public void delete(Long id) {
        Scheduler scheduler = schedulerRepository.getOne(id);
        scheduler.setActive(false);
        scheduler.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        scheduler.setChangeBy(SecurityUtils.usernameForActivations());
        schedulerRepository.save(scheduler);
    }

    @Override
    public void activate(Long id) {
        Scheduler scheduler = schedulerRepository.getOne(id);
        scheduler.setActive(true);
        scheduler.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        scheduler.setChangeBy(SecurityUtils.usernameForActivations());
        schedulerRepository.save(scheduler);
    }


    @Override
    public List<Scheduler> getSchedulerByUsername(String username) {
        return schedulerRepository.getSchedulerByUsername(username);
    }

}
