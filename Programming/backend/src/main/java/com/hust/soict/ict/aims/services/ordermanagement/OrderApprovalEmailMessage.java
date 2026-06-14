package com.hust.soict.ict.aims.services.ordermanagement;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderApprovalEmailMessage implements EmailMessage {
    private final Order order;
    private final String frontendUrl;

    @Override
    public String subject() {
        return "Great News! Your AIMS Order #" + order.getId() + " has been Approved \uD83C\uDF89";
    }

    @Override
    public String recipient() {
        return order.getDeliveryInformation().getCustomerEmail();
    }

    @Override
    public String body() {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML, Body, and a Main Container Card
        sb.append("<html><body style='font-family: \"Segoe UI\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Cinematic Header (Emerald Theme for Approval)
        sb.append("<div style='background: linear-gradient(135deg, #047857, #10b981); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #d1fae5; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>Order Approved</p>");
        sb.append("</div>");

        // 3. Greeting & Good News
        sb.append("<div style='padding: 30px 40px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px; text-align: center;'>We're getting your order ready! \uD83D\uDE80</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>Hello <strong>").append(order.getDeliveryInformation().getCustomerName()).append("</strong>,</p>");
        sb.append("<p style='color: #52525b; font-size: 16px; text-align: center;'>Good news! Your order has been reviewed and approved by our management team. We are now packing your items and preparing them for shipment.</p>");

        // 4. Order Snapshot Box
        sb.append("<div style='background-color: #f8fafc; padding: 20px; border-radius: 12px; border: 1px solid #f1f5f9; margin: 30px 0; text-align: center;'>");
        sb.append("<p style='margin: 0; color: #71717a; font-size: 14px; text-transform: uppercase; font-weight: bold;'>Order Reference</p>");
        sb.append("<p style='margin: 10px 0 0 0; font-family: monospace; color: #059669; font-size: 18px; font-weight: bold;'>").append(order.getId()).append("</p>");
        sb.append("</div>");

        // 5. Next Steps
        sb.append("<h3 style='color: #27272a; font-size: 16px; border-bottom: 2px solid #e4e4e7; padding-bottom: 8px;'>What happens next?</h3>");
        sb.append("<ul style='color: #52525b; font-size: 15px; padding-left: 20px;'>");
        sb.append("<li style='margin-bottom: 10px;'>Our warehouse team will carefully pack your items.</li>");
        sb.append("<li style='margin-bottom: 10px;'>Your package will be handed over to our delivery partners.</li>");
        sb.append("<li>You can track your order status live on our platform.</li>");
        sb.append("</ul>");

        // 6. Dynamic Call-to-Action Button
        String orderLink = frontendUrl + "/order/" + order.getId();
        sb.append("<div style='text-align: center; margin: 40px 0 10px 0;'>");
        sb.append("<a href='").append(orderLink).append("' ")
                .append("style='background-color: #10b981; color: #ffffff; padding: 14px 32px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(16, 185, 129, 0.25); transition: background-color 0.2s;'>")
                .append("Track Order Status")
                .append("</a>");
        sb.append("</div>");

        sb.append("</div>"); // Close main padding div

        // 7. Footer
        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>Need help? Reply to this email or call us at <strong>1800-AIMS</strong></p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");

        return sb.toString();
    }

    @Component
    public static class Factory extends EmailMessage.Factory<OrderApprovalEmailMessage, OrderApprovalEvent> {
        @Override
        public Class<OrderApprovalEmailMessage> messageType() {
            return OrderApprovalEmailMessage.class;
        }

        @Override
        public OrderApprovalEmailMessage createMessage(OrderApprovalEvent payload) {
            return new OrderApprovalEmailMessage(payload.order(), frontendUrl);
        }
    }
}
