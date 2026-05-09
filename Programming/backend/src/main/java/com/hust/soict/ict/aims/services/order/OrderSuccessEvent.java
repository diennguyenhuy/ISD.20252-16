package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
class OrderSuccessEvent {
    private Order order;
    private PaymentTransaction paymentTransaction;
}
