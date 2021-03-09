package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.entity.StorageZone;
import pl.coderslab.cls_wms_app.repository.StorageZoneRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class StorageZoneServiceImpl implements StorageZoneService{
    private final StorageZoneRepository storageZoneRepository;

    @Autowired
    public StorageZoneServiceImpl(StorageZoneRepository storageZoneRepository) {
        this.storageZoneRepository = storageZoneRepository;
    }

    @Override
    public void add(StorageZone storageZone) {
        storageZoneRepository.save(storageZone);
    }

    @Override
    public List<StorageZone> getStorageZonesByWarehouseName(String warehouseName) {
        return storageZoneRepository.getStorageZoneByWarehouseName(warehouseName);
    }

    //for fixtures
    @Override
    public List<StorageZone> getStorageZones() {
        return storageZoneRepository.getStorageZones();
    }

    @Override
    public StorageZone findById(Long id) {
        return storageZoneRepository.getOne(id);
    }

    @Override
    public void deactivate(Long id) {
        StorageZone storageZone = storageZoneRepository.getOne(id);
        storageZone.setActive(false);
        storageZone.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        storageZone.setChangeBy(SecurityUtils.usernameForActivations());
        storageZoneRepository.save(storageZone);
    }

    @Override
    public void activate(Long id) {
        StorageZone storageZone = storageZoneRepository.getOne(id);
        storageZone.setActive(true);
        storageZone.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        storageZone.setChangeBy(SecurityUtils.usernameForActivations());
        storageZoneRepository.save(storageZone);
    }

    @Override
    public void remove(Long id) {
        storageZoneRepository.deleteById(id);
    }

    @Override
    public List<StorageZone> getDeactivatedStorageZones() {
        return storageZoneRepository.getDeactivatedStorageZones();
    }


}
