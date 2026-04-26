package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Entity
@Table(name = "stock_adjust_log")
@Getter
@NoArgsConstructor
public class StockAdjustLog {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false, updatable = false)
    private int oldStock;
    @Column(nullable = false, updatable = false)
    private int newStock;

    @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
    private String reason;

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

        return "[PRODUCT_LOG #" + id + "] " + managerInfo
                + " changed product #" + product.getId()
                + "'s stock from " + oldStock + " to " + newStock
                + " at " + localTimestamp
                + " with reason: " + reason;
    }
}
