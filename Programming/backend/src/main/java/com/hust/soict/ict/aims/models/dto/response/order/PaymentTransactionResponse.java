package com.hust.soict.ict.aims.models.dto.response.order;

import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@NoArgsConstructor
@Setter
public class PaymentTransactionResponse {
    private UUID id;
    private String transactionContent;
    private Instant transactionTimestamp;
    private String transactionMethod;
    private long amountPaid;
}
