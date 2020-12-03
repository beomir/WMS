package pl.coderslab.cls_wms_app.fixtures;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Status;
import pl.coderslab.cls_wms_app.service.StatusService;

import java.util.Arrays;
import java.util.List;

@Component
public class StatusFixture {

    private StatusService statusService;
    private List<Status> statusList = Arrays.asList(
            new Status(null, "on_hand", "Available on stock", "2020-11-28:T10:00:00","2020-11-28:T10:00:00"),
            new Status(null, "to_send", "Assigned to send", "2020-11-28:T10:00:00","2020-11-28:T10:00:00"),
            new Status(null, "on_reception", "Assigned to receipt", "2020-11-28:T10:00:00","2020-11-28:T10:00:00"),
            new Status(null, "destroyed", "Destroyed, ready to utilization", "2020-11-28:T10:00:00","2020-11-28:T10:00:00"),
            new Status(null, "damaged", "Damaged", "2020-11-28:T10:00:00","2020-11-28:T10:00:00")

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
