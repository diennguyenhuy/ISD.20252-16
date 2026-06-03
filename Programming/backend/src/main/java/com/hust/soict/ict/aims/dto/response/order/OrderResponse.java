package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Data
public class OrderResponse {
    private UUID id;
    private List<OrderItemResponse> items;
    private String status;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
    private PaymentTransactionResponse paymentTransaction;
    private Instant createdAt;
}
