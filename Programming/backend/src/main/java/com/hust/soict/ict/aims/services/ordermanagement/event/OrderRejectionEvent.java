package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.order.Order;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
public final class OrderRejectionEvent {
    private final Order order;
    @Setter(AccessLevel.PACKAGE)
    private boolean refundable = false;

    public OrderRejectionEvent(Order order) {
        this.order = order;
    }
}
