package pl.coderslab.cls_wms_app.fixtures;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Status;
import pl.coderslab.cls_wms_app.service.wmsValues.StatusService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class StatusFixture {

    private StatusService statusService;
    private List<Status> statusList = Arrays.asList(
            new Status(null, "on_hand", "Available on stock","Stock", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "to_send", "Assigned to send","StockShipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "on_reception", "Assigned to receipt","StockReception", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "destroyed", "Destroyed, ready to utilization","Stock", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "damaged", "Damaged","Stock", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "creation_pending", "Reception during creation","Reception", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "unloading_pending", "Reception during unloading","Reception", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "put_away_pending", "Reception during putting away to destination location process","Reception", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "closed", "Put away process finished for all handle devices for reception","Reception", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "creation_pending", "Shipment during creation","Shipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "picking_pending", "Shipment picking","Shipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "put_away_pending", "Shipment putaway","Shipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "closed", "Shipment closed","Shipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")

    );

    @Autowired
    public StatusFixture(StatusService statusService) {
        this.statusService = statusService;
    }

    public void loadIntoDB() {
        for (Status status : statusList) {
            statusService.add(status);
        }
    }
}
