package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.subsystems.exception.PaymentException;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentCapture;
import com.hust.soict.ict.aims.subsystems.paypal.model.PayPalPaymentInitiation;

/*
 * [SOLID DIP: ownership/packaging improvement, not a hard violation][cite: 1]
 * Principle: Dependency Inversion (D) "Ownership Inversion"[cite: 1]
 * Why: This is the abstraction the AIMS core (PayByCreditCardService) depends on,[cite: 1]
 *      yet it lives INSIDE the low-level subsystems.paypal package. Per ownership[cite: 1]
 *      inversion, the client/core layer should OWN the contract, not the low-level[cite: 1]
 *      provider. As written, the core must import from subsystems.paypal, and a[cite: 1]
 *      future Stripe/Momo subsystem would also have to import its contract from th[cite: 1]
 *      PayPal package.[cite: 1]
 * Proposed Solution: Move IPaymentProvider + PaymentInitiation + PaymentCapture ir[cite: 1]
 *      a core-owned package (e.g. .payment.port). PayPal then depends inward on[cite: 1]
 */
public interface PayPalRedirectGateway {
    PayPalPaymentInitiation createPayment(String orderId, long totalAmount) throws PaymentException;
    PayPalPaymentCapture capturePayment(String providerOrderId) throws PaymentException;
}
