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

@MappedSuperclass
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Immutable
public abstract class ProductAuditLog extends AuditLog {
    @ManyToOne
    @JoinColumn(name = "manager_id", updatable = false)
    protected User manager;

    @Column(updatable = false, nullable = false)
    protected String managerUsername;

    @ManyToOne
    @JoinColumn(name = "product_id", updatable = false)
    protected Product product;

    @Column(updatable = false, nullable = false)
    protected String productTitle;

    protected ProductAuditLog(User manager, Product product) {
        super();
        this.manager = manager;
        this.managerUsername = manager.getUsername();
        this.product = product;
        this.productTitle = product.getTitle();
    }
}
