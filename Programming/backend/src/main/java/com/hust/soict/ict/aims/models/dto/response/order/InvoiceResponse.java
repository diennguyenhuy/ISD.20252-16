package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Setter
@NoArgsConstructor
public class InvoiceResponse {
    private Instant issuedAt;
    private long totalPriceWithoutVAT;
    private long totalPriceWithVAT;
    private long deliveryFee;
    private long totalAmount;
}
