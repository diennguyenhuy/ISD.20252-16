package com.hust.soict.ict.aims.services.customer.event;

import com.hust.soict.ict.aims.models.entities.order.Order;

public record OrderCancelEvent(Order order) {
}
