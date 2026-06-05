package com.hust.soict.ict.aims.services.notification.email;

import com.hust.soict.ict.aims.services.notification.NotificationMessage;

interface EmailNotificationMessage extends NotificationMessage {
    String subject();
    String body();
    String recipient();
}
