package pl.coderslab.cls_wms_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.cls_wms_app.entity.EmailRecipients;

import java.util.List;

@Repository
public interface EmailRecipientsRepository extends JpaRepository<EmailRecipients, Long> {

    @Query("Select distinct er from EmailRecipients er join fetch er.company c join fetch Users u on u.company = c.name where u.username like ?1")
    List<EmailRecipients> getEmailRecipientsForCompanyByUsername(String username);

    @Query("Select distinct er from EmailRecipients er")
    List<EmailRecipients> getEmailRecipients();

    @Query("Select distinct er from EmailRecipients er join fetch er.company c join fetch er.emailTypes et where c.name = ?1 and et.type like ?2")
    List<EmailRecipients> getEmailRecipientsByCompanyForShipmentType(String company, String type);

    @Query("Select distinct er from EmailRecipients er join fetch er.company c join fetch er.emailTypes et where c.name = ?1 and et.type like ?2")
    List<EmailRecipients> getEmailRecipientsByCompanyForReceptionType(String company , String type);

    @Query("Select distinct er from EmailRecipients er join fetch er.company c join fetch er.emailTypes et where et.type like ?1")
    List<EmailRecipients> getEmailRecipientsByCompanyForStockType(String type);

    @Query("Select distinct er from EmailRecipients er where er.token = ?1")
    EmailRecipients getEmailRecipientsByToken(String token);

}
