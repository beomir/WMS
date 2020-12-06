package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.entity.Status;
import pl.coderslab.cls_wms_app.entity.Unit;

import java.util.List;

@Repository
public interface UnitRepository extends JpaRepository<Unit, Long> {

    @Query("Select u from Unit u where u.active = true")
    List<Unit> getUnit();

    @Query("Select u from Unit u where u.active = false")
    List<Unit> getDeactivatedUnit();

}
