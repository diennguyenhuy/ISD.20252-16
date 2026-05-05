package com.hust.soict.ict.aims.services.context.provider;

import com.hust.soict.ict.aims.services.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SessionOrderDraftProvider implements OrderDraftContext {
    private final HttpSession session;

    private static final String DRAFT_ORDER_SESSION_KEY = "DRAFT_ORDER_SESSION_KEY";

    @Override
    public Order getDraftOrder() throws OrderNotPlacedException {
        Order order = (Order) session.getAttribute(DRAFT_ORDER_SESSION_KEY);

        if (order == null) {
            throw new OrderNotPlacedException("Order not placed");
        }

        return order;
    }

    @Override
    public void saveDraftOrder(Order order) {
        session.setAttribute(DRAFT_ORDER_SESSION_KEY, order);
    }

    @Override
    public void clearDraftOrder() {
        session.removeAttribute(DRAFT_ORDER_SESSION_KEY);
    }
}
