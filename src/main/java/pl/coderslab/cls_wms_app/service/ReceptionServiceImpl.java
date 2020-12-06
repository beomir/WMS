package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
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
    public List<Reception> getReceptions() {
        return receptionRepository.getReceptions();
    }

    @Override
    public List<Integer> pallets() {
        List<Integer> ret = new ArrayList<>();
        for (int i=1; i<=66; i++) {
            ret.add(i);
        }
        return ret;
    }

}
