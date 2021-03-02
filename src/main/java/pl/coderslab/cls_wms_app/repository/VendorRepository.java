package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.entity.Vendor;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    @Query("Select distinct v from Vendor v join fetch v.company c JOIN fetch Users u on u.company = c.name where v.active = true and u.username like ?1 order by v.name")
    List<Vendor> getVendor(String username);

    @Query("Select v from Vendor v where v.active = false")
    List<Vendor> getDeactivatedVendor();

    @Query("Select v from Vendor v where v.active = true")
    List<Vendor> getVendors();

    @Query("Select v from Vendor v where v.name = ?1")
    Vendor getVendorByName(String vendorName);

}
