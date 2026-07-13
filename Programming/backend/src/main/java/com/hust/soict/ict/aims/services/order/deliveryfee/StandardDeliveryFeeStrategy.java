package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Cohesion: Functional Cohesion<br>
 * Reason:
 * All attributes, constants, and methods contribute solely
 * to delivery fee calculation logic.
 * Coupling: Data coupling with DeliveryService because only
 * required primitive/simple parameters are passed.
 * Stamp coupling avoided by receiving only necessary data
 * instead of entire Order objects.
 */
@Component
class StandardDeliveryFeeStrategy extends BaseStandardDeliveryFeeStrategy {
    @Override
    public DeliveryFeeCalculationMethod method() {
        return DeliveryFeeCalculationMethod.STANDARD;
    }


    @Override
    protected BigDecimal calculateChargeableWeight(Order.Draft order) {
        return order.getTotalWeight();
    }
}
