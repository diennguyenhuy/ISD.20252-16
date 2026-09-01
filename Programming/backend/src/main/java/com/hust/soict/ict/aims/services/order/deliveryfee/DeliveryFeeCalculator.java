package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.exceptions.UnsupportedDeliveryFeeStrategyException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.util.Set;

public interface DeliveryFeeCalculator {
    long calculate(DeliveryFeeCalculationMethod method, Order.Draft draftOrder);
    Set<DeliveryFeeCalculationMethod> getSupportedMethods();

    default long calculate(String method, Order.Draft draftOrder) {
        DeliveryFeeCalculationMethod m;
        try {
            m = DeliveryFeeCalculationMethod.valueOf(method.toUpperCase().strip());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedDeliveryFeeStrategyException(method, e);
        }
        return calculate(m, draftOrder);
    }
}
