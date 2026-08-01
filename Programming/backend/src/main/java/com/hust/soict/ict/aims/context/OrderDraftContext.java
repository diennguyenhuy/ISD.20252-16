package com.hust.soict.ict.aims.context;

import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface OrderDraftContext {
    Order.Draft getDraftOrder() throws OrderNotPlacedException;
    void saveDraftOrder(Order.Draft order);
    void clearDraftOrder();
}
