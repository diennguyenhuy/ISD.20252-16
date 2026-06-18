package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordResetEmailMessage implements EmailMessage {

    private final String targetEmail;
    private final String username;
    private final String temporaryPassword;
    private final String frontendUrl;

    @Override
    public String recipient() {
        return targetEmail;
    }

    @Override
    public String subject() {
        return "🔐 Action Required: Your AIMS Password Has Been Reset";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        sb.append("<!DOCTYPE html><html><head></head>");
        sb.append("<body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Header (Amber/Orange theme for security alerts)
        sb.append("<div style='background: linear-gradient(135deg, #b45309, #f59e0b); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='sans-serif; margin: 5px 0 0 0; color: #fef3c7; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Security Alert</p>");
        sb.append("</div>");

        // 3. Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Your password was reset</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>An administrator has reset the password for your AIMS account. Your new temporary password is securely generated below.</p>");

        // 4. Temporary password box
        sb.append("<div style='background-color: #fffbeb; padding: 20px; border-radius: 12px; border: 2px dashed #f59e0b; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #92400e; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Temporary Password</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-family: monospace; color: #b45309; font-size: 24px; font-weight: bold; letter-spacing: 3px;'>").append(temporaryPassword).append("</p>");
        sb.append("</div>");

        // 5. Warning & Instructions
        sb.append("<div style='background-color: #fef2f2; padding: 15px; border-radius: 8px; border-left: 4px solid #ef4444; margin: 20px 0;'>");
        sb.append("<p style='margin: 0; color: #991b1b; font-size: 14px;'><strong>⚠ Action Required:</strong> For your security, you must log in and change this password immediately. This temporary password will expire soon.</p>");
        sb.append("</div>");

        // 6. Call to Action Button
        String loginUrl = frontendUrl + "/login";
        sb.append("<div style='text-align: center; margin: 35px 0 15px 0;'>");
        sb.append("<a href='").append(loginUrl).append("' ")
                .append("style='background-color: #b45309; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(180, 83, 9, 0.25); transition: background-color 0.2s;'>")
                .append("Log In Now")
                .append("</a>");
        sb.append("</div>");

        sb.append("<p style='color: #71717a; font-size: 14px; margin-top: 30px;'>If you did not request this reset, please contact your system administrator immediately.</p>");
        sb.append("</div>");

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>This is an automated security notification from AIMS.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Component
    public static class Factory extends EmailMessage.Factory<PasswordResetEmailMessage, PasswordResetEvent> {
        @Override
        public Class<PasswordResetEmailMessage> messageType() {
            return PasswordResetEmailMessage.class;
        }

        @Override
        public PasswordResetEmailMessage createMessage(PasswordResetEvent payload) {
            return new PasswordResetEmailMessage(
                    payload.passwordResetUser().getEmail(),
                    payload.passwordResetUser().getUsername(),
                    payload.temporaryPassword(),
                    frontendUrl
            );
        }
    }
}
