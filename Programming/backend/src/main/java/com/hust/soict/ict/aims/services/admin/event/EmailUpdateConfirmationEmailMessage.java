package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
class EmailUpdateConfirmationEmailMessage implements EmailMessage<EmailUpdateEvent> {
    private final String newEmail;
    private final String username;
    private final String frontendUrl;

    @Override
    public String recipient() {
        return newEmail;
    }

    @Override
    public String subject() {
        return "✉️ Confirmation: Your AIMS Account Email Has Been Updated";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML & Main Container (Using safe default fonts)
        sb.append("<!DOCTYPE html><html><head></head>");
        sb.append("<body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Header (Trustworthy Indigo/Blue theme)
        sb.append("<div style='background: linear-gradient(135deg, #3730a3, #4f46e5); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #e0e7ff; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Email Confirmed</p>");
        sb.append("</div>");

        // 3. Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Email Update Successful ✅</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>An administrator has successfully updated your AIMS account profile. This address is now your primary contact for all system notifications.</p>");

        // 4. Highlight Box for New Email
        sb.append("<div style='background-color: #f8fafc; padding: 20px; border-radius: 12px; border: 1px solid #e2e8f0; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #334155; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Your New Registered Email</p>");
        sb.append("<p style='margin: 10px 0 0 0; color: #4f46e5; font-size: 18px; font-weight: bold;'>").append(newEmail).append("</p>");
        sb.append("</div>");

        // 5. Instruction / Note
        sb.append("<p style='color: #52525b; font-size: 15px;'>Please ensure you use this new email address the next time you log in to the AIMS portal.</p>");

        // 6. Call to Action Button
        String loginUrl = frontendUrl + "/login";
        sb.append("<div style='text-align: center; margin: 35px 0 15px 0;'>");
        sb.append("<a href='").append(loginUrl).append("' ")
                .append("style='background-color: #4f46e5; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(79, 70, 229, 0.25); transition: background-color 0.2s;'>")
                .append("Log In Now")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>");

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>This is an automated notification from AIMS. Please do not reply.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Component
    static class Factory extends EmailMessage.Factory<EmailUpdateConfirmationEmailMessage, EmailUpdateEvent> {

        @Override
        public Class<EmailUpdateConfirmationEmailMessage> messageType() {
            return EmailUpdateConfirmationEmailMessage.class;
        }

        @Override
        public EmailUpdateConfirmationEmailMessage createMessage(EmailUpdateEvent payload) {
            return new EmailUpdateConfirmationEmailMessage(payload.newEmail(), payload.emailUpdatedUser().getUsername(), frontendUrl);
        }
    }
}
