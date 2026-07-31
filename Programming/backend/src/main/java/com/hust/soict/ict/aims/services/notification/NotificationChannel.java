package com.hust.soict.ict.aims.services.notification;

/**
 * The notification channel interface that all notification channels of the system implement
 * @param <M> The type of notification message that a channel can support sending. The channel
 *           does not care what the message is built from and how it is built
 */
public abstract class NotificationChannel<M extends NotificationMessage<?>> {
    private final NotificationMethod method;

    protected NotificationChannel(NotificationMethod method) {
        this.method = method;
    }

    final NotificationMethod method() {
        return method;
    }

    protected abstract void send(M message);
}
