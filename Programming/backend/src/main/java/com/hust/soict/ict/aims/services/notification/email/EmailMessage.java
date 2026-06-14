package com.hust.soict.ict.aims.services.notification.email;

import com.hust.soict.ict.aims.services.notification.NotificationMessage;
import com.hust.soict.ict.aims.services.notification.NotificationMethod;
import org.springframework.beans.factory.annotation.Value;

public interface EmailMessage extends NotificationMessage {
    String subject();
    String body();
    String recipient();

    abstract class Factory<M extends EmailMessage, P> implements NotificationMessage.Factory<M, P> {
        @Value("${app.frontend.url}")
        protected String frontendUrl;

        @Override
        public final NotificationMethod supportedMethod() {
            return NotificationMethod.EMAIL;
        }
    }
}
