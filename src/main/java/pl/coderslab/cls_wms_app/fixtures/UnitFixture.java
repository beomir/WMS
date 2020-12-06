package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.service.ArticleService;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.UnitService;

import java.util.Arrays;
import java.util.List;

@Component
public class UnitFixture {

    private UnitService unitService;


    private List<Unit> unitList = Arrays.asList(
            new Unit(null, "EA", "each of piece", "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Unit(null, "CAR", "carton", "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Unit(null, "PAL", "pallet", "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Unit(null, "KG", "kilogram", "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true),
            new Unit(null, "T", "ton", "2020-11-28:T10:00:00","2020-11-28:T10:00:00",true)
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
