package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Vendor;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("Select v from Vendor v")
    List<Vendor> getVendor();


}
