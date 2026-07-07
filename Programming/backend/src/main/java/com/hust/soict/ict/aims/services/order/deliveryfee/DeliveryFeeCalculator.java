package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.exceptions.UnsupportedDeliveryFeeStrategyException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class DeliveryFeeCalculator {
    private final Map<DeliveryFeeCalculationMethod, DeliveryFeeCalculationStrategy> strategyMap = new EnumMap<>(DeliveryFeeCalculationMethod.class);

    public DeliveryFeeCalculator(List<DeliveryFeeCalculationStrategy> strategies) {
        strategies.forEach(strategy -> strategyMap.put(strategy.method(), strategy));
    }

    public long calculate(DeliveryFeeCalculationMethod method, Order.Draft draftOrder) {
        return Optional.ofNullable(strategyMap.get(method))
                .orElseThrow(() -> new UnsupportedDeliveryFeeStrategyException("Delivery fee calculation strategy not yet supported"))
                .calculateDeliveryFee(draftOrder);
    }
}
