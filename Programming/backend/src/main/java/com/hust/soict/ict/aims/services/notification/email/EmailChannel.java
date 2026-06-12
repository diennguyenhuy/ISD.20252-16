package com.hust.soict.ict.aims.services.notification.email;

import com.hust.soict.ict.aims.services.notification.NotificationChannel;
import com.hust.soict.ict.aims.services.notification.NotificationMethod;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
@Slf4j
public class EmailChannel implements NotificationChannel<EmailMessage> {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    protected String from;

    @Override
    public final NotificationMethod method() {
        return NotificationMethod.EMAIL;
    }

    @Override
    public void send(EmailMessage message) {
        log.debug("Sending {} email...", notificationName(message));
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(message.recipient());
            helper.setSubject(message.subject());
            helper.setText(message.body(), true);
            mailSender.send(mimeMessage);
            log.debug("Successfully sent {} email!", notificationName(message));
        } catch (MessagingException e) {
            log.warn("Multipart creation failed: {}", e.getMessage());
        } catch (MailAuthenticationException e) {
            log.warn("Mail authentication failed: {}", e.getMessage());
        } catch (MailSendException e) {
            log.warn("Could not send email: {}", e.getMessage());
        } catch (MailException e) {
            log.warn("Mail exception: {}", e.getMessage());
        }
    }

    private String notificationName(EmailMessage em) {
        return em.getClass().getSimpleName().replaceAll("([A-Z])", " $1").trim().toLowerCase();
    }
}
