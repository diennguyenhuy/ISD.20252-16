package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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
