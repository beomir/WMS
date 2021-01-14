package pl.coderslab.cls_wms_app.service;


import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.entity.EmailTypes;
import pl.coderslab.cls_wms_app.repository.EmailTypesRepository;

import java.util.List;


@Service
public class EmailTypesServiceImpl implements EmailTypesService{

    private final EmailTypesRepository emailTypesRepository;

    public EmailTypesServiceImpl(EmailTypesRepository emailTypesRepository) {
        this.emailTypesRepository = emailTypesRepository;
    }

    @Override
    public void add(EmailTypes emailTypes) {
        emailTypesRepository.save(emailTypes);
    }

    @Override
    public List<EmailTypes> getEmailTypes() {
        return emailTypesRepository.getEmailTypes();
    }
}
