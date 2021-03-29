package pl.coderslab.cls_wms_app.service.wmsSettings;

import pl.coderslab.cls_wms_app.entity.Extremely;
import pl.coderslab.cls_wms_app.entity.Transaction;
import pl.coderslab.cls_wms_app.temporaryObjects.TransactionSearch;

import java.util.List;

public interface ExtremelyService {

    void add(Extremely extremely);

    List<Extremely> getExtremely();

    Extremely findById(Long id);

    void save(Extremely extremely);

    void delete(Long id);


    void edit(Extremely extremely);

}
