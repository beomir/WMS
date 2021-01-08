package pl.coderslab.cls_wms_app.service;

import pl.coderslab.cls_wms_app.entity.EmailRecipients;

import java.util.List;

public interface EmailRecipientsService {

    void add(EmailRecipients emailRecipients);

    void saveFromForm(EmailRecipients emailRecipients);

    void editFromForm(EmailRecipients emailRecipients);

    List<EmailRecipients> getEmailRecipientsForCompanyByUsername(String username);

    List<EmailRecipients> getEmailRecipients();

    EmailRecipients findByToken(String token);

    void deactivate(String token);

    void activate(String token);

    List<EmailRecipients> getEmailRecipientsByCompanyAndType(String company, String type);

}
