package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.Shipment;

import java.util.List;

@Repository
public interface ReceptionRepository extends JpaRepository<Reception, Long> {

    @Query("Select r from Reception r join fetch r.article a join fetch r.warehouse w where w.id =?1")
    List<Reception> getReception(Long id);


}
