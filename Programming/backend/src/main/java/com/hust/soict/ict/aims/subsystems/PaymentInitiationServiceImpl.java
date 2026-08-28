package com.hust.soict.ict.aims.subsystems;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class PaymentInitiationServiceImpl implements PaymentInitiationService {
    private final Map<PaymentMethod, PaymentService> paymentServices = new EnumMap<>(PaymentMethod.class);
    private final Set<PaymentMethod> methods = EnumSet.noneOf(PaymentMethod.class);

    public PaymentInitiationServiceImpl(List<PaymentService> paymentServices) {
        paymentServices.forEach(p -> {
            this.paymentServices.put(p.method(), p);
            methods.add(p.method());
        });
    }

    @Override
    public PaymentInitiation initiatePayment(PaymentMethod paymentMethod) throws PaymentException {
        return paymentServices.get(paymentMethod).startPayment();
    }

    @Override
    public Set<PaymentMethod> getSupportedPaymentMethods() {
        return Collections.unmodifiableSet(methods);
    }
}
