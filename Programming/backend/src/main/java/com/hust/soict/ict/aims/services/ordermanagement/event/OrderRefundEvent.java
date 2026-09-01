package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.order.Order;

public record OrderRefundEvent(Order order) {}
