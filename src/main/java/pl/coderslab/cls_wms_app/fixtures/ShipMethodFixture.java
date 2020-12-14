package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.ShipMethod;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.ShipMethodService;

import java.util.Arrays;
import java.util.List;

@Component
public class ShipMethodFixture {
    private ShipMethodService shipMethodService;

    private List<ShipMethod> shipMethodList = Arrays.asList(
            new ShipMethod(null, "LIFO", "Last in First out", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new ShipMethod(null, "FIFO", "First in First out",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system"),
            new ShipMethod(null, "FEFO", "First expired First out",TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system")
    );

    @Autowired
    public ShipMethodFixture(ShipMethodService shipMethodService) {
        this.shipMethodService = shipMethodService;
    }

    public void loadIntoDB() {
        for (ShipMethod shipMethod : shipMethodList) {
            shipMethodService.add(shipMethod);
        }
    }
}
