package com.hust.soict.ict.aims.services.admin.event;

import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import org.springframework.stereotype.Component;

class AccountCreatedEmailMessage implements EmailMessage<AccountCreatedEvent> {
    private final String targetEmail;
    private final String username;
    private final String temporaryPassword;
    private final String frontendUrl;

    private AccountCreatedEmailMessage(String targetEmail, String username, String temporaryPassword, String frontendUrl) {
        this.targetEmail = targetEmail;
        this.username = username;
        this.temporaryPassword = temporaryPassword;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public String recipient() {
        return targetEmail;
    }

    @Override
    public String subject() {
        return "👋 Welcome to AIMS! Your Account Has Been Created";
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML & Main Container (Using safe default fonts)
        sb.append("<!DOCTYPE html><html><head></head>");
        sb.append("<body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Header (Welcoming Indigo/Blue theme)
        sb.append("<div style='background: linear-gradient(135deg, #3730a3, #4f46e5); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #e0e7ff; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Welcome Aboard</p>");
        sb.append("</div>");

        // 3. Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Your account is ready! 🚀</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(username).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>An administrator has successfully set up your new AIMS account. You can now log in to the portal using the credentials provided below.</p>");

        // 4. Credentials Box
        sb.append("<div style='background-color: #f0fdf4; padding: 20px; border-radius: 12px; border: 1px solid #bbf7d0; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #166534; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Your Login Details</p>");
        sb.append("<p style='margin: 15px 0 5px 0; color: #15803d; font-size: 16px;'>Username: <strong style='color: #14532d; font-size: 18px;'>").append(username).append("</strong></p>");
        sb.append("<p style='margin: 0; color: #15803d; font-size: 16px;'>Temporary Password: <strong style='font-family: monospace; color: #14532d; font-size: 20px; letter-spacing: 1px;'>").append(temporaryPassword).append("</strong></p>");
        sb.append("</div>");

        // 5. Instruction / Note
        sb.append("<div style='background-color: #f8fafc; padding: 15px; border-radius: 8px; border-left: 4px solid #3b82f6; margin: 20px 0;'>");
        sb.append("<p style='margin: 0; color: #0f172a; font-size: 14px;'><strong>💡 Note:</strong> For your security, you will be required to change your password immediately after your first login.</p>");
        sb.append("</div>");

        // 6. Call to Action Button
        String loginUrl = frontendUrl + "/login";
        sb.append("<div style='text-align: center; margin: 35px 0 15px 0;'>");
        sb.append("<a href='").append(loginUrl).append("' ")
                .append("style='background-color: #4f46e5; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(79, 70, 229, 0.25); transition: background-color 0.2s;'>")
                .append("Go to Login Page")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>");

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>If you have any questions, please contact your system administrator.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Component
    static class Factory extends EmailMessage.Factory<AccountCreatedEmailMessage, AccountCreatedEvent> {
        Factory() {
            super(AccountCreatedEmailMessage.class);
        }

        @Override
        public AccountCreatedEmailMessage createMessage(AccountCreatedEvent payload) {
            return new AccountCreatedEmailMessage(
                    payload.createdUser().getEmail(),
                    payload.createdUser().getUsername(),
                    payload.temporaryPassword(),
                    frontendUrl
            );
        }
    }
}