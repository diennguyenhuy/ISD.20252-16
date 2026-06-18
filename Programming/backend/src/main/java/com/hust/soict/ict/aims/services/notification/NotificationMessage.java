package com.hust.soict.ict.aims.services.notification;

/**
 * The notification message interface that all notification messages
 * of the system implement. It is a marker interface, meaning there is
 * intentionally no methods, acting only as a way to convey that a class
 * is a notification message.
 * @param <E> The event/payload that a notification message can be built from.
 *           The type parameter is a "phantom type" whose primary purpose is
 *           to enforce strict typing and prevent misuse at compile time.
 */
public interface NotificationMessage<E> {

    interface Factory<M extends NotificationMessage<E>, E> {
        NotificationMethod supportedMethod();
        Class<M> messageType();
        M createMessage(E payload);
    }

}
