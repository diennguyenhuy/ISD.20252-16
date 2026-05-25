package com.hust.soict.ict.aims.services.payment;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.repositories.PaymentTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    // Data Coupling: only interact through repository interfaces
    private final OrderRepository orderRepository;
    private final PaymentTransactionRepository transactionRepository;

    /**
     * Not implemented (for other payment methods like VietQR)
     */
    @Override
    public PaymentInitiationResponse initiatePayment(TransactionMethod method) {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * Service responsible for handling payment operations
     *
     * Cohesion:
     * - Functional: handles credit card payment processing
     * - Procedural: follows a fixed sequence of steps
     *
     * Coupling:
     * - Depends on OrderRepository and PaymentTransactionRepository
     *
     * Domain Logic:
     * - Uses OrderStatus state machine to ensure valid transitions
     *
     * Limitation:
     * - Payment simulation is hardcoded
     * - No integration with real payment gateway
     */
    @Override
    public PaymentInitiationResponse initiatePayment(
            TransactionMethod method,
            PayByCreditCardRequest request
    ) {

        // Step 1: Validate card info
        validateCard(request);

        // Step 2: Get order from DB
        Order order = orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Step 3: Check order state
        if (order.getStatus() != OrderStatus.DRAFT &&
                order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order not payable");
        }

        // Step 4: Simulate payment gateway
        boolean success = simulatePayment(request);

        // Step 5: Create transaction
        PaymentTransaction transaction = PaymentTransaction.of(
                success ? "Payment success" : "Payment failed",
                Instant.now(),
                method,
                (long) request.getAmount(),
                order
        );

        transactionRepository.save(transaction);

        // Step 6: Update order status
        if (success) {
            order.changeStatus(OrderStatus.APPROVED);
        } else {
            order.changeStatus(OrderStatus.REJECTED);
        }

        orderRepository.save(order);

        // Step 7: Return response
        return new PaymentInitiationResponse(
                null,
                null,
                success,
                success ? "Payment successful" : "Payment failed"
        );
    }

    // =========================

    /**
     * Validate credit card information
     *
     * Cohesion:
     * - Functional: only handles validation logic
     *
     * Improvement:
     * - Can be extracted to CardValidationService
     */
    private void validateCard(PayByCreditCardRequest request) {

        if (request == null) {
            throw new RuntimeException("Request null");
        }

        if (request.getCardNumber() == null ||
                !request.getCardNumber().matches("\\d{16}")) {
            throw new RuntimeException("Invalid card number");
        }

        if (request.getCvv() == null ||
                !request.getCvv().matches("\\d{3}")) {
            throw new RuntimeException("Invalid CVV");
        }
    }

    /**
     * Simulate external payment gateway
     *
     * Current logic:
     * - VISA (card starts with 4) → success
     * - others → fail
     *
     * Limitation:
     * - Not realistic, only for testing
     *
     * Improvement:
     * - Replace with PaymentGatewayService
     */
    private boolean simulatePayment(PayByCreditCardRequest request) {
        return request.getCardNumber().startsWith("4");
    }
}