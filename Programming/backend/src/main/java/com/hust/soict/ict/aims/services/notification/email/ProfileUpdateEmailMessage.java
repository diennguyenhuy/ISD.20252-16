package com.hust.soict.ict.aims.services.notification.email;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Email notification sent when a user's profile is updated (e.g., password changed).
 * Serves as an audit trail — the user is always informed of sensitive changes.
 *
 * <p>Follows the team's extensible notification pattern:
 * private constructor + inner {@link Factory} + record-based {@link Payload}.
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class ProfileUpdateEmailMessage implements EmailMessage {

    /** Immutable payload carrying the data needed to build this email. */
    public record Payload(String targetEmail, String username, String changeType) {}

    private final String targetEmail;
    private final String username;
    private final String changeType;

    @Override
    public String recipient() {
        return targetEmail;
    }

    @Override
    public String subject() {
        return "🔔 AIMS Account Security Alert: " + changeType;
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // HTML + Body + Container
        sb.append("<html><body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // Header (Blue theme for informational alerts)
        sb.append("<div style='background: linear-gradient(135deg, #1e40af, #3b82f6); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #dbeafe; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Security Alert</p>");
        sb.append("</div>");

        // Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Account Update Detected 🔔</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>This is a notification that the following change was made to your AIMS account:</p>");

        // Change details box
        sb.append("<div style='background-color: #eff6ff; padding: 20px; border-radius: 12px; border: 1px solid #bfdbfe; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #1e40af; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Change Type</p>");
        sb.append("<p style='margin: 10px 0 0 0; color: #1d4ed8; font-size: 18px; font-weight: bold;'>").append(changeType).append("</p>");
        sb.append("</div>");

        // Security notice
        sb.append("<div style='background-color: #fef2f2; padding: 15px; border-radius: 8px; border-left: 4px solid #ef4444; margin: 20px 0;'>");
        sb.append("<p style='margin: 0; color: #991b1b; font-size: 14px;'><strong>⚠ Didn't make this change?</strong> If you did not perform this action, please contact your system administrator immediately to secure your account.</p>");
        sb.append("</div>");

        sb.append("</div>");

        // Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>This is an automated security notification from AIMS.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    // --- Inner Factory (Spring-managed) ---

    @Component
    public static class Factory extends EmailMessage.Factory<ProfileUpdateEmailMessage, Payload> {
        @Override
        public Class<ProfileUpdateEmailMessage> messageType() {
            return ProfileUpdateEmailMessage.class;
        }

        @Override
        public ProfileUpdateEmailMessage createMessage(Payload payload) {
            return new ProfileUpdateEmailMessage(
                    payload.targetEmail(),
                    payload.username(),
                    payload.changeType());
        }
    }
}
