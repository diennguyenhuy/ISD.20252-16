package com.hust.soict.ict.aims.services.notification.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OrderConfirmationEmailSender extends EmailNotificationChannel<OrderConfirmationEmail> {
    public OrderConfirmationEmailSender(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Override
    public Class<OrderConfirmationEmail> messageType() {
        return OrderConfirmationEmail.class;
    }

    @Override
    protected String notificationName() {
        return "order confirmation";
    }
}
