package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.WorkDetails;

import java.util.List;

public interface WorkDetailsService {

    void add(WorkDetails workDetails);

    List<WorkDetails> getWorkDetails();

    WorkDetails findById(Long id);

    void save(WorkDetails workDetails);

    void delete(Long id);

    void edit(WorkDetails workDetails);

}
