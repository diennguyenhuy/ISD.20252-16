package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.dto.request.payment.paypal.PayPalCaptureRequest;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.dto.response.payment.paypal.PayPalCreateResponse;
import com.hust.soict.ict.aims.services.payment.paypal.PaypalPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * HTTP entry point for the PayPal (credit-card) payment use case.
 *
 * <p>Mirrors {@code PayOrderController} (VietQR): a thin layer that delegates the
 * whole lifecycle to {@link PaypalPaymentService}. It exchanges only DTOs with
 * the service (DATA coupling) and holds no business logic.
 *
 * <p>Endpoints (base {@code /order/payment/paypal}):
 * <ul>
 *   <li>{@code POST /create}  → start a PayPal payment for the draft order</li>
 *   <li>{@code POST /capture} → settle the approved payment &amp; finalize the order</li>
 *   <li>{@code POST /cancel}  → record a customer cancellation (no charge)</li>
 * </ul>
 */
@RestController
@RequestMapping("/order/payment/paypal")
@RequiredArgsConstructor
public class PaypalPaymentController {

    private final PaypalPaymentService paypalPaymentService;

    @PostMapping("/create")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody PayPalCreateResponse createPayment() throws PaymentException {
        return paypalPaymentService.createPayment();
    }

    @PostMapping("/capture")
    @ResponseStatus(HttpStatus.OK)
    public @ResponseBody OrderResponse capturePayment(
            @Valid @RequestBody PayPalCaptureRequest request) throws PaymentException {
        return paypalPaymentService.capturePayment(request.token());
    }

    @PostMapping("/cancel")
    @ResponseStatus(HttpStatus.OK)
    public void cancelPayment() {
        paypalPaymentService.cancelPayment();
    }
}
