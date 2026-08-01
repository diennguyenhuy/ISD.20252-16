package com.hust.soict.ict.aims.subsystems.vietqr.synccallback;


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
                request.getTransactionId(), request.getOrderId(),
                request.getAmount(), request.getTransType());

        // Print the full object to the console so we can see EVERYTHING VietQR sent
        System.out.println("=== FULL WEBHOOK PAYLOAD FROM VIETQR ===");
        System.out.println(request.toString());
        System.out.println("========================================");

        // Only process credit transactions (money received)
        if (!"C".equalsIgnoreCase(request.getTransType())) {
            log.warn("[VietQR-Sync] Ignoring non-credit transaction: transType={}", request.getTransType());
            return errorResponse("Transaction type not supported: " + request.getTransType());
        }

        // Validate amount
        if (request.getAmount() == null || request.getAmount() <= 0) {
            log.warn("[VietQR-Sync] Invalid amount: {}", request.getAmount());
            return errorResponse("Invalid transaction amount");
        }
        Instant transactionTimestamp = (request.getTransactionTime() != null)
                ? Instant.ofEpochMilli(request.getTransactionTime())
                : Instant.now();

        log.info("[VietQR-Sync] Processed webhook successfully for transactionId={}",
                request.getTransactionId());

        return successResponse(request.getTransactionId());
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

