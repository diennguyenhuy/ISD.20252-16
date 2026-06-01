package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.PaymentStatusResponse;
import com.hust.soict.ict.aims.models.dto.response.payments.QRCodeResponse;
import com.hust.soict.ict.aims.services.order.PlaceOrderService;
import com.hust.soict.ict.aims.services.payment.PayOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/*
 * + Cohesion level: FUNCTIONAL
 * + Coupling level with PayOrderService and PlaceOrderService: DATA
 * + Reason: PayOrderController has one clear responsibility: handling VietQR
 *           payment-related HTTP requests from the client. Its methods
 *           generateQRCode(), checkPaymentStatus(), confirmPayment(), and
 *           simulateCallback() all contribute to the same payment workflow
 *           by receiving requests, invoking services, and returning response DTOs.
 *
 *           The coupling with PayOrderService and PlaceOrderService is
 *           DATA coupling because the controller exchanges only the required
 *           data objects such as Order, QRCode, QRCodePaymentStatus,
 *           PaymentTransaction, and response DTOs. The controller does not
 *           pass control flags or depend on the internal implementation logic
 *           of the service classes.
 *
 *           Additionally, the controller improves modularity by separating
 *           HTTP/API handling logic from the business logic implemented inside
 *           the service layer.
 *
 *           DTO mapping is delegated to PaymentMapper (MapStruct) to keep
 *           both the controller and service layer free of manual field copying.
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

