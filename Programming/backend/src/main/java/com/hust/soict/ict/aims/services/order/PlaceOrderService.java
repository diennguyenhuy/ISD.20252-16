package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.exceptions.*;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.dto.mapper.OrderMapper;
import com.hust.soict.ict.aims.services.order.deliveryfee.DeliveryFeeCalculationMethod;
import com.hust.soict.ict.aims.services.order.deliveryfee.DeliveryFeeCalculator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
 * SOLID Review
 * Potential Violation:
 * - Single Responsibility Principle (SRP)
 * - Open/Closed Principle (OCP)
 * Reason:
 * - [SRP] PlaceOrderService is responsible for multiple aspects of
 * the order lifecycle, including draft order creation,
 * invoice generation, order finalization, order retrieval,
 * and order cancellation. Changes to any of these workflows may
 * require modification of the same class.
 * - [OCP] Order completion requirements are hardcoded inside
 * finalizeOrder(). New requirements such as additional mandatory order
 * information, validation rules, or completion criteria
 * would require modification of existing logic.
 * Improvement Direction:
 * - [SRP] If the application grows, consider separating responsibilities
 * into dedicated services such as OrderDraftService,
 * OrderFinalizationService, and OrderQueryService.
 * - [OCP] Introduce extensible validation mechanisms such as
 * OrderCompletionRule or OrderValidator abstractions
 * that can be extended without modifying the service.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class PlaceOrderService {
    private final StockValidator stockValidator;

    private final DeliveryFeeCalculator deliveryFeeCalculator;

    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;
    private final OrderMapper orderMapper;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException {
        log.debug("Placing order...");
        log.debug("Checking stock availability...");
        Order draftOrder = Order.from(stockValidator.checkStockAvailability(cartContext.getOrCreateCart()));
        log.debug("Stock availability check done and satisfied.");

        try {
            Order oldDraft = orderDraftContext.getDraftOrder();
            draftOrder.provideDeliveryInformation(oldDraft.getDeliveryInformation(), oldDraft.getDeliveryFee());
        } catch (OrderNotPlacedException ignored) {}

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Order placed successfully! New draft order has been created!");
        return orderMapper.toOrderDraftResponse(draftOrder);
    }

    public DeliveryResponse submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        log.debug("Submitting delivery request...");
        Order draftOrder = orderDraftContext.getDraftOrder();

        var di = draftOrder.provideDeliveryInformation(
                deliveryRequest.getCustomerName(),
                deliveryRequest.getCustomerEmail(),
                deliveryRequest.getPhoneNumber(),
                deliveryRequest.getProvince(),
                deliveryRequest.getCommune(),
                deliveryRequest.getAddress(),
                deliveryRequest.getDeliveryMethod()
        );

        long deliveryFee = deliveryFeeCalculator.calculate(DeliveryFeeCalculationMethod.STANDARD, draftOrder);

        draftOrder.updateDeliveryFee(deliveryFee);

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Delivery information successfully saved!");
        return orderMapper.toDeliveryResponse(di);
    }

    public InvoiceResponse getInvoice() {
        log.debug("Creating invoice...");
        Order draftOrder = orderDraftContext.getDraftOrder();
        Invoice invoice = draftOrder.getInvoice();
        if (invoice == null) {
            throw new OrderNotCompleteException("Order invoice is not yet generated due to internal logical error or missing delivery info.");
        }
        return orderMapper.toInvoiceResponse(invoice);
    }

    public void cancelOrder() {
        log.debug("Canceling order...");
        orderDraftContext.clearDraftOrder();
        log.debug("Order successfully canceled!");
    }
}
