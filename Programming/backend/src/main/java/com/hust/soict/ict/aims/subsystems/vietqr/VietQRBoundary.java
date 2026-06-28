package com.hust.soict.ict.aims.subsystems.vietqr;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRController: DATA
 * + Reason: Only primitive Strings (authorizationHeader, accessToken, requestString)
 *   are exchanged — no composite objects or control flags are involved.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class VietQRBoundary {
    private static final String GET_TOKEN_URL = "/token_generate";
    private static final String GENERATE_QR_URL = "/qr/generate-customer";
    private static final String TEST_CALLBACK_URL = "/vqr/bank/api/test/transaction-callback";

    private final VietQRProperties props;
    private final HttpClient httpClient = HttpClient.newHttpClient();

    String getAccessToken(String authorizationHeader)
            throws IOException, InterruptedException {

        String url = props.getApiBaseUrl() + GET_TOKEN_URL;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Authorization", authorizationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("=== VIETQR GET TOKEN ===");
        log.info("URL: {}", url);
        log.info("STATUS: {}", response.statusCode());
        return response.body();
    }

    String generateQRCode(String accessToken, String requestString)
            throws IOException, InterruptedException {

        String url = props.getApiBaseUrl() + GENERATE_QR_URL;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("=== VIETQR GENERATE QR ===");
        log.info("URL: {}", url);
        log.info("STATUS: {}", response.statusCode());
        return response.body();
    }

    String checkPaymentStatus(String accessToken, String requestString)
            throws IOException, InterruptedException {

        String callbackUrl = props.getApiBaseUrl().replace("/vqr/api", TEST_CALLBACK_URL);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(callbackUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        log.info("=== VIETQR TEST CALLBACK ===");
        log.info("URL: {}", callbackUrl);
        log.info("STATUS: {}", response.statusCode());
        return response.body();
    }
}