package com.hust.soict.ict.aims.payment;

import com.hust.soict.ict.aims.payment.exception.PaymentException;
import com.hust.soict.ict.aims.payment.exception.UnsupportedPaymentMethodException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
class RefundServiceImpl implements RefundService {
    private final Map<PaymentMethod, Refundable> refundServices = new EnumMap<>(PaymentMethod.class);
    private final Set<PaymentMethod> methods = EnumSet.noneOf(PaymentMethod.class);

    public RefundServiceImpl(List<Refundable> refundServices) {
        refundServices.forEach(p -> {
            this.refundServices.put(p.method(), p);
            methods.add(p.method());
        });
    }

    @Override
    public boolean supports(PaymentMethod paymentMethod) {
        return refundServices.containsKey(paymentMethod);
    }

    @Override
    public void refund(PaymentTransaction paymentTransaction) throws PaymentException {
        PaymentMethod method = PaymentMethod.valueOf(paymentTransaction.getTransactionMethod());
        Refundable refundCapability = Optional.ofNullable(refundServices.get(method))
                .orElseThrow(() -> new UnsupportedPaymentMethodException("Method " + paymentTransaction.getTransactionMethod() + " does not support refund"));
        refundCapability.refund(paymentTransaction);
    }

    @Override
    public Set<PaymentMethod> getSupportedPaymentMethods() {
        return Collections.unmodifiableSet(methods);
    }
}
