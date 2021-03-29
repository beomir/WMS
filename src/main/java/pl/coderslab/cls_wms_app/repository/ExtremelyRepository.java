package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Unit;

import java.util.List;

@Repository
public interface ExtremelyRepository extends JpaRepository<Extremely, Long> {

    @Query("Select e from Extremely e")
    List<Extremely> getAllExtremelies();

}
