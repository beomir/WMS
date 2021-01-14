package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.EmailTypes;

import java.util.List;


public interface EmailTypesService {

    void add(EmailTypes emailtypes);

    List<EmailTypes> getEmailTypes();

}
