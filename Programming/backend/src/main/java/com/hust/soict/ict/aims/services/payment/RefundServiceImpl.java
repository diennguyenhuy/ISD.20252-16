package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
class RefundServiceImpl implements RefundService {
    private final Map<PaymentMethod, IRefundCapability> refundServices;

    public RefundServiceImpl(List<IRefundCapability> refundServices) {
        this.refundServices = refundServices.stream()
                .collect(Collectors.toMap(
                        IRefundCapability::method,
                        Function.identity()
                ));
    }

    public boolean supports(PaymentMethod paymentMethod) {
        return refundServices.containsKey(paymentMethod);
    }

    @Override
    public boolean supports(String paymentMethod) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return refundServices.containsKey(method);
    }

    @Override
    public void refund(PaymentTransaction paymentTransaction) throws PaymentException {
        PaymentMethod method = PaymentMethod.valueOf(paymentTransaction.getTransactionMethod());
        IRefundCapability refundCapability = Optional.ofNullable(refundServices.get(method))
                .orElseThrow(() -> new UnsupportedPaymentMethodException("Method " + paymentTransaction.getTransactionMethod() + " does not support refund"));
        refundCapability.refund(paymentTransaction);
    }
}
