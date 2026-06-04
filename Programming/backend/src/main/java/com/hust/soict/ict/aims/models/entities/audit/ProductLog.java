package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Immutable @Entity
@Table(name = "product_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog extends ProductAuditLog {
    public enum Action {
        ADD,
        EDIT,
        DELETE,
        DEACTIVATE,
    }

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Action action;

    public ProductLog(Product product, User user, Action action) {
        super(user, product);
        this.action = action;
    }
}
