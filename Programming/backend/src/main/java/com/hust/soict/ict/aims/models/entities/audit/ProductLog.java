package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@Immutable @Entity
@Table(name = "product_log")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog extends AuditLog {
    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProductAction action;

    @Column(updatable = false, nullable = false)
    private UUID managerId;

    @Column(updatable = false, nullable = false)
    private String managerUsername;

    @Column(updatable = false, nullable = false)
    private UUID productId;

    @Column(updatable = false, nullable = false)
    private String productTitle;

    protected ProductLog(User manager, Product product, ProductAction action) {
        super();
        this.managerId = manager.getId();
        this.managerUsername = manager.getUsername();
        this.productId = product.getId();
        this.productTitle = product.getTitle();
        this.action = action;
    }
}
