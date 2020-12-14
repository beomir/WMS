package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.Vendor;
import pl.coderslab.cls_wms_app.repository.VendorRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class VendorServiceImpl implements VendorService{
    private final VendorRepository vendorRepository;

    @Autowired
    public VendorServiceImpl(VendorRepository vendorRepository) {
        this.vendorRepository = vendorRepository;
    }

    @Override
    public void add(Vendor vendor) {
        vendorRepository.save(vendor);
    }

    @Override
    public List<Vendor> getVendor(String username) {
        return vendorRepository.getVendor(username);
    }

    @Override
    public List<Vendor> getVendors() {
        return vendorRepository.getVendors();
    }

    @Override
    public List<Vendor> getDeactivatedVendor() {
        return vendorRepository.getDeactivatedVendor();
    }

    @Override
    public Vendor findById(Long id) {
        return vendorRepository.getOne(id);
    }

    @Override
    public Vendor get(Long id) {
        return vendorRepository.getOne(id);
    }

    @Override
    public void delete(Long id) {
        Vendor vendor = vendorRepository.getOne(id);
        vendor.setActive(false);
        vendor.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        vendor.setChangeBy(SecurityUtils.usernameForActivations());
        vendorRepository.save(vendor);
    }

    @Override
    public void activate(Long id) {
        Vendor vendor = vendorRepository.getOne(id);
        vendor.setActive(true);
        vendor.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        vendor.setChangeBy(SecurityUtils.usernameForActivations());
        vendorRepository.save(vendor);
    }


}
