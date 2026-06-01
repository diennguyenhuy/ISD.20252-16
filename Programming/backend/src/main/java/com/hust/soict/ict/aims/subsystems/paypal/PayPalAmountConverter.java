package com.hust.soict.ict.aims.subsystems.paypal;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Converts AIMS prices (stored as {@code Long} VND) into the 2-decimal money
 * string PayPal expects in the configured currency.
 *
 * <p>This currency concern is a <b>gateway detail</b> and is deliberately hidden
 * inside the subsystem — the AIMS core keeps thinking purely in VND.
 */
@Component
@RequiredArgsConstructor
class PayPalAmountConverter {

    private final PayPalProperties props;

    /** e.g. 250_000 VND with rate 25_000 → "10.00" */
    String toProviderValue(Long vndAmount) {
        return BigDecimal.valueOf(vndAmount)
                .divide(BigDecimal.valueOf(props.getVndToCurrencyRate()), 2, RoundingMode.HALF_UP)
                .toPlainString();
    }
}
