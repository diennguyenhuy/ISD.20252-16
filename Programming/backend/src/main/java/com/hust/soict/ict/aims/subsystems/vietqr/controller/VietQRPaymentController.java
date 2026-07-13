package com.hust.soict.ict.aims.subsystems.vietqr.controller;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.subsystems.vietqr.model.VietQRPaymentStatus;
import com.hust.soict.ict.aims.subsystems.vietqr.service.VietQRPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/*
 * SOLID Principles: No violations.
 *
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with PayOrderService: DATA
 * + Reason: Exchanges only response DTOs (QRCodeResponse, PaymentStatusResponse,
 *   OrderResponse) — no control flags or internal service state is exposed.
 */
@RestController
@RequestMapping("/order/payment/vietqr")
@RequiredArgsConstructor
public class VietQRPaymentController {
    private final VietQRPaymentService vietQRPaymentService;

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody VietQRPaymentStatus checkPaymentStatus() throws PaymentException {
        return vietQRPaymentService.checkPaymentStatus();
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody OrderResponse confirmPayment()
            throws PaymentException, IllegalStateException {
        return vietQRPaymentService.confirmPayment();
    }
}

