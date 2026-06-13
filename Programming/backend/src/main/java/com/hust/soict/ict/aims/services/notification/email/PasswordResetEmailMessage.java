package com.hust.soict.ict.aims.services.notification.email;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Email notification sent when an administrator resets a user's password.
 *
 * <p>Follows the team's extensible notification pattern:
 * <ol>
 *     <li>Private constructor — only the inner {@link Factory} can instantiate.</li>
 *     <li>The Factory is a Spring {@code @Component} so it's auto-discovered by
 *         {@link com.hust.soict.ict.aims.services.notification.NotificationService}.</li>
 *     <li>Service layer invokes:
 *         {@code notificationService.send(PasswordResetEmailMessage.class, payload)}</li>
 * </ol>
 */
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetEmailMessage implements EmailMessage {

    /** Immutable payload record carrying the data needed to build this email. */
    public record Payload(String targetEmail, String username, String temporaryPassword) {}

    private final String targetEmail;
    private final String username;
    private final String temporaryPassword;

    @Override
    public String recipient() {
        return targetEmail;
    }

    @Override
    public String subject() {
        return "🔐 Your AIMS Account Password Has Been Reset";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // HTML + Body + Container
        sb.append("<html><body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // Header (Orange/Amber theme for security alerts)
        sb.append("<div style='background: linear-gradient(135deg, #b45309, #f59e0b); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #fef3c7; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Password Reset</p>");
        sb.append("</div>");

        // Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Your password has been reset 🔑</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>An administrator has reset the password for your AIMS account. Your new temporary password is shown below.</p>");

        // Temporary password box
        sb.append("<div style='background-color: #fffbeb; padding: 20px; border-radius: 12px; border: 2px dashed #f59e0b; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #92400e; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Temporary Password</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-family: monospace; color: #b45309; font-size: 22px; font-weight: bold; letter-spacing: 2px;'>").append(temporaryPassword).append("</p>");
        sb.append("</div>");

        // Warning
        sb.append("<div style='background-color: #fef2f2; padding: 15px; border-radius: 8px; border-left: 4px solid #ef4444; margin: 20px 0;'>");
        sb.append("<p style='margin: 0; color: #991b1b; font-size: 14px;'><strong>⚠ Important:</strong> You will be required to change this password immediately after your next login. This temporary password will expire with the session.</p>");
        sb.append("</div>");

        sb.append("<p style='color: #71717a; font-size: 14px;'>If you did not expect this reset, please contact your system administrator immediately.</p>");
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
    public static class Factory extends EmailMessage.Factory<PasswordResetEmailMessage, Payload> {
        @Override
        public Class<PasswordResetEmailMessage> messageType() {
            return PasswordResetEmailMessage.class;
        }

        @Override
        public PasswordResetEmailMessage createMessage(Payload payload) {
            return new PasswordResetEmailMessage(
                    payload.targetEmail(),
                    payload.username(),
                    payload.temporaryPassword());
        }
    }
}
