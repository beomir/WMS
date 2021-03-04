package pl.coderslab.cls_wms_app.fixtures;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Status;
import pl.coderslab.cls_wms_app.service.StatusService;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class StatusFixture {

    private StatusService statusService;
    private List<Status> statusList = Arrays.asList(
            new Status(null, "on_hand", "Available on stock", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "to_send", "Assigned to send", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "on_reception", "Assigned to receipt", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "destroyed", "Destroyed, ready to utilization", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new Status(null, "damaged", "Damaged", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")

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
