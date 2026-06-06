package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.exceptions.UnsupportedNotificationMethod;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    private final Map<NotificationMethod, NotificationChannel<?>> channels = new EnumMap<>(NotificationMethod.class);
    private final Map<NotificationMethod, Map<Class<? extends NotificationMessage>, NotificationMessage.Factory<?, ?>>> factories = new EnumMap<>(NotificationMethod.class);

    public NotificationService(
            List<NotificationChannel<?>> channels,
            List<NotificationMessage.Factory<?, ?>> factories
    ) {
        channels.forEach(c -> this.channels.put(c.method(), c));
        factories.forEach(f ->
                this.factories.computeIfAbsent(f.supportedMethod(), m -> new HashMap<>())
                        .put(f.messageType(), f)
        );
    }

    @SuppressWarnings("unchecked")
    private <M extends NotificationMessage> NotificationChannel<M> getChannel(NotificationMethod method) {
        return (NotificationChannel<M>) Optional.ofNullable(this.channels.get(method))
                .orElseThrow(() -> new UnsupportedNotificationMethod("Unsupported notification method: " + method.name()));
    }

    @SuppressWarnings("unchecked")
    private <M extends NotificationMessage, P>
    NotificationMessage.Factory<M, P> getFactory(NotificationMethod method, Class<M> messageType) {
        var m = Optional.ofNullable(this.factories.get(method))
                .orElseThrow(() -> new UnsupportedNotificationMethod("Unsupported notification method: " + method.name()));
        return (NotificationMessage.Factory<M, P>) Optional.ofNullable(m.get(messageType))
                .orElseThrow(() -> new UnsupportedNotificationMethod("Mismatching notification message type: " + messageType.getName() + " while method is: " + method.name()));
    }


    public <M extends NotificationMessage> void send(NotificationMethod method, M message) {
        NotificationChannel<M> channel = getChannel(method);
        channel.send(message);
    }

    public <M extends NotificationMessage, P> void send(NotificationMethod method, Class<M> messageType, P payload) {
        NotificationMessage.Factory<M, P> factory = getFactory(method, messageType);
        send(method, factory.createMessage(payload));
    }
}
