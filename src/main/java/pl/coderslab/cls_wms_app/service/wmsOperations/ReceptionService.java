package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.entity.WorkDetails;
import pl.coderslab.cls_wms_app.temporaryObjects.ReceptionSearch;

import java.io.File;
import java.util.List;

public interface ReceptionService {

    void add(Reception reception);

    void addNew(Reception reception);

    void edit(Reception reception);


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

    void assignDoorLocationToReception(Long receptionNumber, Long doorLocation);

    void finishUnloading(Long receptionNumber);

    void save(ReceptionSearch receptionSearch);

}
