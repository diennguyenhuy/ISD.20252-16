package com.hust.soict.ict.aims.subsystems.vietqr;

import com.hust.soict.ict.aims.IPaymentQRCode;
import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

import java.io.IOException;
/**
 * Cohesion level: FUNCTIONAL
 * Coupling level:
 *   - VietQRBoundary : DATA COUPLING
 *       Reason: VietQRController passes only primitive/simple values (String accessToken,
 *               String requestString, String authorizationHeader) to VietQRBoundary methods.
 *               Only the exact data needed for each HTTP call is passed; no composite object
 *               is transferred unnecessarily.
 *
 *   - QRAccessTokenRequest : DATA COUPLING
 *       Reason: VietQRController constructs a QRAccessTokenRequest with two primitive Strings
 *               (username, password) and then calls buildAuthorizationHeader() which returns a
 *               String. The interaction is limited to necessary data only.
 *
 *   - QRAccessTokenResponse: DATA COUPLING
 *
 *   - QRGenerateRequest: DATA COUPLING
 *
 *   - QRTestCallbackRequest: DATA COUPLING
 *
 *   - Order: STAMP COUPLING
 *       Reason: The full Order object is accepted as a parameter to generateQRCode() and
 *               checkPaymentStatus(), but only order.getTotalAmount() and order.getId() are
 *               actually used. The whole composite object is passed even though only a subset
 *               of its data is needed.
 *
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
     * @param order Order to generate QR code for
     * @return QRCode object containing QR information
     * @throws PaymentException if generation fails
     */
    @Override
    public QRCode generateQRCode(Order order) throws PaymentException {
        try {
            // Get valid access token
            String token = getValidAccessToken();

            long amount = (long) order.getTotalAmount();

            // Derive a short, alphanumeric orderId (≤13 chars, no dashes) so that
            // sanitizeOrderId() and sanitizeContent() in QRGenerateRequest both
            // reference the same value and don't truncate inconsistently.
            String orderId = toShortOrderId(order.getId());
            String content = "ORDER " + orderId;

            // Create request with bank info from config
            QRGenerateRequest request = new QRGenerateRequest(
                    bankCode, accountNo, accountName,
                    content, amount, orderId);
            String requestString = request.buildRequestString();

            String response = boundary.generateQRCode(token, requestString);

            QRCode qrCode = new QRCode();
            qrCode.parseQRCodeResponse(response);

            return qrCode;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getMessage());
            throw new UnknownException("Failed to generate QR code: ", e);
        }
    }

    /**
     * Check payment status for an order
     * 
     * @param order Order to check
     * @return PaymentStatus indicating current state
     * @throws PaymentException if check fails
     */
    @Override
    public QRCodePaymentStatus checkPaymentStatus(Order order) throws PaymentException {
        try {
            // Get valid access token
            String token = getValidAccessToken();

            long amount = (long) order.getTotalAmount();

            // Use the same short orderId derivation as generateQRCode() so the
            // content field in the test-callback request matches what was sent
            // during QR generation.
            String orderId = toShortOrderId(order.getId());
            String content = "ORDER " + orderId;

            // Create status check request
            QRTestCallbackRequest request = new QRTestCallbackRequest(
                    accountNo,
                    content,
                    amount,
                    bankCode
            );

            String requestString = request.buildRequestString();

            // Call API with Bearer token
            String response = boundary.checkPaymentStatus(token, requestString);

            // Parse response string to PaymentStatus
            QRCodePaymentStatus status = new QRCodePaymentStatus();
            status.parseResponseString(response);

            return status;
        } catch (Exception e) {
            throw new UnknownException("Failed to check payment status: " + e.getMessage(), e);
        }
    }

    /**
     * Converts a UUID to a compact, alphanumeric string suitable for VietQR's
     * {@code orderId} field (max 13 chars, no special characters).
     *
     * <p>Strategy: strip the hyphens from the UUID hex string, then take the
     * first 13 characters. This gives a stable, collision-resistant prefix that
     * both {@code generateQRCode()} and {@code checkPaymentStatus()} can derive
     * independently from the same {@link java.util.UUID}, ensuring the
     * {@code content} and {@code orderId} fields sent to the VietQR API always
     * reference the same value.
     *
     * @param id the order UUID
     * @return a 13-character lowercase hex string
     */
    private static String toShortOrderId(java.util.UUID id) {
        String hex = id.toString().replace("-", ""); // 32 lowercase hex chars
        return hex.substring(0, 13);                 // VietQR max orderId = 13 chars
    }
}
