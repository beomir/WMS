package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.Article;
import pl.coderslab.cls_wms_app.entity.Reception;

import java.util.List;

public interface ReceptionService {

    void add(Reception reception);

    List<Reception> getReception(Long id);

    Reception findById(Long id);

    Reception get(Long id);

    void delete(Long id);

    void update(Reception reception);
}
