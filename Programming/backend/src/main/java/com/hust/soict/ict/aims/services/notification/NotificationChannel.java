package com.hust.soict.ict.aims.services.notification;

public interface NotificationChannel<M extends NotificationMessage> {
    Class<M> messageType();
    NotificationMethod method();
    void send(M message);
}
