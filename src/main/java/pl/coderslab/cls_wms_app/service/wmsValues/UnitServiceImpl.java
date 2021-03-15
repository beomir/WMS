package pl.coderslab.cls_wms_app.service.wmsValues;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Unit;
import pl.coderslab.cls_wms_app.repository.UnitRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class UnitServiceImpl implements UnitService{

    private UnitRepository unitRepository;

    @Autowired
    public UnitServiceImpl(UnitRepository unitRepository) {
        this.unitRepository = unitRepository;
    }

    @Override
    public void add(Unit unit) {
        unitRepository.save(unit);
    }

    @Override
    public List<Unit> getUnit() {
        return unitRepository.getUnit();
    }

    @Override
    public List<Unit> getDeactivatedUnit() {
        return unitRepository.getDeactivatedUnit();
    }

    @Override
    public Unit findById(Long id) {
        return unitRepository.getOne(id);
    }


    @Override
    public void delete(Long id) {
        Unit unit = unitRepository.getOne(id);
        unit.setActive(false);
        unit.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        unit.setChangeBy(SecurityUtils.usernameForActivations());
        unitRepository.save(unit);
    }

    @Override
    public void activate(Long id) {
        Unit unit = unitRepository.getOne(id);
        unit.setActive(true);
        unit.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        unit.setChangeBy(SecurityUtils.usernameForActivations());
        unitRepository.save(unit);
    }

}
