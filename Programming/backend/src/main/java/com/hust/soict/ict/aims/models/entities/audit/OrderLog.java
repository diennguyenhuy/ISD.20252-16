package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.order.Order;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.Optional;
import java.util.UUID;

@Immutable @Entity
@Table(schema = "order_log")
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class OrderLog extends AuditLog {
    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private OrderAction action;

    @Column(updatable = false, nullable = false)
    private UUID orderId;

    @Column(updatable = false)
    private UUID managerId;

    @Column(updatable = false)
    private String managerUsername;

    OrderLog(OrderAction action, Order order) {
        this.action = action;
        this.orderId = order.getId();
    }

    OrderLog(OrderAction action, Order order, User manager) {
        this(action, order);
        this.managerId = manager.getId();
        this.managerUsername = manager.getUsername();
    }

    public Optional<UUID> getManagerId() {
        return Optional.ofNullable(managerId);
    }

    public Optional<String> getManagerUsername() {
        return Optional.ofNullable(managerUsername);
    }

    @Override
    public OrderAction getAction() {
        return action;
    }
}
