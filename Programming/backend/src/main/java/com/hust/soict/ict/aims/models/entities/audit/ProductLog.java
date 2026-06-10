package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.*;

@Immutable @Entity
@Table(name = "product_log")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductLog extends ProductAuditLog {
    @Column(nullable = false, updatable = false, length = 20)
    @Enumerated(EnumType.STRING)
    private ProductAction action;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_update_log", joinColumns = @JoinColumn(name = "log_id"))
    @OrderColumn(name = "field_number")
    private List<ProductEditDetail> details = new ArrayList<>();

    public ProductLog(User manager, Product product, ProductAction action) {
        super(manager, product);
        if (action == ProductAction.UPDATE) {
            throw new IllegalArgumentException("Edit product log action must come with a list of edit details. Use the other constructor");
        }
        this.action = action;
    }

    public ProductLog(User manager, Product product, Set<String> fieldNames, Map<String, Object> oldValueMap, Map<String, Object> newValueMap) {
        super(manager, product);
        this.action = ProductAction.UPDATE;

        for (String fieldName : fieldNames) {
            Object oldValue = oldValueMap.get(fieldName);
            Object newValue = newValueMap.get(fieldName);

            if (!Objects.equals(oldValue, newValue)) {
                details.add(new ProductEditDetail(fieldName, Objects.toString(oldValue), Objects.toString(newValue)));
            }
        }
    }
}
