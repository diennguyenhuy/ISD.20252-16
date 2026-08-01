package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.dto.response.order.OrderDraftResponse;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;

public interface PlaceOrderService {
    OrderDraftResponse placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException;
    OrderDraftResponse submitDeliveryInformation(DeliveryRequest deliveryRequest);
    OrderDraftResponse getInvoice();
    void cancelOrder();
}
