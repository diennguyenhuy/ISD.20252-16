package com.hust.soict.ict.aims.payment.vietqr.synccallback;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
class VietQRTransactionSyncService {
    public VietQRTransactionSyncResponse handleSync(VietQRTransactionSyncRequest request) {
        log.info("[VietQR-Sync] Received transaction-sync: transactionId={} orderId={} amount={} transType={}",
                request.transactionId(), request.orderId(),
                request.amount(), request.transType());

        // Print the full object to the console so we can see EVERYTHING VietQR sent
        System.out.println("=== FULL WEBHOOK PAYLOAD FROM VIETQR ===");
        System.out.println(request.toString());
        System.out.println("========================================");

        // Only process credit transactions (money received)
        if (!"C".equalsIgnoreCase(request.transType())) {
            log.warn("[VietQR-Sync] Ignoring non-credit transaction: transType={}", request.transType());
            return errorResponse("Transaction type not supported: " + request.transType());
        }

        // Validate amount
        if (request.amount() == null || request.amount() <= 0) {
            log.warn("[VietQR-Sync] Invalid amount: {}", request.amount());
            return errorResponse("Invalid transaction amount");
        }
        Instant transactionTimestamp = (request.transactionTime() != null)
                ? Instant.ofEpochMilli(request.transactionTime())
                : Instant.now();

        log.info("[VietQR-Sync] Processed webhook successfully for transactionId={}",
                request.transactionId());

        return successResponse(request.transactionId());
    }


    private static VietQRTransactionSyncResponse successResponse(String refTransactionId) {
        return new VietQRTransactionSyncResponse(
                false,
                null,
                "Success",
                new VietQRTransactionSyncObject(refTransactionId)
        );
    }

    private static VietQRTransactionSyncResponse errorResponse(String reason) {
        return new VietQRTransactionSyncResponse(true, reason, reason, null);
    }
}

