package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Stock;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.repository.WorkDetailsRepository;

import javax.servlet.http.HttpSession;
import java.util.List;

public interface WorkDetailsService {

    void add(WorkDetails workDetails);

    List<WorkDetails> getWorkDetails();


    WorkDetails findById(Long id);

    void save(WorkDetails workDetails);

    void delete(Long id);

    void edit(WorkDetails workDetails);

    void pickUpGoods(String fromLocation,String enteredArticle, String enteredHdNumber , String equipment,String warehouse, String company,String workHandle,Long piecesQty,String workType) throws CloneNotSupportedException;

    void workLineFinish(WorkDetails workDetails,String scannerChosenEquipment);

    void workFinished(WorkDetails workDetails, HttpSession session);

    List<WorkDetailsRepository.WorkHeaderList> workHeaderList(String workDetailsWarehouse, String workDetailsCompany, String workDetailsArticle, String workDetailsType, String workDetailsHandle, String workDetailsHandleDevice, String workDetailsStatus, String workDetailsLocationFrom, String workDetailsLocationTo, String workDetailsWorkNumber);

    void createPutAwayWork(Long productionNumberToConfirm, HttpSession session) throws CloneNotSupportedException;

    void createTransferWork(Stock chosenStockPositional, Stock stock, String locationN);

    void closeWorkDetail(Long workNumber,String warehouseName);

    void changeStatusAfterStartWork(Long workNumber,String warehouseName);

    void assigningWorkLogic(HttpSession session,Long receptionNumber,String scannerChosenWarehouse);

    boolean receptionPutawayWorkSearch(HttpSession session,Long receptionNumber,String scannerChosenWarehouse);

    boolean stockTransferWorkSearch(HttpSession session, Long receptionNumber, String scannerChosenWarehouse);

    boolean productionPickingWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse);

    boolean productionProduceWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse);

    boolean productionPutawayWorkSearch(HttpSession session, Long productionNumber, String scannerChosenWarehouse);

}
