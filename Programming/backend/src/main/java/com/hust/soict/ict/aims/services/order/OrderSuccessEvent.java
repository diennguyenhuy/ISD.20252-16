package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Encapsulates data related to a successful order event.
 * Coupling:
 * - Stamp coupling with Order and PaymentTransaction
 *   because full domain objects are carried in the event.
 */
@Getter
@AllArgsConstructor
class OrderSuccessEvent {
    private Order order;
    private PaymentTransaction paymentTransaction;
}
