package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;

abstract class DeliveryFeeCalculationStrategy {
    private final DeliveryFeeCalculationMethod method;

    protected DeliveryFeeCalculationStrategy(DeliveryFeeCalculationMethod method) {
        this.method = method;
    }

    final DeliveryFeeCalculationMethod method() {
        return method;
    }

    protected abstract long calculateDeliveryFee(Order.Draft order);
}
