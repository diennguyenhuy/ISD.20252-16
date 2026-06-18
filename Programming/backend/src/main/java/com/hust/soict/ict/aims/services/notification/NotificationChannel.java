package com.hust.soict.ict.aims.services.notification;

/**
 * The notification channel interface that all notification channels of the system implement
 * @param <M> The type of notification message that a channel can support sending. The channel
 *           does not care what the message is built from and how it is built
 */
public interface NotificationChannel<M extends NotificationMessage<?>> {
    NotificationMethod method();
    void send(M message);
}
