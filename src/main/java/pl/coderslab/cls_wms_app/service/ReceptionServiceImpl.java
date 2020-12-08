package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.Reception;
import pl.coderslab.cls_wms_app.repository.ReceptionRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReceptionServiceImpl implements ReceptionService{
    private ReceptionRepository receptionRepository;

    @Autowired
    public ReceptionServiceImpl(ReceptionRepository receptionRepository) {
        this.receptionRepository = receptionRepository;
    }

    @Override
    public void add(Reception reception) {
        receptionRepository.save(reception);
    }

    @Override
    public int getCreatedReceptionById(Long receptionNbr) {
       return receptionRepository.getCreatedReceptionById(receptionNbr);
    }

    @Override
    public void updateCloseCreationValue(Long receptionNbr) {
        if(getCreatedReceptionById(receptionNbr)>0){
            receptionRepository.updateCloseCreationValue(receptionNbr);
        }
    }


    @Override
    public List<Reception> getReceptions(Long id,String username) {
        return receptionRepository.getReceptions(id, username);
    }

    @Override
    public List<Reception> getReception(Long id) {
        return receptionRepository.getReception(id);
    }

    @Override
    public Reception findById(Long id) {
        return null;
    }


    @Override
    public void delete(Long id) {

    }

    @Override
    public void update(Reception reception) {

    }

    @Override
    public Long lastReception() {
        return receptionRepository.lastReception();
    }


    @Override
    public List<Reception> openedReceptions(Long id, String username) {
        return receptionRepository.openedReceptions(id, username);
    }

    @Override
    public int qtyOfOpenedReceptions(Long id, String username) {
        return receptionRepository.qtyOfOpenedReceptions(id, username);
    }


    @Override
    public List<Integer> pallets() {
        List<Integer> ret = new ArrayList<>();
        for (int i=1; i<=66; i++) {
            ret.add(i);
        }
        return ret;
    }

    @Override
    public void finished(Long id) {
        Reception reception = receptionRepository.getOne(id);
        reception.setFinished(true);
        receptionRepository.save(reception);
    }

    @Override
    public void closeCreation(Long id) {
        Reception reception = receptionRepository.getOne(id);
        reception.setCreation_closed(true);
        Long receptionNmbr = receptionRepository.getOne(id).getReceptionNumber();
        receptionRepository.updateCloseCreationValue(receptionNmbr);
    }

    @Override
    public void openCreation(Long id) {
        Reception reception = receptionRepository.getOne(id);
        reception.setCreation_closed(false);
        receptionRepository.save(reception);
    }

}
