package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Location;
import pl.coderslab.cls_wms_app.entity.Production;

import java.util.List;

@Repository
public interface ProductionRepository extends JpaRepository<Production, Long> {


    @Query("Select p from Production p where p.company.name like ?1 and p.warehouse.name like ?2 and CONCAT(p.finishProductNumber,'') like ?3 and CONCAT(p.intermediateArticleNumber,'') like ?4 and p.created like ?5 and p.last_update like ?6 and p.status like ?7 ")
    List<Production> getProductionByCriteria(String companyName,String warehouseName,String finishProductNumber,String intermediateArticleNumber,String created, String lastUpdate, String status);

    @Query("Select p from Production p where p.productionNumber = ?1")
    Production getProductionByNumber(Long productionNumber);

    @Query(value = "Select production_number from Production p where  concat(production_number,'') like concat(substr(?1,1,8), '%')  order by 1 DESC limit 1",nativeQuery = true)
    Long lastProductionNumberForToday(Long productionNumber);

}
