package pl.coderslab.cls_wms_app.service.wmsValues;

import pl.coderslab.cls_wms_app.entity.Vendor;

import java.util.List;

public interface VendorService {

    void add(Vendor vendor);

    List<Vendor> getVendor(String username);

    List<Vendor> getVendors();

    List<Vendor> getDeactivatedVendor();

    Vendor findById(Long id);


    Vendor get(Long id);

    void delete(Long id);

    void activate(Long id);

}
