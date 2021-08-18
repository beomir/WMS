package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.ArticleTypes;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsDetails;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsHeader;

import java.util.List;

@Repository
public interface OrderOfLocationsDetailsRepository extends JpaRepository<OrderOfLocationsDetails, Long> {

    //for fixtures
    @Query("Select od from OrderOfLocationsDetails od")
    List<OrderOfLocationsDetails> getOrderOfLocationsDetailsForFixture();

}
