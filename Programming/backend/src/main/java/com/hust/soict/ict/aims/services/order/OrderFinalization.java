package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface OrderFinalization {
    Order finalizeOrder(Order.Draft draftOrder) throws OrderNotCompleteException;
}
