package com.hust.soict.ict.aims.services.notification.email;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderRejectionEmailMessage implements EmailMessage {
    private final Order order;
    private final String frontendUrl;

    @Override
    public String subject() {
        return "Important Update: Your AIMS Order #" + order.getId() + " has been Cancelled";
    }

    @Override
    public String recipient() {
        return order.getDeliveryInformation().getCustomerEmail();
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML & Main Container
        sb.append("<html><body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Cinematic Header (Destructive Red Theme for Rejection)
        sb.append("<div style='background: linear-gradient(135deg, #9f1239, #e11d48); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #ffe4e6; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Order Cancelled</p>");
        sb.append("</div>");

        // 3. Greeting & Bad News (Empathetic Tone)
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>We're sorry! \uD83D\uDE1E</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>Hello <strong>").append(order.getDeliveryInformation().getCustomerName()).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>We regret to inform you that we are unable to fulfill your order at this time. This occasionally happens due to sudden inventory shortages or delivery constraints in your area.</p>");

        // 4. Order Snapshot Box
        sb.append("<div style='background-color: #fff1f2; padding: 20px; border-radius: 12px; border: 1px solid #ffe4e6; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #e11d48; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Order Reference</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-family: monospace; color: #9f1239; font-size: 18px; font-weight: bold;'>").append(order.getId()).append("</p>");
        sb.append("</div>");

        // 5. Refund Information (CRITICAL: Adapts based on Payment Method)
        sb.append("<h3 style='color: #27272a; font-size: 16px; border-bottom: 2px solid #e4e4e7; padding-bottom: 8px;'>Refund Information</h3>");

        if (order.getPaymentTransaction() != null) {
            sb.append("<p style='color: #52525b; font-size: 15px;'>Your payment of <strong>")
                    .append(String.format("%,d", order.getPaymentTransaction().getAmountPaid()))
                    .append(" VND</strong> is fully protected.</p>");

            // Logic branch based on the Problem Statement rules
            if (Objects.equals(order.getPaymentTransaction().getTransactionMethod(), PaymentMethod.PAYPAL.name())) {
                sb.append("<div style='background-color: #f8fafc; border-left: 4px solid #3b82f6; padding: 15px; margin-top: 15px; font-size: 14px; color: #3f3f46;'>");
                sb.append("An automated refund has been initiated to your PayPal account. Please allow 3-5 business days for the funds to appear in your balance.");
                sb.append("</div>");
            } else if (Objects.equals(order.getPaymentTransaction().getTransactionMethod(), PaymentMethod.VIETQR.name())) {
                sb.append("<div style='background-color: #fefce8; border-left: 4px solid #eab308; padding: 15px; margin-top: 15px; font-size: 14px; color: #422006;'>");
                sb.append("Because you paid via VietQR bank transfer, our management team has been notified to manually process your refund. <strong>Our support team will contact you shortly</strong> to confirm your receiving bank details.");
                sb.append("</div>");
            }
        } else {
            sb.append("<p style='color: #52525b; font-size: 15px;'>No payment was charged for this order.</p>");
        }

        // 6. Dynamic Call to Action Button
        String orderLink = frontendUrl + "/order/" + order.getId();
        sb.append("<div style='text-align: center; margin: 40px 0 10px 0;'>");
        sb.append("<a href='").append(orderLink).append("' ")
                .append("style='background-color: #e11d48; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(225, 29, 72, 0.25); transition: background-color 0.2s;'>")
                .append("View Order Details")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>"); // Close main padding div

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>We apologize for the inconvenience. Reply to this email or call <strong>1800-AIMS</strong>.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");

        return sb.toString();
    }

    @Component
    public static class Factory extends EmailMessage.Factory<OrderRejectionEmailMessage, Order> {
        @Value("${app.frontend.url}")
        private String frontendUrl;

        @Override
        public Class<OrderRejectionEmailMessage> messageType() {
            return OrderRejectionEmailMessage.class;
        }

        @Override
        public OrderRejectionEmailMessage createMessage(Order payload) {
            return new  OrderRejectionEmailMessage(payload, frontendUrl);
        }
    }
}
