package com.hust.soict.ict.aims.models.entities.audit;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.*;

@Entity
@Table(name = "product_update_log")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductUpdateLog extends ProductLog {
    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "product_update_detail", joinColumns = @JoinColumn(name = "log_id"))
    @OrderColumn(name = "field_number")
    private List<ProductEditDetail> details = new ArrayList<>();

    public List<ProductEditDetail> getDetails() {
        return Collections.unmodifiableList(details);
    }

    @Override
    public ProductAction getAction() {
        return ProductAction.UPDATE;
    }

    public ProductUpdateLog(
            User manager,
            Product product,
            Set<String> requestedFieldNames,
            Map<String, ?> oldValueMap,
            Map<String, ?> newValueMap
    ) {
        super(manager, product, ProductAction.UPDATE);
        requestedFieldNames.forEach(f -> {
            Object oldValue = oldValueMap.get(f);
            Object newValue = newValueMap.get(f);

            if (!Objects.equals(oldValue, newValue)) {
                details.add(new ProductEditDetail(
                        f,
                        Objects.toString(oldValue),
                        Objects.toString(newValue)
                ));
            }
        });
    }
}
