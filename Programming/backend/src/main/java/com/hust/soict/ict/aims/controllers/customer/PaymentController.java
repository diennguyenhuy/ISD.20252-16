package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.TransactionMethod;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/credit-card")
    public PaymentInitiationResponse payByCreditCard(
            @Valid @RequestBody PayByCreditCardRequest request
    ) {
        return paymentService.initiatePayment(TransactionMethod.PAYPAL, request);
    }
}