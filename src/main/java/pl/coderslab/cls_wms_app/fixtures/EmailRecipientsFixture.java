package pl.coderslab.cls_wms_app.fixtures;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.Company;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.service.CompanyService;
import pl.coderslab.cls_wms_app.service.EmailRecipientsService;

import java.util.Arrays;
import java.util.List;

@Component
public class EmailRecipientsFixture {

    private final EmailRecipientsService emailRecipientsService;
    private final CompanyService companyService;


    private List<EmailRecipients> emailRecipientsList = Arrays.asList(
            new EmailRecipients(null, "anna_leszka@wp.pl", "Anna","Leszka","222-333-444",true, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system","Shipment",null,null),
            new EmailRecipients(null, "beomir89@gmail.com", "Andrzej","Leszka","555-666-777",true, TimeUtils.timeNowLong(),TimeUtils.timeNowLong(),"system","Shipment",null,null)
    );

    @Autowired
    public EmailRecipientsFixture(EmailRecipientsService emailRecipientsService1, CompanyService companyService) {
        this.emailRecipientsService = emailRecipientsService1;
        this.companyService = companyService;
    }

    public void loadIntoDB() {
        List<Company> companies = companyService.getCompany();

        for (EmailRecipients emailRecipients : emailRecipientsList) {
            emailRecipientsService.add(emailRecipients);
        }
        EmailRecipients emailRecipients1 = emailRecipientsList.get(0);
        EmailRecipients emailRecipients2 = emailRecipientsList.get(1);

        emailRecipients1.setCompany(companies.get(0));
        emailRecipients2.setCompany(companies.get(0));

        emailRecipientsService.add(emailRecipients1);
        emailRecipientsService.add(emailRecipients2);
    }
}
