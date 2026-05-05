package com.hust.soict.ict.aims.services.context;

import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface OrderDraftContext {
    Order getDraftOrder() throws OrderNotPlacedException;
    void saveDraftOrder(Order order);
    void clearDraftOrder();
}
