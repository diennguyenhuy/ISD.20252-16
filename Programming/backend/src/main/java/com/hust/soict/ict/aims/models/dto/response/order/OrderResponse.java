package com.hust.soict.ict.aims.models.dto.response.order;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public class OrderResponse {

    private UUID id;
    private List<OrderItemResponse> items;
    private String status;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
    private PaymentTransactionResponse paymentTransaction;
    private Instant createdAt;

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public List<OrderItemResponse> getItems() { return items; }
    public void setItems(List<OrderItemResponse> items) { this.items = items; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public DeliveryResponse getDeliveryInformation() { return deliveryInformation; }
    public void setDeliveryInformation(DeliveryResponse deliveryInformation) { this.deliveryInformation = deliveryInformation; }

    public InvoiceResponse getInvoice() { return invoice; }
    public void setInvoice(InvoiceResponse invoice) { this.invoice = invoice; }

    public PaymentTransactionResponse getPaymentTransaction() { return paymentTransaction; }
    public void setPaymentTransaction(PaymentTransactionResponse paymentTransaction) { this.paymentTransaction = paymentTransaction; }

    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}