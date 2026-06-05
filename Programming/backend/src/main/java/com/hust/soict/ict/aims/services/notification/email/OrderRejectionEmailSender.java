package com.hust.soict.ict.aims.services.notification.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OrderRejectionEmailSender extends EmailNotificationChannel<OrderRejectionEmail> {
    public OrderRejectionEmailSender(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Override
    public Class<OrderRejectionEmail> messageType() {
        return OrderRejectionEmail.class;
    }


    @Override
    protected String notificationName() {
        return "order rejection";
    }
}
