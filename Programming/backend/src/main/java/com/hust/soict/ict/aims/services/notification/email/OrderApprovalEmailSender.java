package com.hust.soict.ict.aims.services.notification.email;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class OrderApprovalEmailSender extends EmailNotificationChannel<OrderApprovalEmail> {
    public OrderApprovalEmailSender(JavaMailSender mailSender) {
        super(mailSender);
    }

    @Override
    public Class<OrderApprovalEmail> messageType() {
        return OrderApprovalEmail.class;
    }

    @Override
    protected String notificationName() {
        return "order approval";
    }
}
