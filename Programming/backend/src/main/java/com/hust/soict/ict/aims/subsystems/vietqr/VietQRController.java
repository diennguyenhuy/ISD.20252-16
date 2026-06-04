package com.hust.soict.ict.aims.subsystems.vietqr;

import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.io.IOException;
/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with VietQRBoundary: DATA
 * + Coupling level with Order: STAMP — only order.getId() and order.getTotalAmount()
 *   are used, but the full Order object is passed.
 */
public class VietQRController implements IPaymentQRCode {
    private final VietQRBoundary boundary;
    private String accessToken;
    private long tokenExpiryTime;

    // VietQR credentials
    private final String vietqrUsername;
    private final String vietqrPassword;

    // Bank account info for receiving payments
    private final String bankCode;
    private final String accountNo;
    private final String accountName;

    public VietQRController(String username, String password, String bankCode,
            String bankAccount, String userBankName, String apiBaseUrl) {
        this.boundary = new VietQRBoundary(apiBaseUrl);
        this.vietqrUsername = username;
        this.vietqrPassword = password;
        this.bankCode = bankCode;
        this.accountNo = bankAccount;
        this.accountName = userBankName;
    }

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
            QRAccessTokenRequest request = new QRAccessTokenRequest(vietqrUsername, vietqrPassword);
            String authHeader = request.buildAuthorizationHeader();

            String responseString = boundary.getAccessToken(authHeader);

            QRAccessTokenResponse response = new QRAccessTokenResponse();
            response.parseResponseString(responseString);

            if (!response.isValid()) {
                throw new InvalidTokenException("Failed to get valid access token");
            }

            this.accessToken = response.getAccessToken();
            this.tokenExpiryTime = System.currentTimeMillis() + (response.getExpiresIn() * 1000L);

            return this.accessToken;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new UnknownException("Thread interrupted", e);
        } catch (IOException e) {
            throw new UnknownException("Error while fetching access token", e);
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
    public QRCode generateQRCode(String orderId, long totalAmount) throws PaymentException {
        try {
            // Get valid access token
            String token = getValidAccessToken();

            // Use the formatter to explicitly sanitize strings
            orderId = VietQRFormatter.sanitizeOrderId(orderId);
            String content = VietQRFormatter.sanitizeContent("ORD" + orderId);

            // Create request with bank info from config
            QRGenerateRequest request = new QRGenerateRequest(
                    bankCode, accountNo, accountName,
                    content, totalAmount, orderId);
            String requestString = request.buildRequestString();

            String response = boundary.generateQRCode(token, requestString);

            QRCode qrCode = new QRCode();
            qrCode.parseQRCodeResponse(response);

            return qrCode;
        } catch (Exception e) {
            throw new UnknownException("Failed to generate QR code: ", e);
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
    public QRCodePaymentStatus checkPaymentStatus(String orderId, long totalAmount) throws PaymentException {
        try {
            String token = getValidAccessToken();

            orderId = VietQRFormatter.sanitizeOrderId(orderId);
            String content = VietQRFormatter.sanitizeContent("ORD" + orderId);

            // Create status check request
            QRTestCallbackRequest request = new QRTestCallbackRequest(
                    accountNo, content, totalAmount, bankCode
            );
            String requestString = request.buildRequestString();
            String response = boundary.checkPaymentStatus(token, requestString);
            QRCodePaymentStatus status = new QRCodePaymentStatus();
            status.parseResponseString(response);
            return status;
        } catch (Exception e) {
            throw new UnknownException("Failed to check payment status: " + e.getMessage(), e);
        }
    }

}
