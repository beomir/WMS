package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsDetails;
import pl.coderslab.cls_wms_app.entity.OrderOfLocationsHeader;

import java.util.List;

@Repository
public interface OrderOfLocationsHeaderRepository extends JpaRepository<OrderOfLocationsHeader, Long> {


    @Query("Select oh from OrderOfLocationsHeader oh where oh.active = false")
    List<OrderOfLocationsHeader> getNotActiveOrderOfLocationsHeader();

    @Query("Select  oh from OrderOfLocationsHeader oh where oh.active = true")
    List<OrderOfLocationsHeader> getActiveOrderOfLocationsHeader();

    //for fixtures
    @Query("Select oh from OrderOfLocationsHeader oh")
    List<OrderOfLocationsHeader> getOrderOfLocationsHeaderForFixture();
}
