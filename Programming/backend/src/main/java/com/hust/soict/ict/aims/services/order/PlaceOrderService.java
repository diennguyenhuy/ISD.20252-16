package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.models.dto.response.order.*;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * Cohesion: Procedural Cohesion<br>
 * Reason: Coordinates the order placement workflow including stock validation,
 * order drafting, invoice generation, finalization, retrieval, and cancellation.<br>
 * Coupling:
 * - Data coupling with StockValidator, OrderRepository,
 *   and ApplicationEventPublisher through method calls.
 *   Uses event-driven architecture through ApplicationEventPublisher
 *   to reduce direct dependencies between services.
 * - Stamp coupling with Order, PaymentTransaction,
 *   OrderDraftContext, and OrderMapper because
 *   composite domain objects are passed between modules.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderService implements OrderFinalization {
    private final StockValidator stockValidator;
    private final OrderRepository orderRepository;

    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;

    private final OrderMapper orderMapper;

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException {
        log.debug("Placing order...");
        log.debug("Checking stock availability...");
        Order draftOrder = Order.from(stockValidator.checkStockAvailability(cartContext.getOrCreateCart()));
        log.debug("Stock availability check done and satisfied.");

        try {
            var existingDelivery = orderDraftContext.getDraftOrder().getDeliveryInformation();
            if (existingDelivery != null) {
                DeliveryInformation.of(existingDelivery, draftOrder);
                draftOrder.setDeliveryFee(orderDraftContext.getDraftOrder().getDeliveryFee());
            }
        } catch (OrderNotPlacedException ignored) {}

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

    @Override
    @Transactional
    public OrderResponse finalizeOrder(Order draftOrder) throws OrderNotCompleteException {
        log.debug("Finalizing order...");

        if (draftOrder.getDeliveryInformation() == null
                || draftOrder.getInvoice() == null
                || draftOrder.getPaymentTransaction() == null
        ) {
            throw new OrderNotCompleteException("Order is not complete.");
        }

        draftOrder.changeStatus(Order.Status.PENDING);

        Order order = orderRepository.save(draftOrder);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order));

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
        order.changeStatus(Order.Status.CANCELLED);

        log.debug("Order #{} successfully canceled!", orderId);
        orderRepository.save(order);
    }
}
