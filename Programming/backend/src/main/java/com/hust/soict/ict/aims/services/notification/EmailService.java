package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Cohesion: Communicational Cohesion
 * Reason:
 * Methods collaborate to construct and send
 * order confirmation emails using related email data.
 * Coupling:
 * - Data coupling with JavaMailSender and
 *   NotificationService abstraction.
 * - Stamp coupling with Order and PaymentTransaction
 *   because full domain objects are used to build emails.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @Override
    public void sendOrderConfirmation(Order order) {
        log.debug("Sending order confirmation email...");
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(order.getDeliveryInformation().getCustomerEmail());
            helper.setSubject("AIMS - Thank you for your order! Order #" + order.getId());
            helper.setText(buildOrderEmail(order), true);
            mailSender.send(mimeMessage);
            log.debug("Order confirmation email sent successfully!");
        } catch (MessagingException e) {
            log.warn("Multipart creation failed: {}", e.getMessage());
        } catch (MailAuthenticationException e) {
            log.warn("Mail authentication failed: {}", e.getMessage());
        } catch (MailSendException e) {
            log.warn("Could not send email: {}", e.getMessage());
        } catch (MailException e) {
            log.warn("Mail exception: {}", e.getMessage());
        }
    }

    private String buildOrderEmail(Order order) {
        StringBuilder sb = new StringBuilder();

        // Helper formatters
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        String formattedTxTime = order.getPaymentTransaction() != null ?
                formatter.format(order.getPaymentTransaction().getTransactionTimestamp()) : "N/A";
        String formattedInvoiceTime = order.getInvoice() != null && order.getInvoice().getIssuedAt() != null ?
                formatter.format(order.getInvoice().getIssuedAt()) : "N/A";

        // 1. Setup HTML, Body, and a Main Container Card
        sb.append("<html><body style='font-family: \"DM Sans\", Helvetica, Arial, sans-serif; background-color: #f4f4f5; padding: 40px 20px; color: #09090b; line-height: 1.6;'>");
        sb.append("<div style='max-width: 650px; margin: 0 auto; background-color: #ffffff; border-radius: 16px; overflow: hidden; box-shadow: 0 10px 25px rgba(0,0,0,0.05); border: 1px solid #e4e4e7;'>");

        // 2. Cinematic Header
        sb.append("<div style='background: linear-gradient(135deg, #4c1d95, #6d28d9); padding: 30px; text-align: center; color: #ffffff;'>");
        sb.append("<h1 style='margin: 0; font-size: 28px; font-weight: 800; letter-spacing: 1px;'>AIMS</h1>");
        sb.append("<p style='margin: 5px 0 0 0; color: #c4b5fd; font-size: 14px; text-transform: uppercase; letter-spacing: 2px;'>An Internet Media Store</p>");
        sb.append("</div>");

        // 3. Greeting & Intro
        sb.append("<div style='padding: 30px;'>");
        sb.append("<h2 style='color: #27272a; margin-top: 0; font-size: 22px;'>Thank you for your order! \uD83C\uDF89</h2>");
        sb.append("<p style='color: #52525b; font-size: 16px;'>We have successfully received your payment and your order is now processing. Below is your official invoice and receipt.</p>");

        // 4. Split Order & Delivery Info
        sb.append("<div style='display: flex; flex-wrap: wrap; margin-top: 25px; margin-bottom: 30px; gap: 20px;'>");

        // Order Snapshot
        sb.append("<div style='flex: 1; min-width: 250px; background-color: #f8fafc; padding: 20px; border-radius: 12px; border: 1px solid #f1f5f9;'>");
        sb.append("<h3 style='margin: 0 0 10px 0; color: #6d28d9; font-size: 14px; text-transform: uppercase;'>Order Details</h3>");
        sb.append("<p style='margin: 5px 0; font-size: 14px;'><strong>Order ID:</strong> <br><span style='font-family: monospace; color: #52525b;'>").append(order.getId()).append("</span></p>");
        sb.append("<p style='margin: 5px 0; font-size: 14px;'><strong>Date:</strong> <br><span style='color: #52525b;'>").append(formattedInvoiceTime).append("</span></p>");
        sb.append("</div>");

        // Delivery Info
        if (order.getDeliveryInformation() != null) {
            var di = order.getDeliveryInformation();
            sb.append("<div style='flex: 1; min-width: 250px; background-color: #f8fafc; padding: 20px; border-radius: 12px; border: 1px solid #f1f5f9;'>");
            sb.append("<h3 style='margin: 0 0 10px 0; color: #6d28d9; font-size: 14px; text-transform: uppercase;'>Shipping To</h3>");
            sb.append("<p style='margin: 5px 0; font-size: 14px; color: #27272a;'><strong>").append(di.getCustomerName()).append("</strong></p>");
            sb.append("<p style='margin: 5px 0; font-size: 14px; color: #52525b;'>").append(di.getPhoneNumber()).append("</p>");
            sb.append("<p style='margin: 5px 0; font-size: 14px; color: #52525b;'>").append(di.getAddress()).append(", ").append(di.getCommune()).append(", ").append(di.getProvince()).append("</p>");
            sb.append("</div>");
        }
        sb.append("</div>"); // End Split Box

        // 5. Items Table
        sb.append("<h3 style='border-bottom: 2px solid #e4e4e7; padding-bottom: 10px; color: #27272a;'>Purchased Items</h3>");
        sb.append("<table style='width: 100%; border-collapse: collapse; margin-bottom: 20px;'>");

        order.getItems().forEach(item ->
                sb.append("<tr style='border-bottom: 1px solid #f4f4f5;'>")
                .append("<td style='padding: 15px 0; font-weight: 600; color: #3f3f46;'>").append(item.getProductName()).append("</td>")
                .append("<td style='padding: 15px 0; text-align: center; color: #71717a;'>x").append(item.getQuantity()).append("</td>")
                .append("<td style='padding: 15px 0; text-align: right; color: #3f3f46; font-weight: 500;'>")
                .append(String.format("%,d", item.getItemTotalPrice())).append(" ₫</td>")
                .append("</tr>")
        );
        sb.append("</table>");

        // 6. Detailed Invoice Breakdown
        if (order.getInvoice() != null) {
            var inv = order.getInvoice();
            long subtotal = inv.getTotalPriceWithoutVAT();
            long vat = inv.getTotalPriceWithVAT() - subtotal;

            sb.append("<div style='width: 100%; display: flex; justify-content: flex-end;'>");
            sb.append("<table style='width: 300px; font-size: 15px; margin-bottom: 30px; border-collapse: collapse;'>");

            sb.append("<tr><td style='padding: 8px 0; color: #71717a;'>Subtotal:</td><td style='padding: 8px 0; text-align: right; color: #3f3f46;'>").append(String.format("%,d", subtotal)).append(" ₫</td></tr>");
            sb.append("<tr><td style='padding: 8px 0; color: #71717a;'>VAT (10%):</td><td style='padding: 8px 0; text-align: right; color: #3f3f46;'>").append(String.format("%,d", vat)).append(" ₫</td></tr>");
            sb.append("<tr><td style='padding: 8px 0; color: #71717a;'>Delivery Fee:</td><td style='padding: 8px 0; text-align: right; color: #3f3f46;'>").append(String.format("%,d", inv.getDeliveryFee())).append(" ₫</td></tr>");

            sb.append("<tr style='border-top: 2px solid #e4e4e7;'>")
                    .append("<td style='padding: 12px 0 0 0; font-weight: bold; color: #27272a;'>Grand Total:</td>")
                    .append("<td style='padding: 12px 0 0 0; text-align: right; font-weight: 800; color: #6d28d9; font-size: 20px;'>")
                    .append(String.format("%,d", inv.getTotalAmount())).append(" ₫</td></tr>");
            sb.append("</table>");
            sb.append("</div>");
        }

        // 7. Payment Transaction Details
        if (order.getPaymentTransaction() != null) {
            PaymentTransaction pt = order.getPaymentTransaction();
            sb.append("<div style='background-color: #ecfdf5; padding: 25px; border-radius: 12px; border: 1px solid #d1fae5; margin-bottom: 20px;'>");
            sb.append("<h3 style='color: #047857; margin-top: 0; margin-bottom: 15px; font-size: 16px; display: flex; align-items: center;'>");
            sb.append("✓ Payment Successful</h3>");

            sb.append("<table style='width: 100%; font-size: 14px; border-collapse: collapse;'>");
            sb.append("<tr><td style='padding: 6px 0; color: #065f46;'>Method:</td><td style='padding: 6px 0; text-align: right; font-weight: 600; color: #064e3b;'>").append(pt.getTransactionMethod()).append("</td></tr>");
            sb.append("<tr><td style='padding: 6px 0; color: #065f46;'>Time:</td><td style='padding: 6px 0; text-align: right; font-weight: 600; color: #064e3b;'>").append(formattedTxTime).append("</td></tr>");
            sb.append("<tr><td style='padding: 6px 0; color: #065f46;'>Memo:</td><td style='padding: 6px 0; text-align: right; font-weight: 600; font-family: monospace; color: #064e3b;'>").append(pt.getTransactionContent()).append("</td></tr>");

            sb.append("<tr style='border-top: 1px dashed #a7f3d0;'>")
                    .append("<td style='padding: 12px 0 0 0; color: #065f46; font-weight: bold;'>Amount Paid:</td>")
                    .append("<td style='padding: 12px 0 0 0; text-align: right; font-weight: 800; color: #059669; font-size: 18px;'>")
                    .append(String.format("%,d", pt.getAmountPaid())).append(" ₫</td></tr>");
            sb.append("</table>");
            sb.append("</div>");
        }

        String orderLink = frontendUrl + "/order/" + order.getId();

        sb.append("<div style='text-align: center; margin: 35px 0 20px 0;'>");
        sb.append("<p style='color: #52525b; font-size: 14px; margin-bottom: 15px;'>You can track your order status or cancel your order using the link below:</p>");

        // Theming the button to match the React frontend's Primary color (#6d28d9)
        sb.append("<a href='").append(orderLink).append("' ")
                .append("style='background-color: #6d28d9; color: #ffffff; padding: 14px 28px; border-radius: 12px; ")
                .append("text-decoration: none; font-size: 16px; font-weight: bold; display: inline-block; ")
                .append("box-shadow: 0 4px 6px rgba(109, 40, 217, 0.25);'>")
                .append("View / Manage Order")
                .append("</a>");

        sb.append("</div>");

        // 8. Close Container & Footer
        sb.append("</div>"); // Close main padding div

        sb.append("<div style='padding: 20px; text-align: center; background-color: #f4f4f5;'>");
        sb.append("<p style='margin: 0; font-size: 13px; color: #71717a;'>Need help? Reply to this email or call us at <strong>1800-AIMS</strong></p>");
        sb.append("<p style='margin: 10px 0 0 0; font-size: 12px; color: #a1a1aa;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div>");

        sb.append("</div></body></html>");

        return sb.toString();
    }
}
