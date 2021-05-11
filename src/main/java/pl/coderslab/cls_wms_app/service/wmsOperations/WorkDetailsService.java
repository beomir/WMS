package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.WorkDetails;

import java.util.List;

public interface WorkDetailsService {

    void add(WorkDetails workDetails);

    List<WorkDetails> getWorkDetails();

    List<WorkDetails> getWorkDetailsPerWarehouse(Long warehouseId);

    WorkDetails findById(Long id);

    void save(WorkDetails workDetails);

    void delete(Long id);

    void edit(WorkDetails workDetails);

    void pickUpGoods(String fromLocation,String enteredArticle, String enteredHdNumber , String equipment,String warehouse, String company);

    void workLineFinish(WorkDetails workDetails,String scannerChosenEquipment);

    void workFinished(WorkDetails workDetails);

}
