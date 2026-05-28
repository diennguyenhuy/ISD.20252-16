package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "stock_adjust_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StockAdjustLog extends ProductAuditLog {
    @Column(nullable = false, updatable = false)
    private Integer oldStock;
    @Column(nullable = false, updatable = false)
    private Integer newStock;

    @Column(columnDefinition = "TEXT", nullable = false, updatable = false)
    private String reason;

    public StockAdjustLog(User manager, Product product, int oldStock, int newStock, String reason) {
        super(manager, product);
        this.oldStock = oldStock;
        this.newStock = newStock;
        this.reason = reason;
    }
}
