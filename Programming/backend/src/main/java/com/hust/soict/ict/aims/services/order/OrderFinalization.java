package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.OrderNotCompleteException;
import com.hust.soict.ict.aims.dto.response.order.OrderResponse;
import com.hust.soict.ict.aims.models.entities.order.Order;

public interface OrderFinalization {
    OrderResponse finalizeOrder(Order draftOrder) throws OrderNotCompleteException;
}
