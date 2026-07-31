package com.hust.soict.ict.aims.services.notification.email;

import com.hust.soict.ict.aims.services.notification.NotificationMessage;
import com.hust.soict.ict.aims.services.notification.NotificationMethod;
import org.springframework.beans.factory.annotation.Value;

public interface EmailMessage<E> extends NotificationMessage<E> {
    String subject();
    String body();
    String recipient();

    abstract class Factory<M extends EmailMessage<E>, E> extends NotificationMessage.Factory<M, E> {
        @Value("${app.frontend.url}")
        protected String frontendUrl;

        protected Factory(Class<M> messageClass) {
            super(messageClass, NotificationMethod.EMAIL);
        }
    }
}
