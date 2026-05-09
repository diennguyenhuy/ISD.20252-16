package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Setter
public class OrderResponse {
    private UUID id;
    private List<OrderItemResponse> items;
    private String status;
    private DeliveryResponse deliveryInformation;
    private InvoiceResponse invoice;
    private PaymentTransactionResponse paymentTransaction;
    private Instant createdAt;
}
