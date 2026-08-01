package com.hust.soict.ict.aims.services.notification;

import java.util.Set;

public interface NotificationService {
    <M extends NotificationMessage<E>, E> void send(Class<M> messageType, E payload);
    Set<NotificationMethod> getSupportedMethods();
}
