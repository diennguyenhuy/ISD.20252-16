package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.order.*;
import com.hust.soict.ict.aims.models.entities.order.*;
import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PlaceOrderService {
    private final StockValidator stockValidator;
    private final OrderRepository orderRepository;

    private final OrderDraftContext orderDraftContext;

    private final OrderMapper orderMapper;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    private final ApplicationEventPublisher applicationEventPublisher;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException {
        Order draftOrder = Order.from(stockValidator.checkStockAvailability());

        orderDraftContext.saveDraftOrder(draftOrder);

        return orderMapper.toOrderDraftResponse(draftOrder);
    }

    public DeliveryResponse submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        Order draftOrder = orderDraftContext.getDraftOrder();
        var deliveryInformation = draftOrder.getDeliveryInformation();
        if (deliveryInformation != null) {
            updateDeliveryInformation(deliveryInformation, deliveryRequest);

            return orderMapper.toDeliveryResponse(deliveryInformation);
        }

        var di = DeliveryInformation.of(
                deliveryRequest.getCustomerName(),
                deliveryRequest.getCustomerEmail(),
                deliveryRequest.getPhoneNumber(),
                deliveryRequest.getProvince(),
                deliveryRequest.getCommune(),
                deliveryRequest.getAddress(),
                deliveryRequest.getDeliveryMethod(),
                draftOrder
        );

        orderDraftContext.saveDraftOrder(draftOrder);

        return orderMapper.toDeliveryResponse(di);
    }

    private void updateDeliveryInformation(DeliveryInformation deliveryInformation, DeliveryRequest deliveryRequest) {
        deliveryInformation.setCustomerName(deliveryRequest.getCustomerName());
        deliveryInformation.setCustomerEmail(deliveryRequest.getCustomerEmail());
        deliveryInformation.setPhoneNumber(deliveryRequest.getPhoneNumber());
        deliveryInformation.setProvince(deliveryRequest.getProvince());
        deliveryInformation.setCommune(deliveryRequest.getCommune());
        deliveryInformation.setAddress(deliveryRequest.getAddress());
        deliveryInformation.setAddress(deliveryRequest.getAddress());
    }

    public InvoiceResponse getInvoice() {
        Order draftOrder = orderDraftContext.getDraftOrder();

        draftOrder.setDeliveryFee(deliveryFeeCalculator.calculateDeliveryFee(
                draftOrder.getTotalWeight(),
                draftOrder.getDeliveryInformation().getProvince(),
                draftOrder.getTotalPriceWithoutVAT()
        ));

        var invoice = Invoice.from(draftOrder);
        orderDraftContext.saveDraftOrder(draftOrder);

        return orderMapper.toInvoiceResponse(invoice);
    }

    @Transactional
    public OrderResponse finalizeOrder(PaymentTransaction paymentTransaction) { //TODO: Add new PaymentTransaction to order

        Order draftOrder = orderDraftContext.getDraftOrder();
        draftOrder.changeStatus(OrderStatus.PENDING);

        Order order = orderRepository.save(draftOrder);

        applicationEventPublisher.publishEvent(new OrderSuccessEvent(order, paymentTransaction));

        return orderMapper.toOrderResponse(order);
    }

    public void cancelOrder() {
        orderDraftContext.clearDraftOrder();
    }

    @Transactional
    public OrderResponse getOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        return orderMapper.toOrderResponse(order);
    }

    @Transactional
    public void cancelOrder(UUID orderId) throws IllegalStateException, OrderNotFoundException {
        Order order = orderRepository.findById(orderId).orElseThrow(() -> new OrderNotFoundException(orderId));
        order.changeStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);
    }
}
