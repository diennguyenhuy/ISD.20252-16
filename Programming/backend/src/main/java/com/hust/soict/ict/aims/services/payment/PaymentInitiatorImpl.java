package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
class PaymentInitiatorImpl implements PaymentInitiator {
    private final Map<PaymentMethod, PaymentService> paymentServices = new EnumMap<>(PaymentMethod.class);

    public PaymentInitiatorImpl(List<PaymentService> paymentServices) {
        paymentServices.forEach(p -> this.paymentServices.put(p.method(), p));
    }

    public PaymentInitiation initiatePayment(String paymentMethod) throws PaymentException {
        PaymentMethod pm;
        try {
            pm = PaymentMethod.valueOf(paymentMethod.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new UnsupportedPaymentMethodException(paymentMethod, e);
        }

        return paymentServices.get(pm).startPayment();
    }
}
