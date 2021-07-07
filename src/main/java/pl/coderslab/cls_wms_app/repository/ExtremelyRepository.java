package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Extremely;

import pl.coderslab.cls_wms_app.entity.Warehouse;

import java.util.List;

@Repository
public interface ExtremelyRepository extends JpaRepository<Extremely, Long> {

    @Query("Select e from Extremely e")
    List<Extremely> getAllExtremelies();

    @Query("Select e from Extremely  e where e.warehouse.name like ?1 and e.extremelyName = ?2")
    List<Extremely> listCheckLocationScopeMax( String warehouse,String extremelyName);

    @Query("Select e from Extremely  e where e.warehouse.name = ?1 and e.extremelyName = ?2")
    Extremely checkLocationScopeMax( String warehouse,String extremelyName);

    @Query("Select e from Extremely  e where e.company.name = ?1 and e.extremelyName = ?2")
    Extremely checkProductionModuleStatus( String company,String extremelyName);
}
