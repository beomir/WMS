package pl.coderslab.cls_wms_app.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class DataUpdater {

    private final StockService stockService;

    @Autowired
    public DataUpdater( StockService stockService) {
        this.stockService = stockService;

    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void updateData(){
        stockService.sendStock();
    }
}
