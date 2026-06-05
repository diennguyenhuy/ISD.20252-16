package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.exceptions.UnsupportedNotificationMethod;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.notification.email.OrderConfirmationEmail;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NotificationService {
    private final Map<NotificationMethod, Map<Class<? extends NotificationMessage>, NotificationChannel<?>>> notificationChannels;

    public NotificationService(List<NotificationChannel<?>> notificationChannels) {
        this.notificationChannels = new EnumMap<>(NotificationMethod.class);
        notificationChannels.forEach(c ->
                this.notificationChannels.computeIfAbsent(
                c.method(),
                k -> new HashMap<>()
        ).putIfAbsent(c.messageType(), c));
    }

    @SuppressWarnings("unchecked")
    public <M extends NotificationMessage> void send(NotificationMethod method, M message) {
        NotificationChannel<M> channel = (NotificationChannel<M>) Optional.ofNullable(
                Optional.ofNullable(notificationChannels.get(method))
                .orElseThrow(() -> new UnsupportedNotificationMethod("Unsupported notification method: " + method.name()))
                        .get(message.getClass())
        ).orElseThrow(() -> new UnsupportedNotificationMethod("Mismatching notification message type: " + message.getClass().getName() + " while method is: " + method.name()));
        channel.send(message);
    }
}
