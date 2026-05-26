package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/payment")
@RequiredArgsConstructor
public class PaymentController {

    // Control Coupling: controller controls which payment method is used
    private final PaymentService paymentService;

    /**
     * Endpoint: Pay by Credit Card
     *
     * Coupling:
     * - Control Coupling: defines payment method
     * - Stamp Coupling: passes full request DTO
     *
     * Cohesion:
     * - Functional: only handles HTTP request
     */
    @PostMapping("/credit-card")
    public PaymentInitiationResponse payByCreditCard(
            @Valid @RequestBody PayByCreditCardRequest request
    ) {
        return paymentService.initiatePayment(PaymentTransaction.Method.PAYPAL, request);
    }
}
