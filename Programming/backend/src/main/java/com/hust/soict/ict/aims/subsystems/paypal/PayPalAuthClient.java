package com.hust.soict.ict.aims.subsystems.paypal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

/**
 * Obtains PayPal OAuth2 access tokens (client-credentials grant).
 *
 * <p>Subsystem-internal: only {@link PayPalOrdersClient} talks to it. Uses the
 * modern Spring {@code RestClient} (Boot 4 best practice).
 *
 * <p>Token caching is intentionally omitted for clarity; PayPal tokens live ~9h,
 * so a {@code @Cacheable} or in-memory expiry cache is an easy later optimization.
 */
@Component
@Slf4j
class PayPalAuthClient {

    private final PayPalProperties props;
    private final RestClient restClient;

    PayPalAuthClient(PayPalProperties props) {
        this.props = props;
        this.restClient = RestClient.builder().baseUrl(props.getBaseUrl()).build();
    }

    String getAccessToken() {
        try {
            PayPalApiModel.TokenResponse token = restClient.post()
                    .uri("/v1/oauth2/token")
                    .headers(h -> h.setBasicAuth(props.getClientId(), props.getClientSecret()))
                    .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                    .body("grant_type=client_credentials")
                    .retrieve()
                    .body(PayPalApiModel.TokenResponse.class);

            if (token == null || token.accessToken() == null || token.accessToken().isBlank()) {
                throw new PayPalApiException("PayPal returned an empty access token");
            }
            return token.accessToken();
        } catch (RestClientException e) {
            throw new PayPalApiException("Unable to authenticate with PayPal: " + e.getMessage(), e);
        }
    }
}
