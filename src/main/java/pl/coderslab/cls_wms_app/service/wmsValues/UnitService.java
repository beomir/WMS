package pl.coderslab.cls_wms_app.service.wmsValues;

import pl.coderslab.cls_wms_app.entity.Unit;

import java.util.List;

public interface UnitService {

    void add(Unit unit);

    List<Unit> getUnit();

    List<Unit> getDeactivatedUnit();

    Unit findById(Long id);


    void delete(Long id);

    void activate(Long id);

}
