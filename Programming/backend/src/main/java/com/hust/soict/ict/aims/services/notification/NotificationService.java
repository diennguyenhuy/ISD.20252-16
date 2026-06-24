package com.hust.soict.ict.aims.services.notification;

public interface NotificationService {
    <M extends NotificationMessage<E>, E> void send(Class<M> messageType, E payload);
}
