package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.services.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.models.dto.response.order.DeliveryResponse;
import com.hust.soict.ict.aims.models.dto.response.order.InvoiceResponse;
import com.hust.soict.ict.aims.models.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.models.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.DeliveryInformation;
import com.hust.soict.ict.aims.models.entities.order.Invoice;
import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.repositories.OrderRepository;
import com.hust.soict.ict.aims.models.mapper.OrderMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlaceOrderService {
    private final StockValidator stockValidator;
    private final OrderRepository orderRepository;

    private final OrderDraftContext orderDraftContext;

    private final OrderMapper orderMapper;
    private final DeliveryFeeCalculator deliveryFeeCalculator;

    public OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException {
        Order order = Order.from(stockValidator.checkStockAvailability());

        orderDraftContext.saveDraftOrder(order);

        return orderMapper.toOrderDraftResponse(order);
    }

    public DeliveryResponse submitDeliveryInformation(DeliveryRequest deliveryRequest) {
        Order order = orderDraftContext.getDraftOrder();

        var di = DeliveryInformation.of(
                order,
                deliveryRequest.getCustomerName(),
                deliveryRequest.getCustomerEmail(),
                deliveryRequest.getPhoneNumber(),
                deliveryRequest.getProvince(),
                deliveryRequest.getCommune(),
                deliveryRequest.getAddress(),
                deliveryRequest.getDeliveryMethod()
        );

        return orderMapper.toDeliveryResponse(di);
    }

    @Transactional
    public void saveOrder() throws OrderNotPlacedException {
        Order order = orderDraftContext.getDraftOrder();

        orderRepository.save(order);
    }

    private void calculateDeliveryFee(Order order) {
        order.setDeliveryFee(deliveryFeeCalculator.calculateDeliveryFee(
                order.getTotalWeight(),
                order.getDeliveryInformation().getProvince(),
                order.getTotalPriceWithoutVAT()
        ));

    }

    public InvoiceResponse getInvoice() {
        Order order = orderDraftContext.getDraftOrder();

        calculateDeliveryFee(order);

        var invoice = Invoice.from(order);

        return orderMapper.toInvoiceResponse(invoice);
    }
}
