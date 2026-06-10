package com.hust.soict.ict.aims.models.entities.audit;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Immutable;

@Embeddable
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Immutable
public class ProductEditDetail {
    @Column(nullable = false, updatable = false)
    private String fieldName;

    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String oldValue;
    @Column(nullable = false, updatable = false, columnDefinition = "TEXT")
    private String newValue;
}
