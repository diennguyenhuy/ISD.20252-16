package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.UUID;

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Immutable
public abstract class ProductAuditLog extends AuditLog {
    @Column(updatable = false, nullable = false)
    private UUID managerId;

    @Column(updatable = false, nullable = false)
    private String managerUsername;

    @Column(updatable = false, nullable = false)
    private UUID productId;

    @Column(updatable = false, nullable = false)
    private String productTitle;

    protected ProductAuditLog(User manager, Product product) {
        super();
        this.managerId = manager.getId();
        this.managerUsername = manager.getUsername();
        this.productId = product.getId();
        this.productTitle = product.getTitle();
    }
}
