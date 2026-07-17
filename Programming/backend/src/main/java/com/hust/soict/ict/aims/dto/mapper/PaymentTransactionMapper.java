package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.PaymentTransactionResponse;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
class PaymentTransactionMapper extends AbstractMapper<PaymentTransaction, PaymentTransactionResponse> {

    PaymentTransactionMapper() {
        super(PaymentTransactionResponse::new);
    }

    @Override
    public void map(PaymentTransaction source, PaymentTransactionResponse target) {
        target.setId(source.getId());
        target.setTransactionContent(source.getTransactionContent());
        target.setTransactionTimestamp(source.getTransactionTimestamp());
        target.setTransactionMethod(source.getTransactionMethod());
        target.setAmountPaid(source.getAmountPaid());
    }

    @Override
    public Class<PaymentTransaction> getSourceClass() {
        return PaymentTransaction.class;
    }

    @Override
    public Class<PaymentTransactionResponse> getTargetClass() {
        return PaymentTransactionResponse.class;
    }
}
