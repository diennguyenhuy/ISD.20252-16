package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.OrderItem;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService implements NotificationService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Override
    public void sendOrderConfirmation(Order order, PaymentTransaction paymentTransaction) {
        log.debug("Sending order confirmation email...");
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            helper.setFrom(from);
            helper.setTo(order.getDeliveryInformation().getCustomerEmail());
            helper.setSubject("AIMS - Thank you for your order! Order #" + order.getId());
            helper.setText(buildOrderEmail(order, paymentTransaction), true);
            mailSender.send(mimeMessage);
        } catch (MessagingException e) {
            log.warn("Multipart creation failed: {}", e.getMessage());
        } catch (MailAuthenticationException e) {
            log.warn("Mail authentication failed: {}", e.getMessage());
        } catch (MailSendException e) {
            log.warn("Could not send email: {}", e.getMessage());
        }
        log.debug("Order confirmation email sent successfully!");
    }

    private String buildOrderEmail(Order order, PaymentTransaction paymentTransaction) {
        StringBuilder sb = new StringBuilder();

        // 1. Setup HTML, Body, and a Main Container Card
        sb.append("<html><body style='font-family: Arial, sans-serif; background-color: #f9fafb; padding: 20px; color: #09090b;'>");
        sb.append("<div style='max-width: 600px; margin: 0 auto; background-color: #ffffff; padding: 30px; border-radius: 12px; box-shadow: 0 4px 6px rgba(0,0,0,0.05);'>");

        // 2. Header
        sb.append("<h1 style='color: #6d28d9; margin-top: 0;'>Thank you for your order! \uD83C\uDF89</h1>");
        sb.append("<p style='font-size: 16px; color: #71717a;'>Your order has been successfully placed. Here are your details:</p>");

        // 3. Order ID Box
        sb.append("<div style='background-color: #f4f4f5; padding: 15px; border-radius: 8px; margin-bottom: 20px;'>");
        sb.append("<strong>Order ID:</strong> <span style='font-family: monospace; color: #6d28d9;'>")
                .append(order.getId())
                .append("</span>");
        sb.append("</div>");

        // 4. Items Table
        sb.append("<table style='width: 100%; border-collapse: collapse; margin-bottom: 30px;'>");
        sb.append("<thead><tr style='border-bottom: 2px solid #e4e4e7; text-align: left;'>");
        sb.append("<th style='padding: 10px 0; color: #71717a;'>Product</th>");
        sb.append("<th style='padding: 10px 0; text-align: right; color: #71717a;'>Quantity</th>");
        sb.append("</tr></thead><tbody>");

        for (OrderItem item : order.getItems()) {
            sb.append("<tr style='border-bottom: 1px solid #f4f4f5;'>")
                    .append("<td style='padding: 12px 0; font-weight: bold;'>").append(item.getProduct().getTitle()).append("</td>")
                    .append("<td style='padding: 12px 0; text-align: right;'>").append(item.getQuantity()).append("</td>")
                    .append("</tr>");
        }
        sb.append("</tbody></table>");

        // 5. Total Footer
        sb.append("<div style='text-align: right; font-size: 18px;'>");
        sb.append("<span style='color: #71717a; margin-right: 10px;'>Total Amount:</span>");
        sb.append("<strong style='color: #6d28d9; font-size: 24px;'>")
                .append(String.format("%,d", order.getInvoice().getTotalAmount())) // Formats number with commas
                .append(" VND</strong>");
        sb.append("</div>");

        // 6. Payment Transaction Details (NEW)
        // Format the Instant into a readable string (Adjust timezone as needed)
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss").withZone(ZoneId.of("Asia/Ho_Chi_Minh"));
        String formattedTime = formatter.format(paymentTransaction.getTransactionTimestamp());

        sb.append("<div style='background-color: #f8fafc; padding: 20px; border-radius: 8px; border: 1px solid #e4e4e7;'>");
        sb.append("<h3 style='color: #6d28d9; margin-top: 0; margin-bottom: 15px; font-size: 16px;'>Payment Information</h3>");

        sb.append("<table style='width: 100%; font-size: 14px; border-collapse: collapse;'>");

        // Method
        sb.append("<tr>")
                .append("<td style='padding: 8px 0; color: #71717a;'>Payment Method:</td>")
                .append("<td style='padding: 8px 0; text-align: right; font-weight: bold; color: #09090b;'>").append(paymentTransaction.getTransactionMethod()).append("</td>")
                .append("</tr>");

        // Timestamp
        sb.append("<tr>")
                .append("<td style='padding: 8px 0; color: #71717a;'>Transaction Time:</td>")
                .append("<td style='padding: 8px 0; text-align: right; font-weight: bold; color: #09090b;'>").append(formattedTime).append("</td>")
                .append("</tr>");

        // Content
        sb.append("<tr>")
                .append("<td style='padding: 8px 0; color: #71717a;'>Transfer Content:</td>")
                .append("<td style='padding: 8px 0; text-align: right; font-weight: bold; font-family: monospace; color: #09090b;'>").append(paymentTransaction.getTransactionContent()).append("</td>")
                .append("</tr>");

        // Amount Paid (Highlighted in Green)
        sb.append("<tr style='border-top: 1px dashed #d4d4d8;'>")
                .append("<td style='padding: 12px 0 0 0; color: #71717a;'>Amount Paid:</td>")
                .append("<td style='padding: 12px 0 0 0; text-align: right; font-weight: bold; color: #10b981; font-size: 16px;'>")
                .append(String.format("%,d", paymentTransaction.getAmountPaid())).append(" VND</td>")
                .append("</tr>");

        sb.append("</table>");
        sb.append("</div>");

        // 7. Close Container
        sb.append("<p style='margin-top: 40px; font-size: 12px; color: #a1a1aa; text-align: center;'>© 2026 AIMS - An Internet Media Store. All rights reserved.</p>");
        sb.append("</div></body></html>");

        return sb.toString();
    }
}
