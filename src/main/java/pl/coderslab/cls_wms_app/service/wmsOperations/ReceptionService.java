package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Reception;

import java.io.File;
import java.util.List;

public interface ReceptionService {

    void add(Reception reception);

    void addNew(Reception reception);

    void edit(Reception reception);

    List<Reception> getReceptions(Long id,String username);

    List<Reception> getReception(Long id);

//    int getCreatedReceptionById(Long receptionNbr);

//    void updateCloseCreationValue(Long receptionNbr);

    void updateFinishedReceptionValue(Long receptionNbrtoFinish);

    void insertDataToStockAfterFinishedReception(Long receptionNbr);

    Reception findById(Long id);

    void finishReception(Long receptionNmbr) ;

    Long lastReception();

    Long nextPalletNbr();

    List<Reception> openedReceptions(Long id, String username);

    int qtyOfOpenedReceptions(Long id, String username);

    List<Integer> pallets();

    void closeCreation(Long receptionNumber);

    void openCreation(Long receptionNumber);

    void sendReceptions(String company);

    void insertFileContentToDB(File fsFile);

    void addNewReceptionLine(Reception reception);

}
