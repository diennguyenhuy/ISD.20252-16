package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.models.entities.order.Order;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Encapsulates data related to a successful order event.
 * Coupling:
 * - Stamp coupling with Order
 * because full domain objects are carried in the event.
 */
record OrderSuccessEvent(Order order) {
}
