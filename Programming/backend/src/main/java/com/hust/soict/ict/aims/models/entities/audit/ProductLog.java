package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "product_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog {
    public enum Action {
        ADD,
        EDIT,
        DELETE,
        DEACTIVATE,
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Action action;

    @ManyToOne
    @JoinColumn(name = "manager_id", updatable = false)
    private User manager;

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", updatable = false, nullable = false)
    private Product product;

    @CreationTimestamp
    @Column(updatable = false)
    private Instant timestamp;

    @Override
    public String toString() {
        String localTimestamp = LocalDateTime
                .ofInstant(timestamp, ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));

        String managerInfo = (manager != null) ? "Manager #" + manager.getId() : "A manager";

        String actionName = switch (action) {
            case ADD -> " added ";
            case EDIT -> " edited ";
            case DELETE -> " deleted ";
            case DEACTIVATE -> " deactivated ";
        };

        return "[PRODUCT_LOG #" + id + "] " + managerInfo + " "
                + actionName + " product #" + product.getId()
                + " at " + localTimestamp;
    }

    public static ProductLog of(
            Action action,
            User manager,
            Product product,
            Instant timestamp
    ) {
        ProductLog l = new ProductLog();

        l.action = action;
        l.manager = manager;
        l.product = product;
        l.timestamp = timestamp;

        return l;
    }
}
