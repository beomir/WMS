package pl.coderslab.cls_wms_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Status;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.StatusRepository;

import java.util.List;

@Service
public class StatusServiceImpl implements StatusService{

    private StatusRepository statusRepository;

    @Autowired
    public StatusServiceImpl(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    @Override
    public void add(Status status) {
        statusRepository.save(status);
    }

    @Override
    public List<Status> getStatus() {
        return statusRepository.getStatus();
    }

    @Override
    public Status findById(Long id) {
        return null;
    }

    @Override
    public Status get(Long id) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Status status) {

    }
}
