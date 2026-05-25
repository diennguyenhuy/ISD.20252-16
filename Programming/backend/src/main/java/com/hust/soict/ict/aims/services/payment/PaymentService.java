package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.TransactionMethod;

/**
 * Payment Service Interface
 *
 * Responsibility:
 * - Define contract for all payment operations
 *
 * Coupling:
 * - Reduces coupling between Controller and Implementation
 * - Allows multiple implementations (CreditCard, PayPal, VietQR)
 *
 * Cohesion:
 * - Functional Cohesion: only defines payment-related behavior
 */
public interface PaymentService {

    PaymentInitiationResponse initiatePayment(TransactionMethod transactionMethod);

    PaymentInitiationResponse initiatePayment(
            TransactionMethod transactionMethod,
            PayByCreditCardRequest request
    );
}