package pl.coderslab.cls_wms_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.repository.SchedulerRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
@Slf4j
public class DataUpdater {

    private final StockService stockService;
    private final SchedulerRepository schedulerRepository;

    @Autowired
    public DataUpdater(StockService stockService, SchedulerRepository schedulerRepository) {
        this.stockService = stockService;

        this.schedulerRepository = schedulerRepository;
    }

    @Scheduled(cron = "0 * * ? * *")
    public void updateData(){

        String hour  = String.valueOf(LocalTime.now().getHour());
        if(hour.length()<2){
            hour = "0" + hour;
        }
        String minutes = String.valueOf(LocalTime.now().getMinute());
        if(minutes.length()<2){
            minutes = "0" + minutes;
        }
        String day = "%" + LocalDate.now().getDayOfWeek() + "%";
        System.out.println(hour);
        System.out.println(minutes);
        System.out.println(day);
        System.out.println(schedulerRepository.getSchedulers(minutes,hour,day).size());
        if(schedulerRepository.getSchedulers(minutes,hour,day).size()>0){
            List<Scheduler> schedulers = schedulerRepository.getSchedulers(minutes,hour,day);
            for(Scheduler value : schedulers){
                stockService.sendStock(value.getCompany().getName());
            }
        }

    }
}
