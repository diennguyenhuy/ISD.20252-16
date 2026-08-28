package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.order.Order;

public final class OrderRejectionEvent {
    final Order order;
    boolean refundable = false;

    public OrderRejectionEvent(Order order) {
        this.order = order;
    }
}
