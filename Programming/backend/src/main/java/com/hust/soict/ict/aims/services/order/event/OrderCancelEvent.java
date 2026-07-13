package com.hust.soict.ict.aims.services.order.event;

import com.hust.soict.ict.aims.models.entities.order.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class OrderCancelEvent {
    private final Order order;
    @Setter
    private boolean refundable = false;

    public OrderCancelEvent(Order order) {
        this.order = order;
    }
}
