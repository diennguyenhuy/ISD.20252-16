package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@NoArgsConstructor
@Data
public class InvoiceResponse {
    private Instant issuedAt;
    private long totalPriceWithoutVAT;
    private long totalPriceWithVAT;
    private long deliveryFee;
    private long totalAmount;
}
