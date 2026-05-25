package com.hust.soict.ict.aims.subsystems.vietqr;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
/**
 * + Cohesion level: FUNCTIONAL
 * + Coupling level level with VietQRController: DATA
 * + Reason: VietQRBoundary is only called by VietQRController. The controller passes
 *           individual String parameters (authorizationHeader, accessToken, requestString)
 *           and receives back a raw JSON String. Only the exact necessary data is exchanged;
 *           no composite objects or control flags are involved.
 */
class VietQRBoundary {

    private static final String GET_TOKEN_URL = "/token_generate";
    private static final String GENERATE_QR_URL = "/qr/generate-customer";
    private static final String TEST_CALLBACK_URL = "/vqr/bank/api/test/transaction-callback";

    private final HttpClient httpClient;
    private final String apiBaseUrl;

    VietQRBoundary(String apiBaseUrl) {
        this.apiBaseUrl = apiBaseUrl;
        this.httpClient = HttpClient.newHttpClient();
    }

    String getAccessToken(String authorizationHeader)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + GET_TOKEN_URL))
                .header("Authorization", authorizationHeader)
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    String generateQRCode(String accessToken, String requestString)
            throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiBaseUrl + GENERATE_QR_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }

    String checkPaymentStatus(String accessToken, String requestString)
            throws IOException, InterruptedException {

        String callbackUrl = apiBaseUrl.replace("/vqr/api", TEST_CALLBACK_URL);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(callbackUrl))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + accessToken)
                .POST(HttpRequest.BodyPublishers.ofString(requestString))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        return response.body();
    }
}