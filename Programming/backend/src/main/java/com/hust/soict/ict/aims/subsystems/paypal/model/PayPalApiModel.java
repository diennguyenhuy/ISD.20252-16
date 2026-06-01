package com.hust.soict.ict.aims.subsystems.paypal.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * PayPal v2 Orders / OAuth wire model.
 *
 * <p>These records represent PayPal's JSON contract and live deep inside the
 * {@code subsystems.paypal.*} boundary. By convention nothing in the AIMS core
 * imports them — only the subsystem's own clients and facade do.
 * {@code @JsonInclude(NON_NULL)} keeps request bodies minimal.
 *
 * <p>This is a pure data holder (a "namespace" of records); the class itself is
 * never instantiated.
 */
public final class PayPalApiModel {

    private PayPalApiModel() {
    }

    /* ── OAuth2 ─────────────────────────────────────────────────────────── */

    public record TokenResponse(
            @JsonProperty("access_token") String accessToken,
            @JsonProperty("token_type") String tokenType,
            @JsonProperty("expires_in") long expiresIn) {
    }

    /* ── Create order (request) ─────────────────────────────────────────── */

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record CreateOrderRequest(
            String intent,
            @JsonProperty("purchase_units") List<PurchaseUnit> purchaseUnits,
            @JsonProperty("application_context") ApplicationContext applicationContext) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ApplicationContext(
            @JsonProperty("return_url") String returnUrl,
            @JsonProperty("cancel_url") String cancelUrl,
            @JsonProperty("user_action") String userAction,
            @JsonProperty("shipping_preference") String shippingPreference,
            @JsonProperty("brand_name") String brandName) {
    }

    /* ── Shared / response ──────────────────────────────────────────────── */

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record PurchaseUnit(
            @JsonProperty("reference_id") String referenceId,
            @JsonProperty("custom_id") String customId,
            Amount amount,
            Payments payments) {
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record Amount(
            @JsonProperty("currency_code") String currencyCode,
            String value) {
    }

    public record OrderResponse(
            String id,
            String status,
            List<Link> links,
            @JsonProperty("purchase_units") List<PurchaseUnit> purchaseUnits) {
    }

    public record Link(String href, String rel, String method) {
    }

    public record Payments(List<Capture> captures) {
    }

    public record Capture(String id, String status, Amount amount) {
    }
}
