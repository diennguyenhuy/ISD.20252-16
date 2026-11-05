package com.hust.soict.ict.aims.payment.paypal;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;

/**
 * Typed configuration for the PayPal subsystem (bound from {@code paypal.*}).
 *
 * <p>Kept inside the subsystem package: the AIMS core never reads these values.
 * Secrets are supplied via environment variables, never committed.
 * @param baseUrl Sandbox by default; switch to {@code https://api-m.paypal.com} for live.
 * @param currency Currency presented to PayPal. PayPal sandbox does not settle VND, so AIMS
 *                 prices (stored in VND) are converted to this currency for the gateway only.
 * @param vndToCurrencyRate VND per 1 unit of {@link #currency}. Conversion is a gateway-only concern.
 * @param returnUrl Frontend route PayPal redirects to after approval (token is appended by PayPal).
 * @param cancelUrl Frontend route PayPal redirects to when the customer cancels.
 * @param brandName Shown on the PayPal hosted page.
 */
@ConfigurationProperties(prefix = "paypal")
record PayPalProperties(
        @DefaultValue("https://api-m.sandbox.paypal.com")
        String baseUrl,
        String clientId,
        String clientSecret,
        @DefaultValue("USD")
        String currency,
        @DefaultValue("26000")
        double vndToCurrencyRate,
        String returnUrl,
        String cancelUrl,
        @DefaultValue("AIMS")
        String brandName
) {}
