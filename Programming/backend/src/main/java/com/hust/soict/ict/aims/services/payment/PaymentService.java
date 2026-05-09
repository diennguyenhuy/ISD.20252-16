package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.TransactionMethod;

//TODO: Refine payment service
public interface PaymentService {
   PaymentInitiationResponse initiatePayment(TransactionMethod transactionMethod);
   PaymentInitiationResponse initiatePayment(TransactionMethod transactionMethod, PayByCreditCardRequest payByCreditCardRequest);

}
