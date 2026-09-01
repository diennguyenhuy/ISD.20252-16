package com.hust.soict.ict.aims.services.ordermanagement.event;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.services.notification.email.EmailMessage;
import org.springframework.stereotype.Component;

class OrderRefundEmailMessage implements EmailMessage<OrderRefundEvent> {
    private final Order order;
    private final String frontendUrl;

    private OrderRefundEmailMessage(Order order, String frontendUrl) {
        this.order = order;
        this.frontendUrl = frontendUrl;
    }

    @Override
    public String subject() {
        return "";
    }

    @Override
    public String body() {
        return "";
    }

    @Override
    public String recipient() {
        return "";
    }

    @Component
    static class Factory extends EmailMessage.Factory<OrderRefundEmailMessage, OrderRefundEvent> {
        Factory() {
            super(OrderRefundEmailMessage.class);
        }

        @Override
        public OrderRefundEmailMessage createMessage(OrderRefundEvent payload) {
            return new OrderRefundEmailMessage(payload.order(), frontendUrl);
        }
    }
}
