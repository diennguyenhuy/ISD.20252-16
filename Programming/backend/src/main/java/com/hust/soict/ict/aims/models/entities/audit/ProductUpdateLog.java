package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

import java.util.*;

@Immutable @Entity
@Table(name = "product_update_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateLog extends ProductLog {
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

    @Override
    public ProductAction getAction() {
        return ProductAction.UPDATE;
    }

    ProductUpdateLog(User manager, Product product, Map<String, ProductEditDetail> details, Map<String, ProductEditArrayDetail> arrayDetails) {
        super(manager, product, ProductAction.UPDATE);
        this.details = new HashMap<>(details);
        this.arrayDetails = new HashMap<>(arrayDetails);
    }
}
