package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.exceptions.UnsupportedNotificationException;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
class NotificationServiceImpl implements NotificationService {
    private final Map<Class<? extends NotificationMessage<?>>, NotificationChannel<?>> channels = new HashMap<>();
    private final Map<Class<? extends NotificationMessage<?>>, NotificationMessage.Factory<?, ?>> factories = new HashMap<>();
    private final Set<NotificationMethod> methods = EnumSet.noneOf(NotificationMethod.class);

    public NotificationServiceImpl(
            List<NotificationChannel<?>> channels,
            List<NotificationMessage.Factory<?, ?>> factories
    ) {
        final Map<NotificationMethod, NotificationChannel<?>> methodToChannel = new EnumMap<>(NotificationMethod.class);
        channels.forEach(c -> {
            methodToChannel.put(c.method, c);
            methods.add(c.method);
        });

        factories.forEach(f -> {
            Class<? extends NotificationMessage<?>> messageType = f.messageClass;
            NotificationMethod method = f.method;

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
    private <M extends NotificationMessage<?>>
    NotificationChannel<M> getChannel(Class<M> messageType) {
        return (NotificationChannel<M>) Optional.ofNullable(channels.get(messageType))
                .orElseThrow(() -> new UnsupportedNotificationException("No channel registered for message type: " + messageType.getName()));
    }

    @SuppressWarnings("unchecked")
    private <M extends NotificationMessage<E>, E>
    NotificationMessage.Factory<M, E> getFactory(Class<M> messageType) {
        return (NotificationMessage.Factory<M, E>) Optional.ofNullable(factories.get(messageType))
                .orElseThrow(() -> new UnsupportedNotificationException("No factory registered for message type: " + messageType.getName()));
    }

    @Override
    public <M extends NotificationMessage<E>, E> void send(Class<M> messageType, E payload) {
        NotificationMessage.Factory<M, E> factory = getFactory(messageType);
        M message = factory.createMessage(payload);

        NotificationChannel<M> channel = getChannel(messageType);
        channel.send(message);
    }

    @Override
    public Set<NotificationMethod> getSupportedMethods() {
        return Collections.unmodifiableSet(methods);
    }
}
