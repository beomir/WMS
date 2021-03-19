package pl.coderslab.cls_wms_app.temporaryObjects;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Scheduler;
import pl.coderslab.cls_wms_app.repository.SchedulerRepository;
import pl.coderslab.cls_wms_app.service.wmsOperations.ReceptionService;
import pl.coderslab.cls_wms_app.service.wmsOperations.ShipmentService;
import pl.coderslab.cls_wms_app.service.storage.StockService;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;


@Service
@Slf4j
public class DataUpdater {

    private final StockService stockService;
    private final SchedulerRepository schedulerRepository;
    private final ReceptionService receptionService;
    private final ShipmentService shipmentService;

    @Autowired
    public DataUpdater(StockService stockService, SchedulerRepository schedulerRepository, ReceptionService receptionService, ShipmentService shipmentService) {
        this.stockService = stockService;
        this.schedulerRepository = schedulerRepository;
        this.receptionService = receptionService;
        this.shipmentService = shipmentService;
    }



    @Scheduled(cron = "0 * * ? * *")
    public void updateData(){
        reportSender();
    }

    public void reportSender(){
        String hour  = String.valueOf(LocalTime.now().getHour());
        if(hour.length()<2){
            hour = "0" + hour;
        }
        String minutes = String.valueOf(LocalTime.now().getMinute());
        if(minutes.length()<2){
            minutes = "0" + minutes;
        }
        String time = hour + ":" + minutes;
        String day = "%" + LocalDate.now().getDayOfWeek() + "%";
        System.out.println(time);
        System.out.println(day);
        System.out.println("Reception" + schedulerRepository.getSchedulers(time,day,"Reception").size());
        System.out.println("Stock" + schedulerRepository.getSchedulers(time,day,"Stock").size());
        System.out.println("Shipment" + schedulerRepository.getSchedulers(time,day,"Shipment").size());
        if(schedulerRepository.getSchedulers(time,day,"Reception").size()>0){
            List<Scheduler> schedulers = schedulerRepository.getSchedulers(time,day,"Reception");
            for(Scheduler value : schedulers){
                receptionService.sendReceptions(value.getCompany().getName());
            }
        }
        else if(schedulerRepository.getSchedulers(time,day,"Stock").size()>0){
            List<Scheduler> schedulers = schedulerRepository.getSchedulers(time,day,"Stock");
            for(Scheduler value : schedulers){
                stockService.sendStock(value.getCompany().getName());
            }
        }
        else if(schedulerRepository.getSchedulers(time,day,"Shipment").size()>0){
            List<Scheduler> schedulers = schedulerRepository.getSchedulers(time,day,"Shipment");
            for(Scheduler value : schedulers){
                shipmentService.sentShipments(value.getCompany().getName());
            }
        }
    }
}
