package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.payments.PaymentStatusResponse;
import com.hust.soict.ict.aims.dto.response.payments.QRCodeResponse;
import com.hust.soict.ict.aims.services.payment.vietqr.PayOrderService;
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
public class PayOrderController {
    private final PayOrderService payOrderService;
    @PostMapping("/qr")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody QRCodeResponse generateQRCode() throws PaymentException {
        return payOrderService.generatePaymentQR();
    }

    @GetMapping("/status")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody PaymentStatusResponse checkPaymentStatus() throws PaymentException {
        return payOrderService.checkPaymentStatus();
    }

    @PostMapping("/confirm")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody OrderResponse confirmPayment()
            throws PaymentException, IllegalStateException {
        return payOrderService.confirmPayment();
    }
}

