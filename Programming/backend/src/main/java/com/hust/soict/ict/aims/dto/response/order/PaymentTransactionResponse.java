package com.hust.soict.ict.aims.dto.response.order;

import java.time.Instant;
import java.util.UUID;

public record PaymentTransactionResponse(
        UUID id,
        String transactionContent,
        Instant transactionTimestamp,
        String transactionMethod,
        long amountPaid
) {}
