package com.hust.soict.ict.aims;

import com.hust.soict.ict.aims.models.dto.request.PayByCreditCardRequest;
import com.hust.soict.ict.aims.models.dto.response.order.PaymentInitiationResponse;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.repositories.PaymentTransactionRepository;
import com.hust.soict.ict.aims.services.payment.PaymentService;
import com.hust.soict.ict.aims.services.payment.PaymentServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Test
    void testPaymentSuccess() {

        OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
        PaymentTransactionRepository transRepo = Mockito.mock(PaymentTransactionRepository.class);

        PayByCreditCardService creditService =
                new PayByCreditCardService(orderRepo, transRepo, null);

        PaymentService service =
                new PaymentServiceImpl(creditService);

        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getStatus()).thenReturn(OrderStatus.DRAFT);

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

        Mockito.verify(order).changeStatus(OrderStatus.APPROVED);

        Mockito.verify(orderRepo).save(order);
    }

    @Test
    void testPaymentFail() {

        OrderRepository orderRepo = Mockito.mock(OrderRepository.class);
        PaymentTransactionRepository transRepo = Mockito.mock(PaymentTransactionRepository.class);

        PaymentService service = new PaymentServiceImpl(orderRepo, transRepo);

        Order order = Mockito.mock(Order.class);

        Mockito.when(order.getStatus()).thenReturn(OrderStatus.DRAFT);

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

        Mockito.verify(order).changeStatus(OrderStatus.REJECTED);

        Mockito.verify(orderRepo).save(order);
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