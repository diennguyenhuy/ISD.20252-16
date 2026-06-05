package com.hust.soict.ict.aims.dto.response.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class PaymentTransactionResponse {
    private UUID id;
    private String transactionContent;
    private Instant transactionTimestamp;
    private String transactionMethod;
    private long amountPaid;
}
