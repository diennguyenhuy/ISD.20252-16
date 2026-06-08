package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.models.entities.order.Order;

public record OrderRejectionEvent(Order order) {
}
