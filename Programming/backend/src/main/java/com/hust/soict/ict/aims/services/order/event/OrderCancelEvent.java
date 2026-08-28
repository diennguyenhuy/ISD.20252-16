package com.hust.soict.ict.aims.services.order.event;

import com.hust.soict.ict.aims.models.entities.order.Order;

public final class OrderCancelEvent {
    final Order order;
    boolean refundable = false;

    public OrderCancelEvent(Order order) {
        this.order = order;
    }
}
