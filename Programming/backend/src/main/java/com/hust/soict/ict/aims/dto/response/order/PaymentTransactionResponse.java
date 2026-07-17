package com.hust.soict.ict.aims.dto.response.order;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
public class PaymentTransactionResponse {
    private UUID id;
    private String transactionContent;
    private Instant transactionTimestamp;
    private String transactionMethod;
    private long amountPaid;
}
