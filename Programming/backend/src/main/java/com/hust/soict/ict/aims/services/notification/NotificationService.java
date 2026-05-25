package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Defines a single abstraction for sending order notifications.
 * Coupling:
 * - Data coupling with listener modules through interface methods.
 * - Stamp coupling through Order and PaymentTransaction
 *   parameters.
 */
public interface NotificationService {
    void sendOrderConfirmation(Order order, PaymentTransaction paymentTransaction);
}
