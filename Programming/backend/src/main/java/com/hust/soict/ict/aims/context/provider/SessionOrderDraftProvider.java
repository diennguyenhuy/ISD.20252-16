package com.hust.soict.ict.aims.context.provider;

import com.hust.soict.ict.aims.context.OrderDraftContext;
import com.hust.soict.ict.aims.exceptions.OrderNotPlacedException;
import com.hust.soict.ict.aims.models.entities.order.Order;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
class SessionOrderDraftProvider implements OrderDraftContext {
    private final HttpSession session;

    private static final String DRAFT_ORDER_SESSION_KEY = "DRAFT_ORDER_SESSION_KEY";

    @Override
    public Order.Draft getDraftOrder() throws OrderNotPlacedException {
        log.debug("getDraftOrder: sessionId={}", session.getId());

        Order.Draft order = (Order.Draft) session.getAttribute(DRAFT_ORDER_SESSION_KEY);

        if (order == null) {
            throw new OrderNotPlacedException("Order not placed");
        }

        return order;
    }

    @Override
    public void saveDraftOrder(Order.Draft order) {
        log.debug("saveDraftOrder: sessionId={}", session.getId());
        session.setAttribute(DRAFT_ORDER_SESSION_KEY, order);
    }

    @Override
    public void clearDraftOrder() {
        log.debug("clearDraftOrder: sessionId={}", session.getId());
        session.removeAttribute(DRAFT_ORDER_SESSION_KEY);
    }
}
