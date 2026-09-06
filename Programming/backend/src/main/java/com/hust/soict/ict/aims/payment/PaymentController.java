package com.hust.soict.ict.aims.payment;

import com.hust.soict.ict.aims.payment.exception.PaymentException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/order/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentInitiationService paymentInitiationService;
    private final RefundService refundService;

    @PostMapping("/{method}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentInitiation initiatePayment(@PathVariable PaymentMethod method) throws PaymentException {
        return paymentInitiationService.initiatePayment(method);
    }

    @GetMapping
    public Set<PaymentMethod> getAllPaymentMethods() {
        return paymentInitiationService.getSupportedPaymentMethods();
    }

    @GetMapping("/{method}")
    public boolean supportRefund(@PathVariable PaymentMethod method) {
        return refundService.supports(method);
    }
}
