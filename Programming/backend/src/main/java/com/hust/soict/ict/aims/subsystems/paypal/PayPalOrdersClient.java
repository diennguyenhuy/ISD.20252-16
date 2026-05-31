package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalApiModel;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

/**
 * Thin wrapper over the two PayPal v2 Orders endpoints AIMS needs:
 * create-order and capture-order.
 *
 * <p>Subsystem-internal. Translates any non-2xx HTTP response into a
 * {@link PayPalApiException} so the {@link PayPalGatewayFacade} has a single
 * failure type to map.
 */
@Component
class PayPalOrdersClient {

    private static final String ORDERS_PATH = "/v2/checkout/orders";

    private final PayPalAuthClient authClient;
    private final RestClient restClient;

    PayPalOrdersClient(PayPalProperties props, PayPalAuthClient authClient) {
        this.authClient = authClient;
        this.restClient = RestClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    PayPalApiModel.OrderResponse createOrder(PayPalApiModel.CreateOrderRequest request) {
        String token = authClient.getAccessToken();
        try {
            return restClient.post()
                    .uri(ORDERS_PATH)
                    .headers(h -> h.setBearerAuth(token))
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .body(PayPalApiModel.OrderResponse.class);
        } catch (RestClientResponseException e) {
            throw new PayPalApiException(
                    "PayPal create-order failed (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
        }
    }

    PayPalApiModel.OrderResponse captureOrder(String providerOrderId) {
        String token = authClient.getAccessToken();
        try {
            return restClient.post()
                    .uri(ORDERS_PATH + "/{id}/capture", providerOrderId)
                    .headers(h -> {
                        h.setBearerAuth(token);
                        // Ask PayPal to return the full order representation so we can
                        // read the capture id / status without a second round-trip.
                        h.set("Prefer", "return=representation");
                    })
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("{}")
                    .retrieve()
                    .body(PayPalApiModel.OrderResponse.class);
        } catch (RestClientResponseException e) {
            throw new PayPalApiException(
                    "PayPal capture failed (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(), e);
        }
    }
}
