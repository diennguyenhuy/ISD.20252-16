package com.hust.soict.ict.aims.controllers;

import com.hust.soict.ict.aims.models.dto.vietqr.request.VietQRTransactionSyncRequest;
import com.hust.soict.ict.aims.models.dto.vietqr.response.VietQRTokenResponse;
import com.hust.soict.ict.aims.models.dto.vietqr.response.VietQRTransactionSyncResponse;
import com.hust.soict.ict.aims.services.vietqr.VietQRCallbackAuthService;
import com.hust.soict.ict.aims.services.vietqr.VietQRTransactionSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class VietQRCallbackController {

    private final VietQRCallbackAuthService authService;
    private final VietQRTransactionSyncService transactionSyncService;

    @PostMapping("/vqr/api/token_generate")
    public ResponseEntity<VietQRTokenResponse> generateToken(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader
    ) {
        log.info("[VietQR-Token] Received token_generate request");

        String token = authService.authenticateFromHeader(authorizationHeader);
        if (token == null) {
            log.warn("[VietQR-Token] Authentication failed — invalid or missing credentials");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        log.info("[VietQR-Token] Token issued successfully");
        return ResponseEntity.ok(
                new VietQRTokenResponse(token, "Bearer", VietQRCallbackAuthService.TOKEN_TTL_SECONDS)
        );
    }

    @PostMapping("/vqr/bank/api/transaction-sync")
    public ResponseEntity<VietQRTransactionSyncResponse> transactionSync(
            @RequestHeader(value = HttpHeaders.AUTHORIZATION, required = false) String authorizationHeader,
            @RequestBody VietQRTransactionSyncRequest request
    ) {
        log.info("[VietQR-Sync] Received transaction-sync callback");

        String token = authService.extractBearerToken(authorizationHeader);
        if (!authService.isValid(token)) {
            log.warn("[VietQR-Sync] Invalid or missing Bearer token");
            // VietQR requires HTTP 200 even on rejection — signal error in the body
            return ResponseEntity.ok(
                    new VietQRTransactionSyncResponse(true, "Unauthorized", "Unauthorized", null)
            );
        }

        VietQRTransactionSyncResponse result = transactionSyncService.handleSync(request);
        return ResponseEntity.ok(result);
    }
}
