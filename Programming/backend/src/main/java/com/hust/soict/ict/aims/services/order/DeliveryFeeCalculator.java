package com.hust.soict.ict.aims.services.order;

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
public class DeliveryFeeCalculator {
    private static final long UNIT_FEE_PER_WEIGHT_DEDUCTION = 2_500;
    private static final BigDecimal UNIT_WEIGHT_DEDUCTION = BigDecimal.valueOf(0.5);
    private static final long INITIAL_FEE = 30_000;
    private static final BigDecimal INITIAL_WEIGHT_DEDUCTION = BigDecimal.valueOf(0.5);
    private static final long INITIAL_FEE_FOR_HANOI_HCM = 22_000;
    private static final BigDecimal INITIAL_WEIGHT_DEDUCTION_FOR_HANOI_HCM = BigDecimal.valueOf(3);
    private static final long FREE_SHIPPING_THRESHOLD = 100_000;
    private static final long MAX_FREE_SHIPPING_SUBSIDY = 25_000;

    public long calculateDeliveryFee(
            BigDecimal totalWeight,
            String province,
            long totalPrice
    ) {
        long fee = 0;
        BigDecimal weight;
        if (isHanoiOrHoChiMinh(province)) {
            fee += INITIAL_FEE_FOR_HANOI_HCM;
            weight = totalWeight.subtract(INITIAL_WEIGHT_DEDUCTION_FOR_HANOI_HCM);
        } else {
            fee += INITIAL_FEE;
            weight = totalWeight.subtract(INITIAL_WEIGHT_DEDUCTION);
        }

        weight = weight.max(BigDecimal.ZERO);

        fee += UNIT_FEE_PER_WEIGHT_DEDUCTION * weight.divide(UNIT_WEIGHT_DEDUCTION, 0, RoundingMode.CEILING).longValueExact();

        if (totalPrice > FREE_SHIPPING_THRESHOLD) {
            fee -= Math.min(fee, MAX_FREE_SHIPPING_SUBSIDY);
        }

        return fee;
    }

    private boolean isHanoiOrHoChiMinh(String province) {
        return province.equalsIgnoreCase("Thành phố Hà Nội") || province.equalsIgnoreCase("Thành phố Hồ Chí Minh");
    }
}
