package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.ArrayList;
import java.util.List;

@Immutable @Entity
@Table(name = "product_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog extends ProductAuditLog {
    public enum Action {
        CREATE,
        UPDATE,
        DELETE,
        DEACTIVATE,
        ACTIVATE
    }

    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private Action action;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_update_log", joinColumns = @JoinColumn(name = "log_id"))
    @OrderColumn(name = "field_number")
    private List<ProductEditDetail> details = new ArrayList<>();

    public ProductLog(Product product, User manager, Action action) {
        super(manager, product);
        if (action == Action.UPDATE) {
            throw new IllegalArgumentException("Edit product log action must come with a list of edit details. Use the other constructor");
        }
        this.action = action;
    }

    public ProductLog(Product product, User manager, List<ProductEditDetail> details) {
        super(manager, product);
        this.action = Action.UPDATE;
        this.details = new ArrayList<>(details);
    }
}
