package com.hust.soict.ict.aims.services.order.deliveryfee;

import com.hust.soict.ict.aims.exceptions.UnsupportedDeliveryFeeStrategyException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class DeliveryFeeCalculatorImpl implements DeliveryFeeCalculator {
    private final Map<DeliveryFeeCalculationMethod, DeliveryFeeCalculationStrategy> strategyMap = new EnumMap<>(DeliveryFeeCalculationMethod.class);
    private final Set<DeliveryFeeCalculationMethod> methods = EnumSet.noneOf(DeliveryFeeCalculationMethod.class);

    DeliveryFeeCalculatorImpl(List<DeliveryFeeCalculationStrategy> strategies) {
        strategies.forEach(strategy -> {
            strategyMap.put(strategy.method, strategy);
            methods.add(strategy.method);
        });
    }

    @Override
    public long calculate(DeliveryFeeCalculationMethod method, Order.Draft draftOrder) {
        return Optional.ofNullable(strategyMap.get(method))
                .orElseThrow(() -> new UnsupportedDeliveryFeeStrategyException("Delivery fee calculation strategy not yet supported"))
                .calculateDeliveryFee(draftOrder);
    }

    @Override
    public Set<DeliveryFeeCalculationMethod> getSupportedMethods() {
        return Collections.unmodifiableSet(methods);
    }
}
