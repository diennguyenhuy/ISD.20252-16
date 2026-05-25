package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.order.*;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderService {
    private final StockValidator stockValidator;
    private final OrderRepository orderRepository;

    private final OrderDraftContext orderDraftContext;

    private final OrderMapper orderMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException {
        log.debug("Placing order...");
        log.debug("Checking stock availability...");
        Order draftOrder = Order.from(stockValidator.checkStockAvailability());

        log.debug("Stock availability check done and satisfied.");

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Order placed successfully! New draft order has been created!");
        return orderMapper.toOrderDraftResponse(draftOrder);
    }

    public InvoiceResponse getInvoice() {
        log.debug("Creating invoice...");
        Order draftOrder = orderDraftContext.getDraftOrder();

        var invoice = Invoice.from(draftOrder);
        orderDraftContext.saveDraftOrder(draftOrder);

        return orderMapper.toInvoiceResponse(invoice);
    }

    @Transactional
    public OrderResponse finalizeOrder(PaymentTransaction paymentTransaction) { //TODO: Add new PaymentTransaction to order
        log.debug("Finalizing order...");
        Order draftOrder = orderDraftContext.getDraftOrder();
        draftOrder.changeStatus(OrderStatus.PENDING);

        Order order = orderRepository.save(draftOrder);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order, paymentTransaction));

        log.debug("Order has been successfully saved!");
        return orderMapper.toOrderResponse(order);
    }

    public void cancelOrder() {
        log.debug("Canceling order...");
        orderDraftContext.clearDraftOrder();
        log.debug("Order successfully canceled!");
    }

    @Transactional
    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) throws IllegalStateException, OrderNotFoundException {
        log.debug("Canceling order #{}...", orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.changeStatus(OrderStatus.CANCELLED);

        log.debug("Order #{} successfully canceled!", orderId);
        orderRepository.save(order);
    }
}
