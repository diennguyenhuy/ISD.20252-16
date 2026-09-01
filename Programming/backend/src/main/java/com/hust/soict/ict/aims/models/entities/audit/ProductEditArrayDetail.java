package com.hust.soict.ict.aims.models.entities.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.util.Collections;
import java.util.List;

@Embeddable
@Immutable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductEditArrayDetail {
    @Column(nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> removedValues;

    @Column(nullable = false, updatable = false)
    @JdbcTypeCode(SqlTypes.ARRAY)
    private List<String> addedValues;

    ProductEditArrayDetail(List<String> removedValues, List<String> addedValues) {
        this.removedValues = removedValues;
        this.addedValues = addedValues;
    }

    public List<String> getRemovedValues() {
        return Collections.unmodifiableList(removedValues);
    }

    public List<String> getAddedValues() {
        return Collections.unmodifiableList(addedValues);
    }
}
