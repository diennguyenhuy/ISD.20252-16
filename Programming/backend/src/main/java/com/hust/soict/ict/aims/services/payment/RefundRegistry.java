package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.exceptions.UnsupportedPaymentMethodException;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import com.hust.soict.ict.aims.services.payment.contract.IRefundCapability;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RefundRegistry {
    private final Map<PaymentMethod, IRefundCapability> refundServices;

    public RefundRegistry(List<IRefundCapability> refundServices) {
        this.refundServices = refundServices.stream()
                .collect(Collectors.toMap(
                        IRefundCapability::method,
                        Function.identity()
                ));
    }

    public boolean supports(PaymentMethod paymentMethod) {
        return refundServices.containsKey(paymentMethod);
    }

    public boolean supports(String paymentMethod) {
        PaymentMethod method;
        try {
            method = PaymentMethod.valueOf(paymentMethod);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return refundServices.containsKey(method);
    }

    public void refund(PaymentTransaction paymentTransaction) {
        PaymentMethod method = PaymentMethod.valueOf(paymentTransaction.getTransactionMethod());
        IRefundCapability refundCapability = Optional.ofNullable(refundServices.get(method))
                .orElseThrow(() -> new UnsupportedPaymentMethodException("Method " + paymentTransaction.getTransactionMethod() + " does not support refund"));
        refundCapability.refund(paymentTransaction);
    }
}
