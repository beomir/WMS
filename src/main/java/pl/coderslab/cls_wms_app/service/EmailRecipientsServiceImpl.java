package pl.coderslab.cls_wms_app.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.cls_wms_app.app.SecurityUtils;
import pl.coderslab.cls_wms_app.app.TimeUtils;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;
import pl.coderslab.cls_wms_app.repository.CompanyRepository;
import pl.coderslab.cls_wms_app.repository.EmailRecipientsRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class EmailRecipientsServiceImpl implements EmailRecipientsService{
    private final EmailRecipientsRepository emailRecipientsRepository;
    private final CompanyRepository companyRepository;

    @Autowired
    public EmailRecipientsServiceImpl(EmailRecipientsRepository emailRecipientsRepository, CompanyRepository companyRepository) {
        this.emailRecipientsRepository = emailRecipientsRepository;
        this.companyRepository = companyRepository;
    }


    @Override
    public void add(EmailRecipients emailRecipients) {
        emailRecipients.setToken(SecurityUtils.uuidToken());
        emailRecipientsRepository.save(emailRecipients);
    }

    @Override
    public void saveFromForm(EmailRecipients emailRecipients) {
        emailRecipients.setToken(SecurityUtils.uuidToken());
        emailRecipients.setActive(true);
        emailRecipients.setCreated(TimeUtils.timeNowLong());
        emailRecipients.setLast_update(TimeUtils.timeNowLong());
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        if(emailRecipients.getCompany()==null) {
            emailRecipients.setCompany(companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
        }
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        emailRecipientsRepository.save(emailRecipients);
    }

    @Override
    public void editFromForm(EmailRecipients emailRecipients) {
        emailRecipients.setId(emailRecipientsRepository.getEmailRecipientsByToken(emailRecipients.getToken()).getId());
        emailRecipients.setActive(emailRecipientsRepository.getEmailRecipientsByToken(emailRecipients.getToken()).isActive());
        emailRecipients.setCreated(TimeUtils.timeNowLong());
        emailRecipients.setLast_update(TimeUtils.timeNowLong());
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        if(emailRecipients.getCompany()==null) {
            emailRecipients.setCompany(companyRepository.getOneCompanyByUsername(SecurityUtils.usernameForActivations()));
        }
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        emailRecipients.setToken(SecurityUtils.uuidToken());
        emailRecipientsRepository.save(emailRecipients);
    }

    @Override
    public List<EmailRecipients> getEmailRecipientsForCompanyByUsername(String username) {
        return emailRecipientsRepository.getEmailRecipientsForCompanyByUsername(username);
    }

    @Override
    public List<EmailRecipients> getEmailRecipients() {
        return emailRecipientsRepository.getEmailRecipients();
    }

    @Override
    public EmailRecipients findByToken(String token) {
        return emailRecipientsRepository.getEmailRecipientsByToken(token);
    }

    @Override
    public void deactivate(String token) {
        EmailRecipients emailRecipients = emailRecipientsRepository.getEmailRecipientsByToken(token);
        emailRecipients.setActive(false);
        emailRecipients.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        emailRecipientsRepository.save(emailRecipients);
    }

    @Override
    public void activate(String token) {
        EmailRecipients emailRecipients = emailRecipientsRepository.getEmailRecipientsByToken(token);
        emailRecipients.setActive(true);
        emailRecipients.setLast_update(LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        emailRecipients.setChangeBy(SecurityUtils.usernameForActivations());
        emailRecipientsRepository.save(emailRecipients);
    }

    @Override
    public List<EmailRecipients> getEmailRecipientsByCompanyForShipmentType(String company, String type) {
        return emailRecipientsRepository.getEmailRecipientsByCompanyForShipmentType(company, type);
    }

    @Override
    public List<EmailRecipients> getEmailRecipientsByCompanyForReceptionType(String company, String type) {
        return emailRecipientsRepository.getEmailRecipientsByCompanyForReceptionType(company, type);
    }

    @Override
    public List<EmailRecipients> getEmailRecipientsByCompanyForStockType(String type) {
        return emailRecipientsRepository.getEmailRecipientsByCompanyForStockType(type);
    }
}
