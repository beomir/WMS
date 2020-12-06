package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.entity.Vendor;

import java.util.List;

public interface VendorService {

    void add(Vendor vendor);

    List<Vendor> getVendor();

    List<Vendor> getDeactivatedVendor();

    Vendor findById(Long id);


    Vendor get(Long id);

    void delete(Long id);

    void activate(Long id);

    void update(Vendor vendor);
}
