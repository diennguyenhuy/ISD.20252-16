package com.hust.soict.ict.aims.services.notification;

public interface NotificationMessage {

    interface Factory<M extends NotificationMessage, P> {
        NotificationMethod supportedMethod();
        Class<M> messageType();
        M createMessage(P payload);
    }

}
