package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

import com.hust.soict.ict.aims.exceptions.ProductConstructionException;
import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String description;

    /// Unit: centimeters cm
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal height;

    /// Unit: centimeters cm
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal width;

    /// Unit: centimeters cm
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal length;

    ///Unit: kilograms kg
    @Column(nullable = false, precision = 10, scale = 3)
    private BigDecimal weight;

    @Column(unique = true, nullable = false, length = 32)
    private String barcode;

    @Column(nullable = false)
    private Long originalValue;

    @Column(nullable = false)
    private Long currentPrice;

    @Column(nullable = false)
    private Integer stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status = ProductStatus.ACTIVE;

    @Column(name = "image_url", length = 2048)
    private String imageURL;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(updatable = false, nullable = false)
    private Instant addedAt;

    @UpdateTimestamp
    @Column(nullable = false)
    private Instant updatedAt;

    private static final double MIN_PRICE_RELATIVE = 0.3;
    private static final double MAX_PRICE_RELATIVE = 1.5;

    public void changePrice(long newPrice) throws ProductValidationException {
        if (newPrice < 0) {
            throw new ProductValidationException("New price must not be negative", "currentPrice");
        }

        double minPrice = MIN_PRICE_RELATIVE * originalValue;
        double maxPrice = MAX_PRICE_RELATIVE * originalValue;

        if (newPrice < minPrice || newPrice > maxPrice) {
            throw new ProductValidationException("Current price must be between " + (long)minPrice + "VND and " + (long)maxPrice + "VND", "currentPrice");
        }

        this.currentPrice = newPrice;
    }

    public void updateStatus(@NonNull ProductStatus status) {
        if (status == ProductStatus.DELETED && stockQuantity > 0) {
            status = ProductStatus.DEACTIVATED;
        }

        this.status = status;
    }

    protected Product(Builder<?> builder) {
        this.title = builder.title;
        this.category = builder.category;
        this.description = builder.description;
        this.height = builder.height;
        this.width = builder.width;
        this.length = builder.length;
        this.weight = builder.weight;
        this.barcode = builder.barcode;
        this.originalValue = builder.originalValue;
        this.currentPrice = builder.currentPrice;
        this.stockQuantity = builder.stockQuantity;
        this.status = builder.status;
        this.imageURL = builder.imageURL;
    }

    public static abstract class Builder<B extends Builder<B>> {
        private String title;
        private String category;
        private String description;
        private BigDecimal height;
        private BigDecimal width;
        private BigDecimal length;
        private BigDecimal weight;
        private String barcode;
        private Long originalValue;
        private Long currentPrice;
        private Integer stockQuantity;
        private ProductStatus status = ProductStatus.ACTIVE;
        private String imageURL;

        protected abstract B self();

        public abstract Product build() throws ProductConstructionException;

        public B title(String title) {
            this.title = title;
            return self();
        }

        public B category(String category) {
            this.category = category;
            return self();
        }

        public B description(String description) {
            this.description = description;
            return self();
        }

        public B height(BigDecimal height) {
            this.height = height;
            return self();
        }

        public B width(BigDecimal width) {
            this.width = width;
            return self();
        }

        public B length(BigDecimal length) {
            this.length = length;
            return self();
        }

        public B weight(BigDecimal weight) {
            this.weight = weight;
            return self();
        }

        public B barcode(String barcode) {
            this.barcode = barcode;
            return self();
        }

        public B originalValue(Long originalValue) {
            this.originalValue = originalValue;
            return self();
        }

        public B currentPrice(Long currentPrice) {
            this.currentPrice = currentPrice;
            return self();
        }

        public B stockQuantity(Integer stockQuantity) {
            this.stockQuantity = stockQuantity;
            return self();
        }

        public B status(ProductStatus status) {
            this.status = status;
            return self();
        }

        public B imageURL(String imageURL) {
            this.imageURL = imageURL;
            return self();
        }

        private final Map<String, List<String>> invalidFields = new HashMap<>();

        protected final void throwProductConstructionExceptionIfAny() throws ProductConstructionException {
            if (!this.invalidFields.isEmpty()) {
                throw new ProductConstructionException(this.invalidFields);
            }
        }

        protected final void validate(Runnable step) {
            try {
                step.run();
            } catch (ProductValidationException e) {
                invalidFields.computeIfAbsent(e.getInvalidFieldName(), k -> new ArrayList<>()).add(e.getMessage());
            }
        }

        protected B validate() {
            this.validate(() -> requireNonBlank(this.title, "title"));
            this.validate(() -> requireNonBlank(this.category, "category"));
            this.validate(() -> requireNonBlank(this.description, "description"));

            this.validate(() -> requirePositive(this.height, "height"));
            this.validate(() -> requirePositive(this.width, "width"));
            this.validate(() -> requirePositive(this.length, "length"));
            this.validate(() -> requirePositive(this.weight, "weight"));

            this.validate(() -> requireNonBlank(this.barcode, "barcode"));

            this.validate(() -> requireNotNull(this.originalValue, "originalValue"));
            this.validate(() -> requireNonNegative(this.originalValue, "originalValue"));
            this.validate(() -> requireNotNull(this.currentPrice, "currentPrice"));
            this.validate(() -> requireNonNegative(this.currentPrice, "currentPrice"));
            this.validate(() -> requireNotNull(this.stockQuantity, "stockQuantity"));
            this.validate(() -> requireNonNegative(this.stockQuantity, "stockQuantity"));

            this.validate(() -> {
                if (this.originalValue != null && this.currentPrice != null && this.stockQuantity != null) {
                    double minPrice = MIN_PRICE_RELATIVE * this.originalValue;
                    double maxPrice = MAX_PRICE_RELATIVE * this.originalValue;

                    if (this.currentPrice < minPrice || this.currentPrice > maxPrice) {
                        throw new ProductValidationException("Current price must be between " + (long)minPrice + "VND and " + (long)maxPrice + "VND", "currentPrice");
                    }
                }
            });

            return self();
        }
    }

    protected static void requireNonBlank(String value, String field) throws ProductValidationException {
        if (value == null || value.isBlank()) {
            throw new ProductValidationException(field + " must not be blank", field);
        }
    }

    protected static void requireNonNegative(long value, String field) throws ProductValidationException {
        if (value < 0) {
            throw new ProductValidationException(field + " must not be negative", field);
        }
    }

    protected static void requirePositive(BigDecimal value, String field) throws ProductValidationException {
        if (value.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductValidationException(field + " must be positive", field);
        }
    }

    protected static void requirePositive(long value, String field) throws ProductValidationException {
        if (value <= 0) {
            throw new ProductValidationException(field + " must be positive", field);
        }
    }

    protected static void requireNotEmpty(List<?> list, String field) throws ProductValidationException {
        if (list == null || list.isEmpty()) {
            throw new ProductValidationException(field + " must not be empty", field);
        }
    }

    protected static void requireNotNull(Object value, String field) throws ProductValidationException {
        if (value == null) {
            throw new ProductValidationException(field + " must not be null", field);
        }
    }
}
