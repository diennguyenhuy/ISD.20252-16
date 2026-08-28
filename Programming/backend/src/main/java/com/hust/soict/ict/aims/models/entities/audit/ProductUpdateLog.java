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
public class ProductUpdateLog extends ProductLog implements Iterable<ProductEditDetail> {
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

    ProductUpdateLog(
            User manager,
            Product product,
            List<ProductEditDetail> details
    ) {
        super(manager, product, ProductAction.UPDATE);
        this.details = new ArrayList<>(details);
    }

    @Override
    public Iterator<ProductEditDetail> iterator() {
        return details.iterator();
    }
}
