package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Vendor;

import java.util.List;

public interface VendorService {

    void add(Vendor vendor);

    List<Vendor> getVendor();

    Vendor findById(Long id);


    Vendor get(Long id);

    void delete(Long id);

    void update(Vendor vendor);
}
