package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Reception;

import java.util.List;

public interface ReceptionService {

    void add(Reception reception);

    List<Reception> getReceptions(Long id,String username);

    List<Reception> getReception(Long id);

    int getCreatedReceptionById(Long receptionNbr);

    void updateCloseCreationValue(Long receptionNbr);

    void updateFinishedReceptionValue(Long receptionNbrtoFinish);

    void insertDataToStockAfterFinishedReception(Long receptionNbr);

    Reception findById(Long id);

    void delete(Long id);

    void update(Reception reception);

    Long lastReception();

    Long nextPalletNbr();

    List<Reception> openedReceptions(Long id, String username);

    int qtyOfOpenedReceptions(Long id, String username);

    List<Integer> pallets();

    //set finished reception line --> only one line
//    void finished(Long id);

    void closeCreation(Long id);

    void openCreation(Long id);
}
