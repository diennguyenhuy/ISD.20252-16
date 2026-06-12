package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.exceptions.UnsupportedNotificationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    private final Map<Class<? extends NotificationMessage>, NotificationChannel<?>> channels = new HashMap<>();
    private final Map<Class<? extends NotificationMessage>, NotificationMessage.Factory<?, ?>> factories = new HashMap<>();

    public NotificationService(
            List<NotificationChannel<?>> channels,
            List<NotificationMessage.Factory<?, ?>> factories
    ) {
        final Map<NotificationMethod, NotificationChannel<?>> methodToChannel = new EnumMap<>(NotificationMethod.class);
        channels.forEach(c -> methodToChannel.put(c.method(), c));

        factories.forEach(f -> {
            Class<? extends NotificationMessage> messageType = f.messageType();
            NotificationMethod method = f.supportedMethod();

            this.factories.put(messageType, f);

            NotificationChannel<?> channel = methodToChannel.get(method);
            if (channel != null) {
                this.channels.put(messageType, channel);
            } else {
                throw new UnsupportedNotificationException("Unsupported notification channel for method: " + method);
            }
        });
    }

    @SuppressWarnings("unchecked")
    private <M extends NotificationMessage>
    NotificationChannel<M> getChannel(Class<? extends NotificationMessage> messageType) {
        return (NotificationChannel<M>) Optional.ofNullable(channels.get(messageType))
                .orElseThrow(() -> new UnsupportedNotificationException("No channel registered for message type: " + messageType.getName()));
    }

    @SuppressWarnings("unchecked")
    private <M extends NotificationMessage, P>
    NotificationMessage.Factory<M, P> getFactory(Class<M> messageType) {
        return (NotificationMessage.Factory<M, P>) Optional.ofNullable(factories.get(messageType))
                .orElseThrow(() -> new UnsupportedNotificationException("No factory registered for message type: " + messageType.getName()));
    }


    public <M extends NotificationMessage> void send(M message) {
        NotificationChannel<M> channel = getChannel(message.getClass());
        channel.send(message);
    }

    public <M extends NotificationMessage, P> void send(Class<M> messageType, P payload) {
        NotificationMessage.Factory<M, P> factory = getFactory(messageType);
        M message = factory.createMessage(payload);
        send(message);
    }
}
