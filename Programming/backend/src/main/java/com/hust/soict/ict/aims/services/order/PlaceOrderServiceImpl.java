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

@Service
@RequiredArgsConstructor
@Slf4j
class PlaceOrderServiceImpl implements PlaceOrderService {
    private final StockValidator stockValidator;

    private final DeliveryFeeCalculator deliveryFeeCalculator;

    private final CartContext cartContext;
    private final OrderDraftContext orderDraftContext;
    private final OrderMapper orderMapper;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException {
        log.debug("Placing order...");
        log.debug("Checking stock availability...");
        Order.Draft draftOrder = new Order.Draft(stockValidator.checkStockAvailability(cartContext.getOrCreateCart()));
        log.debug("Stock availability check done and satisfied.");

        try {
            Order.Draft oldDraft = orderDraftContext.getDraftOrder();
            draftOrder.provideDeliveryInformation(oldDraft.getDeliveryInformation(), oldDraft.getDeliveryFee());
        } catch (OrderNotPlacedException ignored) {}

        orderDraftContext.saveDraftOrder(draftOrder);
        log.debug("Order placed successfully! New draft order has been created!");
        return orderMapper.toOrderDraftResponse(draftOrder);
    }

    public DeliveryResponse submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        log.debug("Submitting delivery request...");
        Order.Draft draftOrder = orderDraftContext.getDraftOrder();

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
        Order.Draft draftOrder = orderDraftContext.getDraftOrder();
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
