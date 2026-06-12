package com.hust.soict.ict.aims.services.notification;

public interface NotificationChannel<M extends NotificationMessage> {
    NotificationMethod method();
    void send(M message);
}
