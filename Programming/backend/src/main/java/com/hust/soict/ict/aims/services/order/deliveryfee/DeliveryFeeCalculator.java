package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;

import java.util.Set;

public interface DeliveryFeeCalculator {
    long calculate(DeliveryFeeCalculationMethod method, Order.Draft draftOrder);
    Set<DeliveryFeeCalculationMethod> getSupportedMethods();
}
