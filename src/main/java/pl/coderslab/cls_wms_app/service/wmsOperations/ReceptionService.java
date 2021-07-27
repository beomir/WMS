package pl.coderslab.cls_wms_app.service.wmsOperations;

import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;

import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.List;

public interface ReceptionService {

    void add(Reception reception);

    void addNew(Reception reception,HttpSession session);

    void edit(Reception reception,HttpSession session);

    Reception findById(Long id);

    void finishReception(Long receptionNmbr,HttpSession session) ;

    Long lastReception();

    List<Reception> openedReceptions(Long id, String username);

    int qtyOfOpenedReceptions(Long id, String username);

    List<Integer> pallets();

    void closeCreation(Long receptionNumber,HttpSession session);

    void openCreation(Long receptionNumber,HttpSession session);

    void sendReceptions(String company);

    void insertFileContentToDB(File fsFile);

    void addNewReceptionLine(Reception reception,HttpSession session);

    void assignDoorLocationToReception(Long receptionNumber, Long doorLocation, HttpSession session);

    void finishUnloading(Long receptionNumber,HttpSession session);

    List<ReceptionRepository.ReceptionViewObject> receptionSummary(String company, String warehouse, String vendor, String status, String location, String receptionNumber, String hdNumber, String createdFrom, String createdTo, String createdBy);



}
