package com.hust.soict.ict.aims.services.notification;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.order.PaymentTransaction;

public interface NotificationService {
    void sendOrderConfirmation(Order order, PaymentTransaction paymentTransaction);
}
