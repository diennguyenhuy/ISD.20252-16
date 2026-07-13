package com.hust.soict.ict.aims.subsystems.vietqr;

import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRCode;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;

/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRBoundary: DATA
 * + Coupling level with Order: STAMP — only order.getId() and order.getTotalAmount()
 *   are used, but the full Order object is passed.
 */
@Component
@RequiredArgsConstructor
@Slf4j
class VietQRGatewayFacade implements VietQRPaymentGateway {
    private final VietQRBoundary boundary;
    private final VietQRProperties props;
    
    private String accessToken;
    private long tokenExpiryTime;

    /**
     * Get valid access token (renew if expired)
     * Token expires in 300 seconds (5 minutes)
     * 
     * @return Valid access token
     * @throws PaymentException if token generation fails
     */
    private synchronized String getValidAccessToken() throws PaymentException {
        long currentTime = System.currentTimeMillis();

        // Check if token is still valid (with 30 seconds buffer)
        if (this.accessToken != null && currentTime < tokenExpiryTime - 30000) {
            return this.accessToken;
        }

        try {
            // Get new token
            QRAccessTokenRequest request = new QRAccessTokenRequest(props.getUsername(), props.getPassword());
            String authHeader = request.buildAuthorizationHeader();

            String responseString = boundary.getAccessToken(authHeader);

            ObjectMapper mapper = new ObjectMapper();
            QRAccessTokenResponse response = mapper.readValue(responseString, QRAccessTokenResponse.class);

            if (!response.isValid()) {
                throw new InvalidTokenException("Failed to get valid access token");
            }

            this.accessToken = response.getAccessToken();
            this.tokenExpiryTime = System.currentTimeMillis() + (response.getExpiresIn() * 1000L);

            return this.accessToken;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UnknownPaymentException("Thread interrupted", e);
        } catch (IOException e) {
            throw new UnknownPaymentException("Error while fetching access token", e);
        }
    }

    /**
     * Generate QR code for payment using VietQR service
     * 
     * @param orderId Order ID to generate QR code for
     * @param totalAmount total amount to be paid
     * @return QRCode object containing QR information
     * @throws PaymentException if generation fails
     */
    @Override
    public VietQRCode generateQRCode(String orderId, long totalAmount) throws PaymentException {
        try {
            // Get valid access token
            String token = getValidAccessToken();

            // Use the formatter to explicitly sanitize strings
            orderId = VietQRFormatter.sanitizeOrderId(orderId);
            String content = VietQRFormatter.sanitizeContent("ORD" + orderId);

            // Create request with bank info from config
            QRGenerateRequest request = new QRGenerateRequest(
                    props.getBankCode(), props.getAccountNo(), props.getAccountName(),
                    content, totalAmount, orderId);
            String requestString = request.buildRequestString();

            String response = boundary.generateQRCode(token, requestString);

            ObjectMapper mapper = new ObjectMapper();
            VietQRCode vietQrCode = mapper.readValue(response, VietQRCode.class);

            return vietQrCode;
        } catch (Exception e) {
            throw new UnknownPaymentException("Failed to generate QR code: ", e);
        }
    }

    /**
     * Check payment status for an order
     * 
     * @param orderId Order ID to check
     * @param totalAmount total amount to be paid
     * @return PaymentStatus indicating current state
     * @throws PaymentException if check fails
     */
    @Override
    public VietQRPaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException {
        try {
            String token = getValidAccessToken();

            orderId = VietQRFormatter.sanitizeOrderId(orderId);
            String content = VietQRFormatter.sanitizeContent("ORD" + orderId);

            // Create status check request
            QRTestCallbackRequest request = new QRTestCallbackRequest(
                    props.getAccountNo(), content, totalAmount, props.getBankCode()
            );
            String requestString = request.buildRequestString();
            String response = boundary.checkPaymentStatus(token, requestString);
            ObjectMapper mapper = new ObjectMapper();
            VietQRPaymentStatus status = mapper.readValue(response, VietQRPaymentStatus.class);
            return status;
        } catch (Exception e) {
            throw new UnknownPaymentException("Failed to check payment status: " + e.getMessage(), e);
        }
    }

}
