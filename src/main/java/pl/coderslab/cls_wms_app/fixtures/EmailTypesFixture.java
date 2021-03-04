package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.EmailTypes;
import pl.coderslab.cls_wms_app.service.EmailTypesService;

import java.util.Arrays;
import java.util.List;

@Component
@Profile("local")
public class EmailTypesFixture {

    private final EmailTypesService emailTypesService;


    private List<EmailTypes> emailTypesList = Arrays.asList(
            new EmailTypes(null,"Shipment", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system" ),
            new EmailTypes(null,"Stock", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system" ),
            new EmailTypes(null,"Receptions", TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),true,"system" )
    );

    @Autowired
    public EmailTypesFixture(EmailTypesService emailTypesService) {
        this.emailTypesService = emailTypesService;
    }

    public void loadIntoDB() {
        for (EmailTypes emailTypes : emailTypesList){
            emailTypesService.add(emailTypes);
        }
    }

}
