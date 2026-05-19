package com.hust.soict.ict.aims;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.repositories.PaymentTransactionRepository;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import com.hust.soict.ict.aims.services.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentServiceTest {

    @Test
    void testPaymentSuccess() {

        OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
        PaymentTransactionRepository transRepo = Mockito.mock(PaymentTransactionRepository.class);

        PaymentService service = new PaymentServiceImpl(orderRepo, transRepo);

        Order order = new Order();
        order.changeStatus(OrderStatus.DRAFT);

        UUID id = UUID.randomUUID();

        Mockito.when(orderRepo.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(transRepo.save(Mockito.any()))
                .thenAnswer(i -> i.getArguments()[0]);

        PayByCreditCardRequest request = new PayByCreditCardRequest();
        request.setOrderId(id.toString());
        request.setAmount(100000);
        request.setCardNumber("4123456789012345");
        request.setCvv("123");

        PaymentInitiationResponse res =
                service.initiatePayment(TransactionMethod.PAYPAL, request);

        assertTrue(res.success());
        assertEquals(OrderStatus.APPROVED, order.getStatus());
    }

    @Test
    void testPaymentFail() {

        OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
        PaymentTransactionRepository transRepo = Mockito.mock(PaymentTransactionRepository.class);

        PaymentService service = new PaymentServiceImpl(orderRepo, transRepo);

        Order order = new Order();
        order.changeStatus(OrderStatus.DRAFT);

        UUID id = UUID.randomUUID();

        Mockito.when(orderRepo.findById(id)).thenReturn(Optional.of(order));
        Mockito.when(transRepo.save(Mockito.any()))
                .thenAnswer(i -> i.getArguments()[0]);

        PayByCreditCardRequest request = new PayByCreditCardRequest();
        request.setOrderId(id.toString());
        request.setAmount(100000);
        request.setCardNumber("5123456789012345"); // FAIL
        request.setCvv("123");

        PaymentInitiationResponse res =
                service.initiatePayment(TransactionMethod.PAYPAL, request);

        assertFalse(res.success());
        assertEquals(OrderStatus.REJECTED, order.getStatus());
    }

    @Test
    void testInvalidCard() {

        PaymentService service = new PaymentServiceImpl(null, null);

        PayByCreditCardRequest req = new PayByCreditCardRequest();
        req.setCardNumber("123");

        assertThrows(RuntimeException.class, () ->
                service.initiatePayment(TransactionMethod.PAYPAL, req)
        );
    }
}