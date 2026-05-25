package com.hust.soict.ict.aims.context;

import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.entities.order.Order;

/**
 * Cohesion: Functional Cohesion
 * Reason:
 * Defines a single abstraction for managing the lifecycle
 * of a draft Order during the ordering process.
 * Coupling:
 * - Stamp coupling with Order because complete Order
 *   aggregates are exchanged through the context.
 */
public interface OrderDraftContext {
    Order getDraftOrder() throws OrderNotPlacedException;
    void saveDraftOrder(Order order);
    void clearDraftOrder();
}
