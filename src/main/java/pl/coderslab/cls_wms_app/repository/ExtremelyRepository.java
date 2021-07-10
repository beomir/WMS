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

    @Query(value = "Select if(substr(a.hd_number + 1,1,4) != YEAR(CURDATE()),Concat(YEAR(CURDATE()), '00000000000001'),a.hd_number + 1) from ( Select hd_number from receptions union select hd_number from storage union select hd_number from work_details ) a order by 1 desc LIMIT 1;",nativeQuery = true)
    Long nextPalletNbr();

    @Query(value = "Select if(substr(work_number + 1,1,4) != YEAR(CURDATE()),Concat(YEAR(CURDATE()), '0000000001'),work_number + 1) from work_details order by 1 desc Limit 1",nativeQuery = true)
    Long nextWorkNumber();
}
