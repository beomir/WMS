package pl.coderslab.cls_wms_app.app;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class SendEmailService {

    private final JavaMailSender javaMailSender;


    public SendEmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;

    }


    public void sendEmailFromContactForm(String body, String email){
        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            message.setFrom("clswarehousemanagementsystem@gmail.com");
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Contact Form");
            message.setText(body, "UTF-8", "html");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        javaMailSender.send(message);
    }

    public void sendEmail(String to, String body, String topic,String fileName) {

        MimeMessage message = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,true);

            helper.setFrom("clswarehousemanagementsystem@gmail.com");
            helper.setTo(to);
            helper.setSubject(topic);
            helper.setText(body,true);

            FileSystemResource file = new FileSystemResource(fileName);
            helper.addAttachment(file.getFilename(),file);
            javaMailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }


}
