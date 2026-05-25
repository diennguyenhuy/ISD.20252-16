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

    private final OrderRepository orderRepository;
    private final PaymentTransactionRepository transactionRepository;

    @Override
    public PaymentInitiationResponse initiatePayment(TransactionMethod method) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public PaymentInitiationResponse initiatePayment(
            TransactionMethod method,
            PayByCreditCardRequest request
    ) {

        validateCard(request);

        Order order = orderRepository.findById(UUID.fromString(request.getOrderId()))
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // check state machine
        if (order.getStatus() != OrderStatus.DRAFT &&
                order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Order not payable");
        }

        boolean success = simulatePayment(request);

        PaymentTransaction transaction = PaymentTransaction.of(
                success ? "Payment success" : "Payment failed",
                Instant.now(),
                method,
                (long) request.getAmount(),
                order
        );

        transactionRepository.save(transaction);

        // update trạng thái
        if (success) {
            order.changeStatus(OrderStatus.APPROVED);
        } else {
            order.changeStatus(OrderStatus.REJECTED);
        }

        orderRepository.save(order);

        return new PaymentInitiationResponse(
                null,
                null,
                success,
                success ? "Payment successful" : "Payment failed"
        );
    }

    // =========================

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

    private boolean simulatePayment(PayByCreditCardRequest request) {
        return request.getCardNumber().startsWith("4"); // VISA success
    }
}