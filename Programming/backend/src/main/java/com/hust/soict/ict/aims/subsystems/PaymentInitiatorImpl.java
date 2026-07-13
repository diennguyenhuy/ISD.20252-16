package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class PaymentInitiatorImpl implements PaymentInitiator {
    private final Map<PaymentMethod, PaymentService> paymentServices = new EnumMap<>(PaymentMethod.class);
    private final Set<PaymentMethod> methods = EnumSet.noneOf(PaymentMethod.class);

    public PaymentInitiatorImpl(List<PaymentService> paymentServices) {
        paymentServices.forEach(p -> {
            this.paymentServices.put(p.method(), p);
            methods.add(p.method());
        });
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

    @Override
    public Set<PaymentMethod> getSupportedPaymentMethods() {
        return Collections.unmodifiableSet(methods);
    }
}
