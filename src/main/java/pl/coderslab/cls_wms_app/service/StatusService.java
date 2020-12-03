package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Status;

import java.util.List;

public interface StatusService {

    void add(Status status);

    List<Status> getStatus();

    Status findById(Long id);


    Status get(Long id);

    void delete(Long id);

    void update(Status status);
}
