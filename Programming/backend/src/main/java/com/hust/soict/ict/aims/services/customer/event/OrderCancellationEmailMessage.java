package com.hust.soict.ict.aims.services.customer.event;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import com.hust.soict.ict.aims.services.payment.PaymentMethod;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCancellationEmailMessage implements EmailMessage {
    private final Order order;
    private final String frontendUrl;

    @Override
    public String subject() {
        return "Confirmation: Your AIMS Order #" + order.getId() + " has been cancelled";
    }

    @Override
    public String recipient() {
        return order.getDeliveryInformation().getCustomerEmail();
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML, Fonts & Main Container
        sb.append("<html><body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Cinematic Header (Slate/Zinc Theme for Neutral Cancellation)
        sb.append("<div style='background: linear-gradient(135deg, #3f3f46, #52525b); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #e4e4e7; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Order Cancelled</p>");
        sb.append("</div>");

        // 3. Greeting & Confirmation
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Cancellation Confirmed</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>Hello <strong>").append(order.getDeliveryInformation().getCustomerName()).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>As per your request, your order has been successfully cancelled. No further action is required on your part to stop the shipment.</p>");

        // 4. Order Snapshot Box
        sb.append("<div style='background-color: #f4f4f5; padding: 20px; border-radius: 12px; border: 1px solid #e4e4e7; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #52525b; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Order Reference</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-family: monospace; color: #27272a; font-size: 18px; font-weight: bold;'>").append(order.getId()).append("</p>");
        sb.append("</div>");

        // 5. Refund Information (Same logic as Rejection)
        sb.append("<h3 style='color: #27272a; font-size: 16px; border-bottom: 2px solid #e4e4e7; padding-bottom: 8px;'>Refund Information</h3>");

        if (order.getPaymentTransaction() != null) {
            sb.append("<p style='color: #52525b; font-size: 15px;'>Since you had already paid <strong>")
                    .append(String.format("%,d", order.getPaymentTransaction().getAmountPaid()))
                    .append(" VND</strong>, a refund process has been initiated.</p>");

            if (Objects.equals(order.getPaymentTransaction().getTransactionMethod(), PaymentMethod.PAYPAL.name())) {
                sb.append("<div style='background-color: #f8fafc; border-left: 4px solid #3b82f6; padding: 15px; margin-top: 15px; font-size: 14px; color: #3f3f46;'>");
                sb.append("An automated refund has been sent to your PayPal account. Please allow 3-5 business days for the funds to reflect in your balance.");
                sb.append("</div>");
            } else if (Objects.equals(order.getPaymentTransaction().getTransactionMethod(), PaymentMethod.VIETQR.name())) {
                sb.append("<div style='background-color: #fefce8; border-left: 4px solid #eab308; padding: 15px; margin-top: 15px; font-size: 14px; color: #422006;'>");
                sb.append("Because you paid via VietQR, our finance team will process your refund manually. <strong>We will contact you shortly</strong> via email/phone to arrange the return transfer.");
                sb.append("</div>");
            }
        } else {
            sb.append("<p style='color: #52525b; font-size: 15px;'>Because this order was cancelled prior to payment, no charges were made to your account.</p>");
        }

        // 6. Call to Action Button (Back to Store)
        sb.append("<div style='text-align: center; margin: 40px 0 10px 0;'>");
        sb.append("<a href='").append(frontendUrl).append("' ")
                .append("style='background-color: #3f3f46; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(63, 63, 70, 0.25); transition: background-color 0.2s;'>")
                .append("Return to Store")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>"); // Close main padding div

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>If this cancellation was a mistake, please place a new order on our website.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");

        return sb.toString();
    }

    @Component
    public static class Factory extends EmailMessage.Factory<OrderCancellationEmailMessage, OrderCancelEvent> {
        @Override
        public Class<OrderCancellationEmailMessage> messageType() {
            return OrderCancellationEmailMessage.class;
        }

        @Override
        public OrderCancellationEmailMessage createMessage(OrderCancelEvent payload) {
            return new OrderCancellationEmailMessage(payload.order(), frontendUrl);
        }
    }
}
