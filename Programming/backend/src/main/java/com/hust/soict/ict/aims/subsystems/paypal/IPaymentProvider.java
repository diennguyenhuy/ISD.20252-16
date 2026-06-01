package com.hust.soict.ict.aims.subsystems.paypal;

import com.hust.soict.ict.aims.exceptions.PaymentException;
import com.hust.soict.ict.aims.models.entities.order.Order;

/**
 * Facade boundary for any external "card / wallet" payment provider.
 *
 * <p>Design notes
 * <ul>
 *   <li><b>Pattern:</b> Facade. This is the <i>only</i> type the AIMS core
 *       (services / controllers) is allowed to know about. Everything below it
 *       — OAuth token retrieval, PayPal v2 Orders JSON, currency conversion,
 *       link parsing — is hidden inside the {@code subsystems.paypal} package.</li>
 *   <li><b>Cohesion:</b> Functional — exposes exactly the two operations the
 *       PayPal "create then capture" redirect flow needs.</li>
 *   <li><b>Coupling:</b> DATA. Callers exchange only provider-agnostic value
 *       objects ({@link PaymentInitiation}, {@link PaymentCapture}) and the
 *       domain {@link Order} aggregate. No PayPal SDK type ever leaks out.</li>
 *   <li><b>SOLID:</b> DIP — {@code PayByCreditCardService} depends on this
 *       abstraction, not on the concrete {@code PayPalGatewayFacade}. A future
 *       Stripe/Momo provider can implement the same interface with zero changes
 *       to the service layer (OCP).</li>
 * </ul>
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
