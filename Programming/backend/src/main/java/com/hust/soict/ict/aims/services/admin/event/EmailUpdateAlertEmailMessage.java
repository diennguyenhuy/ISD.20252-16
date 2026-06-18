package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class EmailUpdateAlertEmailMessage implements EmailMessage {
    private final String oldEmail;
    private final String username;
    private final String frontendUrl;

    @Override
    public String recipient() {
        return oldEmail;
    }

    @Override
    public String subject() {
        return "⚠️ Security Alert: Your AIMS Account Email Has Been Changed";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML & Main Container (Safe Fonts)
        sb.append("<!DOCTYPE html><html><head></head>");
        sb.append("<body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Header (Amber/Orange Warning Theme)
        sb.append("<div style='background: linear-gradient(135deg, #b45309, #f59e0b); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #fef3c7; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Security Notice</p>");
        sb.append("</div>");

        // 3. Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Email Address Updated</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>This is a notice to inform you that a system administrator has successfully updated the primary email address associated with your AIMS account.</p>");

        // 4. Alert Box
        sb.append("<div style='background-color: #fffbeb; padding: 20px; border-radius: 12px; border-left: 4px solid #f59e0b; margin: 30px 0;'>");
        sb.append("<p style='margin: 0; color: #92400e; font-size: 15px;'>Future account notifications, including password resets, will no longer be sent to this email address.</p>");
        sb.append("</div>");

        // 5. Security Failsafe Instructions
        sb.append("<h3 style='color: #27272a; font-size: 16px; border-bottom: 2px solid #e4e4e7; padding-bottom: 8px;'>Did you request this change?</h3>");
        sb.append("<p style='color: #52525b; font-size: 15px;'>If you are aware of this change, no further action is needed on your part.</p>");
        sb.append("<p style='color: #991b1b; font-size: 15px; font-weight: bold;'>If you did NOT request this change, your account may be compromised.</p>");

        // 6. Call to Action Button (Route to a contact/support page or login page)
        sb.append("<div style='text-align: center; margin: 35px 0 15px 0;'>");
        sb.append("<a href='").append(frontendUrl).append("' ")
                .append("style='background-color: #18181b; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(0, 0, 0, 0.25); transition: background-color 0.2s;'>")
                .append("Contact Administrator")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>");

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>This is an automated security notification from AIMS. Please do not reply.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Component
    public static class Factory extends EmailMessage.Factory<EmailUpdateAlertEmailMessage, EmailUpdateEvent> {

        @Override
        public Class<EmailUpdateAlertEmailMessage> messageType() {
            return EmailUpdateAlertEmailMessage.class;
        }

        @Override
        public EmailUpdateAlertEmailMessage createMessage(EmailUpdateEvent payload) {
            return new EmailUpdateAlertEmailMessage(payload.oldEmail(), payload.emailUpdatedUser().getUsername(), frontendUrl);
        }
    }
}
