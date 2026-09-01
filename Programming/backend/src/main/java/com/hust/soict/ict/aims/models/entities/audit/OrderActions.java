package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.user.User;

public final class OrderActions {
    public static final CustomerActionLogging PLACE = () -> OrderAction.PLACE;
    public static final ManagerActionLogging APPROVE =() -> OrderAction.APPROVE;
    public static final ManagerActionLogging REJECT = () -> OrderAction.REJECT;
    public static final CustomerActionLogging CANCEL = () -> OrderAction.CANCEL;
    public static final ManagerActionLogging REFUND = () -> OrderAction.REFUND;
    public static final CustomerActionLogging CANCEL_REFUND = () -> OrderAction.CANCEL_REFUND;
    public static final ManagerActionLogging REJECT_REFUND = () -> OrderAction.REJECT_REFUND;

    @FunctionalInterface
    private interface Action {
        OrderAction action();
    }

    public interface CustomerActionLogging extends Action {
        default OrderLog log(Order order) {
            return new OrderLog(action(), order);
        }
    }

    public interface ManagerActionLogging extends Action {
        default OrderLog log(Order order, User manager) {
            return new OrderLog(action(), order, manager);
        }
    }

    private OrderActions() {}
}
