package com.hust.soict.ict.aims.subsystems.paypal;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Thin wrapper over the PayPal v2 <b>Payments</b> API endpoint AIMS needs for
 * refunds: {@code POST /v2/payments/captures/{capture_id}/refund}.
 *
 * <p>Kept separate from {@link PayPalOrdersClient} because it targets a different
 * PayPal API group ({@code /v2/payments/...} vs {@code /v2/checkout/orders/...});
 * keeping one client per endpoint group preserves high cohesion / SRP.
 *
 * <p>Subsystem-internal (package-private). Translates any non-2xx HTTP response
 * into a {@link PayPalApiException} so the {@link PayPalGatewayFacade} has a
 * single failure type to map to the core {@code PaymentException}.
 */
@Component
class PayPalPaymentsClient {

    private static final String REFUND_PATH = "/v2/payments/captures/{captureId}/refund";

    private final PayPalAuthClient authClient;
    private final RestClient restClient;

    PayPalPaymentsClient(PayPalProperties props, PayPalAuthClient authClient) {
        this.authClient = authClient;
        this.restClient = RestClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    /**
     * Refund a settled capture. A non-null {@code request} body carries the
     * (partial) amount; PayPal also accepts an empty body for a full refund, but
     * AIMS always sends an explicit converted amount.
     */
    PayPalApiModel.RefundResponse refundCapture(String captureId, PayPalApiModel.RefundRequest request) {
        String token = authClient.getAccessToken();
        try {
            return restClient.post()
                    .uri(REFUND_PATH, captureId)
                    .headers(h -> h.setBearerAuth(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(PayPalApiModel.RefundResponse.class);
        } catch (RestClientResponseException e) {
            throw new PayPalApiException(
                    "PayPal refund failed (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
        }
    }
}
