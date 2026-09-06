package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import org.springframework.stereotype.Component;

class OrderRefundEmailMessage implements EmailMessage<OrderRefundEvent> {
    private final Order order;
    private final String frontendUrl;

    private OrderRefundEmailMessage(Order order, String frontendUrl) {
        this.order = order;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public String subject() {
        return "💸 Refund Processed: Your AIMS Order #" + order.getId();
    }

    @Override
    public String recipient() {
        return order.getDeliveryInformation().getCustomerEmail();
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML & Main Container (Safe Fonts)
        sb.append("<!DOCTYPE html><html><head></head>");
        sb.append("<body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Header (Emerald/Green theme for financial success)
        sb.append("<div style='background: linear-gradient(135deg, #059669, #10b981); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #d1fae5; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Refund Confirmation</p>");
        sb.append("</div>");

        // 3. Body Content
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>Your refund is on the way!</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>Hello <strong>").append(order.getDeliveryInformation().getCustomerName()).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>We are writing to confirm that our management team has successfully processed a manual refund for your recent order.</p>");

        // 4. Order Information Box
        sb.append("<div style='background-color: #f0fdf4; padding: 20px; border-radius: 12px; border: 1px solid #bbf7d0; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #166534; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Order Reference</p>");
        sb.append("<p style='margin: 10px 0 0 0; color: #15803d; font-size: 20px; font-family: monospace; font-weight: bold; letter-spacing: 1px;'>#").append(order.getId()).append("</p>");
        sb.append("</div>");

        // 5. Instruction / Note
        sb.append("<div style='background-color: #f8fafc; padding: 15px; border-radius: 8px; border-left: 4px solid #3b82f6; margin: 20px 0;'>");
        sb.append("<p style='margin: 0; color: #0f172a; font-size: 14px;'><strong>💡 Note:</strong> Depending on your bank's processing times, please allow up to 2-3 business days for the funds to reflect in your account.</p>");
        sb.append("</div>");

        // 6. Call to Action Button
        String orderUrl = frontendUrl + "/order/" + order.getId();
        sb.append("<div style='text-align: center; margin: 35px 0 15px 0;'>");
        sb.append("<a href='").append(orderUrl).append("' ")
                .append("style='background-color: #059669; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(5, 150, 105, 0.25); transition: background-color 0.2s;'>")
                .append("View Order Details")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>");

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>If you have any questions about this refund, please reply to this email to contact our support team.</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");
        return sb.toString();
    }

    @Component
    static class Factory extends EmailMessage.Factory<OrderRefundEmailMessage, OrderRefundEvent> {
        Factory() {
            super(OrderRefundEmailMessage.class);
        }

        @Override
        public OrderRefundEmailMessage createMessage(OrderRefundEvent payload) {
            return new OrderRefundEmailMessage(payload.order(), frontendUrl);
        }
    }
}
