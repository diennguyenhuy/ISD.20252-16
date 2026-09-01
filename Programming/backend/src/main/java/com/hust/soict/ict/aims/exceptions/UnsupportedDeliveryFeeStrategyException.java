package com.hust.soict.ict.aims.exceptions;

import com.hust.soict.ict.aims.services.order.deliveryfee.DeliveryFeeCalculationMethod;

public class UnsupportedDeliveryFeeStrategyException extends RuntimeException {
    public UnsupportedDeliveryFeeStrategyException(String method) {
        super("Delivery fee calculation strategy " + method + " not yet supported or not found");
    }

    public UnsupportedDeliveryFeeStrategyException(String method, Throwable cause) {
        super("Delivery fee calculation strategy " + method + " not yet supported or not found", cause);
    }

    public UnsupportedDeliveryFeeStrategyException(DeliveryFeeCalculationMethod method) {
        super("Delivery fee calculation strategy " + method + " not yet supported");
    }

    public UnsupportedDeliveryFeeStrategyException(DeliveryFeeCalculationMethod method, Throwable cause) {
        super("Delivery fee calculation strategy " + method + " not yet supported", cause);
    }
}
