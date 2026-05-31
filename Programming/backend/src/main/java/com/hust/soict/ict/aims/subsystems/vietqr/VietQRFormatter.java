package com.hust.soict.ict.aims.subsystems.vietqr;
/**
 * Utility class for formatting data specific to the VietQR API requirements.
 * 
 * Cohesion: High (Functional) - strictly handles formatting rules for VietQR.
 * Coupling: Low - Only depends on standard Java UUID, no business logic dependencies.
 */
public class VietQRFormatter {
    

    /**
     * Sanitize content to meet VietQR requirements:
     * - Max 23 characters
     * - No special characters
     * - Vietnamese without diacritics.
     */
    public static String sanitizeContent(String content) {
        if (content == null) return "";
        String sanitized = content.replaceAll("[^a-zA-Z0-9 ]", "");
        if (sanitized.length() > 23) {
            sanitized = sanitized.substring(0, 23);
        }
        return sanitized;
    }

    public static String sanitizeOrderId(String orderId) {
        if (orderId == null) return "";
        String sanitized = orderId.replaceAll("[^a-zA-Z0-9]", "");
        if (sanitized.length() > 13) {
            sanitized = sanitized.substring(0, 13);
        }
        return sanitized;
    }
}
