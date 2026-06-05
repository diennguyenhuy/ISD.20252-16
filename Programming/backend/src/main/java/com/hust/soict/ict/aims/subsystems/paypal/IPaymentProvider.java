package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

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
public interface IPaymentProvider {

    /**
     * Create a provider-side payment for the given order and return the URL the
     * customer must be redirected to in order to approve it.
     *
     * @param order the AIMS draft order being paid for
     * @return provider order id + approval (redirect) URL
     * @throws PaymentException if the provider rejects the creation request
     */
    PaymentInitiation createPayment(Order order) throws PaymentException;

    /**
     * Capture (settle) a payment the customer has already approved on the
     * provider's hosted page.
     *
     * @param providerOrderId the provider order id returned by the redirect
     * @return capture outcome (completed flag, capture id, reference id, status)
     * @throws PaymentException if capture fails or the payment was cancelled
     */
    PaymentCapture capturePayment(String providerOrderId) throws PaymentException;
}
