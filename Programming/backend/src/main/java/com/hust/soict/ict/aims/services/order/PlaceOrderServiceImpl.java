package com.hust.soict.ict.aims.services.order;

import org.springframework.stereotype.Service;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.hust.soict.ict.aims.context.CartContext;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.order.deliveryfee.DeliveryFeeCalculationMethod;
import com.hust.soict.ict.aims.services.order.deliveryfee.DeliveryFeeCalculator;
import com.hust.soict.ict.aims.services.order.event.OrderSuccessEvent;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
class PlaceOrderServiceImpl implements PlaceOrderService {
    private final StockValidator stockValidator;

    private final DeliveryFeeCalculator deliveryFeeCalculator;

    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;

    @Override
    public Order.Draft placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException {
        log.debug("Placing order...");
        log.debug("Checking stock availability...");
        Order.Draft draftOrder = cartContext.getOrCreateCart().placeOrder(stockValidator::checkStockAvailability);
        log.debug("Stock availability check done and satisfied.");

        try {
            Order.Draft oldDraft = orderDraftContext.getDraftOrder();
            draftOrder.provideDeliveryInformation(oldDraft.getDeliveryInformation(), oldDraft.getDeliveryFee());
        } catch (OrderNotPlacedException _) {}

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Order placed successfully! New draft order has been created!");
        return draftOrder;
    }

    @Override
    public Order.Draft submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        log.debug("Submitting delivery request...");
        Order.Draft draftOrder = orderDraftContext.getDraftOrder();

        draftOrder.provideDeliveryInformation(
                deliveryRequest.customerName(),
                deliveryRequest.customerEmail(),
                deliveryRequest.phoneNumber(),
                deliveryRequest.province(),
                deliveryRequest.commune(),
                deliveryRequest.address(),
                deliveryRequest.deliveryMethod()
        );

        long deliveryFee = deliveryFeeCalculator.calculate(DeliveryFeeCalculationMethod.STANDARD, draftOrder);

        draftOrder.updateDeliveryFee(deliveryFee);

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Delivery information successfully saved!");
        return draftOrder;
    }

    @Override
    public Order.Draft getInvoice() {
        log.debug("Creating invoice...");
        Order.Draft draftOrder = orderDraftContext.getDraftOrder();
        Invoice invoice = draftOrder.getInvoice();
        if (invoice == null) {
            throw new OrderNotCompleteException("Order invoice is not yet generated due to internal logical error or missing delivery info.");
        }
        return draftOrder;
    }

    @Override
    public void cancelOrder() {
        log.debug("Canceling order...");
        orderDraftContext.clearDraftOrder();
        log.debug("Order successfully canceled!");
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    void clearContext(@SuppressWarnings("unused") OrderSuccessEvent __) {
        cartContext.getOrCreateCart().clear();
        orderDraftContext.clearDraftOrder();
    }
}
