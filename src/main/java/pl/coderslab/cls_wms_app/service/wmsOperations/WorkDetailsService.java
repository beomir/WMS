package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;

import javax.servlet.http.HttpSession;
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

    void workFinished(WorkDetails workDetails, HttpSession session);

    List<WorkDetails> getWorkDetailsByCriteria(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType,String workDetailsHandle,String workDetailsHandleDevice,String workDetailsStatus,String workDetailsLocationFrom,String workDetailsLocationTo,String workDetailsWorkNumber);

    List<WorkDetailsRepository.WorkHeaderList> workHeaderList(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType, String workDetailsHandle, String workDetailsHandleDevice, String workDetailsStatus, String workDetailsLocationFrom, String workDetailsLocationTo, String workDetailsWorkNumber);

    void createPutAwayWork(Long productionNumberToConfirm, HttpSession session) throws CloneNotSupportedException;

    void closeWorkDetail(Long workNumber,String warehouseName);

    void changeStatusAfterStartWork(Long workNumber,String warehouseName);
}
