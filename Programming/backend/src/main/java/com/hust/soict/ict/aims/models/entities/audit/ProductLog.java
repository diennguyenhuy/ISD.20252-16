package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Immutable @Entity
@Table(name = "product_log")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog extends AuditLog<ProductAction> {
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

    ProductLog(User manager, Product product, ProductAction action) {
        if (action == ProductAction.UPDATE) {
            throw new IllegalArgumentException("Must specify field difference with " + ProductAction.UPDATE + " audit logging. Hint: Use the other constructor.");
        }
        super();
        this.managerId = manager.getId();
        this.managerUsername = manager.getUsername();
        this.productId = product.getId();
        this.productTitle = product.getTitle();
        this.action = action;
    }

    @Override
    public ProductAction getAction() {
        return action;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_update_detail", joinColumns = @JoinColumn(name = "log_id"))
    @MapKeyColumn(name = "field_name")
    private Map<String, ProductEditDetail> details = new HashMap<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "product_update_array_detail", joinColumns = @JoinColumn(name = "log_id"))
    @MapKeyColumn(name = "field_name")
    private Map<String, ProductEditArrayDetail> arrayDetails = new HashMap<>();

    public Map<String, ProductEditDetail> getDetails() {
        return Collections.unmodifiableMap(details);
    }

    public Map<String, ProductEditArrayDetail> getArrayDetails() {
        return Collections.unmodifiableMap(arrayDetails);
    }

    ProductLog(User manager, Product product, Map<String, ProductEditDetail> details, Map<String, ProductEditArrayDetail> arrayDetails) {
        this(manager, product, ProductAction.UPDATE);
        this.details = new HashMap<>(details);
        this.arrayDetails = new HashMap<>(arrayDetails);
    }
}
