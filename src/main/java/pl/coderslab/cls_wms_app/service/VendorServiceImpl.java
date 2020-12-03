package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Customer;
import pl.coderslab.cls_wms_app.entity.Vendor;
import pl.coderslab.cls_wms_app.repository.CustomerRepository;
import pl.coderslab.cls_wms_app.repository.VendorRepository;

import java.util.List;

@Service
public class VendorServiceImpl implements VendorService{
    private VendorRepository vendorRepository;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void add(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> getVendor() {
        return vendorRepository.getVendor();
    }

    @Override
    public Vendor findById(Long id) {
        return null;
    }

    @Override
    public Vendor get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Vendor vendor) {

    }
}
