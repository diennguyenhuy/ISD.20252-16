package com.hust.soict.ict.aims.dto.response.order;

import java.time.Instant;

public record InvoiceResponse(
        Instant issuedAt,
        long totalPriceWithoutVAT,
        long totalPriceWithVAT,
        long deliveryFee,
        long totalAmount
) {}
