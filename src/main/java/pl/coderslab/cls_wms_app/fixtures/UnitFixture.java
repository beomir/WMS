package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.service.wmsValues.UnitService;

import java.util.Arrays;
import java.util.List;

@Component
//@Profile("local")
public class UnitFixture {

    private UnitService unitService;


    private List<Unit> unitList = Arrays.asList(
            new Unit(null, "EA", "each of piece", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Unit(null, "CAR", "carton", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Unit(null, "PAL", "pallet", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Unit(null, "KG", "kilogram", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system"),
            new Unit(null, "T", "ton", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system")
    );

    @Autowired
    public UnitFixture(UnitService unitService) {
        this.unitService = unitService;
    }

    public void loadIntoDB() {

        for (Unit unit : unitList) {
            unitService.add(unit);
        }

    }
}
