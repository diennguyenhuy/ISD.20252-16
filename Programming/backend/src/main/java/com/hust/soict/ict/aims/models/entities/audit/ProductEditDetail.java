package com.hust.soict.ict.aims.models.entities.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Immutable
public class ProductEditDetail {
    @Column(nullable = false, updatable = false)
    private String fieldName;

    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String oldValue;
    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String newValue;

    ProductEditDetail(String fieldName, String oldValue, String newValue) {
        this.fieldName = fieldName;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }
}
