package com.hust.soict.ict.aims.subsystems.paypal;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Typed configuration for the PayPal subsystem (bound from {@code paypal.*}).
 *
 * <p>Kept inside the subsystem package: the AIMS core never reads these values.
 * Secrets are supplied via environment variables, never committed.
 */
@Component
@ConfigurationProperties(prefix = "paypal")
@Getter
@Setter
public class PayPalProperties {

    /** Sandbox by default; switch to {@code https://api-m.paypal.com} for live. */
    private String baseUrl = "https://api-m.sandbox.paypal.com";

    private String clientId;
    private String clientSecret;

    /**
     * Currency presented to PayPal. PayPal sandbox does not settle VND, so AIMS
     * prices (stored in VND) are converted to this currency for the gateway only.
     */
    private String currency = "USD";

    /** VND per 1 unit of {@link #currency}. Conversion is a gateway-only concern. */
    private double vndToCurrencyRate = 25_000d;

    /** Frontend route PayPal redirects to after approval (token is appended by PayPal). */
    private String returnUrl;

    /** Frontend route PayPal redirects to when the customer cancels. */
    private String cancelUrl;

    /** Shown on the PayPal hosted page. */
    private String brandName = "AIMS";
}
