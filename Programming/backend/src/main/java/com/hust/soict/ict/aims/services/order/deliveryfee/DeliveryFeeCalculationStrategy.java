package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;

abstract class DeliveryFeeCalculationStrategy {
    final DeliveryFeeCalculationMethod method;

    protected DeliveryFeeCalculationStrategy(DeliveryFeeCalculationMethod method) {
        this.method = method;
    }

    protected abstract long calculateDeliveryFee(Order.Draft order);
}
