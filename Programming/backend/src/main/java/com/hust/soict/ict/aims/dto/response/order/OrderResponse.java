package com.hust.soict.ict.aims.dto.response.order;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record OrderResponse(
        UUID id,
        List<OrderItemResponse> items,
        String status,
        BigDecimal totalWeight,
        Integer totalItemCount,
        DeliveryResponse deliveryInformation,
        InvoiceResponse invoice,
        PaymentTransactionResponse paymentTransaction,
        Instant createdAt,
        Instant updatedAt
) {}
