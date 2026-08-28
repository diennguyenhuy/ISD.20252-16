package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.dto.request.DeliveryRequest;
import com.hust.soict.ict.aims.exceptions.EmptyCartException;
import com.hust.soict.ict.aims.exceptions.NotEnoughStockException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface PlaceOrderService {
    Order.Draft placeOrder() throws EmptyCartException, NotEnoughStockException, ProductNotFoundException;
    Order.Draft submitDeliveryInformation(DeliveryRequest deliveryRequest);
    Order.Draft getInvoice();
    void cancelOrder();
}
