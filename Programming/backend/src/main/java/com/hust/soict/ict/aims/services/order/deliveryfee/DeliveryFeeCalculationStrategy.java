package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;

public interface DeliveryFeeCalculationStrategy {
    DeliveryFeeCalculationMethod method();
    long calculateDeliveryFee(Order.Draft order);
}
