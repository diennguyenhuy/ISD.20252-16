package com.hust.soict.ict.aims.services.order.event;

import com.hust.soict.ict.aims.models.entities.order.Order;

public record OrderSuccessEvent(Order order) {
}
