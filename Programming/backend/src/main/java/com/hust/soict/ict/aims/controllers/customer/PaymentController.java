package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.TransactionMethod;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

/**
 * Provide API endpoints for payment
 */
//TODO: Complete and refine the payment controller in accordance to the unimplemented Payment Service
@RestController
@RequestMapping("/order/payment")
@RequiredArgsConstructor
public class PaymentController {

}
