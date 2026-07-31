package com.hust.soict.ict.aims.dto.mapper;

import com.hust.soict.ict.aims.dto.response.order.PaymentTransactionResponse;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import org.springframework.stereotype.Component;

@Component
class PaymentTransactionMapper extends AbstractMapper<PaymentTransaction, PaymentTransactionResponse> {

    PaymentTransactionMapper() {
        super(PaymentTransaction.class, PaymentTransactionResponse.class, PaymentTransactionResponse::new);
    }

    @Override
    protected void map(PaymentTransaction source, PaymentTransactionResponse target) {
        target.setId(source.getId());
        target.setTransactionContent(source.getTransactionContent());
        target.setTransactionTimestamp(source.getTransactionTimestamp());
        target.setTransactionMethod(source.getTransactionMethod());
        target.setAmountPaid(source.getAmountPaid());
    }
}
