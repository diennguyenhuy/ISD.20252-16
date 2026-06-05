package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;
import java.util.*;

import com.hust.soict.ict.aims.models.entities.AuditableEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Product extends AuditableEntity {
    public enum Status {
        ACTIVE,
        DEACTIVATED,
        DELETED
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(updatable = false)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(columnDefinition = "TEXT")
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

    @Column(unique = true, nullable = false, updatable = false, length = 32)
    private String barcode;

    @Column(nullable = false, updatable = false)
    private long originalValue;

    @Column(nullable = false)
    private long currentPrice;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.ACTIVE;

    @Column(name = "image_url", length = 2048)
    private String imageURL;

    @Version
    private Long version;

    public static final int MIN_PRICE_RELATIVE_PERCENTAGE = 30;
    public static final int MAX_PRICE_RELATIVE_PERCENTAGE = 150;

    public void updatePrice(long newPrice) throws IllegalArgumentException {
        if (newPrice == currentPrice) return;

        if (newPrice < 0) {
            throw new IllegalArgumentException("New price must not be negative");
        }

        long minPrice = originalValue * MIN_PRICE_RELATIVE_PERCENTAGE / 100;
        long maxPrice = originalValue * MAX_PRICE_RELATIVE_PERCENTAGE / 100;

        if (newPrice < minPrice || newPrice > maxPrice) {
            throw new IllegalArgumentException("Current price must be between " + minPrice + "VND and " + maxPrice + "VND");
        }

        this.currentPrice = newPrice;
    }

    public void updateStock(int newStock) throws IllegalStateException, IllegalArgumentException {
        if (newStock == stockQuantity) return;

        if (newStock < 0)  {
            throw new IllegalArgumentException("New stock " + newStock + " must not be negative");
        }

        if (newStock > 0 && status == Status.DELETED) {
            throw new IllegalStateException("Deleted product should not have positive stock of " + newStock);
        }

        this.stockQuantity = newStock;
    }

    public void delete() {
        if (status == Status.DELETED) return;

        if (stockQuantity == 0) status = Status.DELETED;
        else deactivate();
    }

    private void deactivate() {
        if (status == Status.DEACTIVATED) return;
        status = Status.DEACTIVATED;
    }

    public void activate() throws IllegalStateException {
        if (status == Status.ACTIVE) return;

        if (status == Status.DELETED) {
            throw new IllegalStateException("Cannot activate deleted product #" + id);
        }

        status = Status.ACTIVE;
    }

    protected Product(Builder<?, ?> builder) {
        this.title = Objects.requireNonNull(builder.title, "Product title cannot be null");
        this.category = Objects.requireNonNull(builder.category, "Product category cannot be null");
        this.description = builder.description;
        this.height = Objects.requireNonNull(builder.height, "Product height cannot be null");
        this.width = Objects.requireNonNull(builder.width, "Product width cannot be null");
        this.length = Objects.requireNonNull(builder.length, "Product length cannot be null");
        this.weight = Objects.requireNonNull(builder.weight, "Product weight cannot be null");
        this.barcode = Objects.requireNonNull(builder.barcode, "Product barcode cannot be null");
        this.originalValue = Objects.requireNonNull(builder.originalValue, "Product original value cannot be null");
        this.currentPrice = Objects.requireNonNull(builder.currentPrice,  "Product current price cannot be null");
        this.stockQuantity = Objects.requireNonNull(builder.stockQuantity, "Product stock quantity cannot be null");
        this.status = Objects.requireNonNull(builder.status, "Product status cannot be null");
        this.imageURL = builder.imageURL;
    }

    protected final void apply(Builder<?, ?> builder) {
        this.title = builder.title;
        this.category = builder.category;
        this.description = builder.description;
        this.height = builder.height;
        this.width = builder.width;
        this.length = builder.length;
        this.weight = builder.weight;
        this.imageURL = builder.imageURL;
    }

    public abstract Builder<?, ?> toBuilder();

    public static abstract class Builder<P extends Product, B extends Builder<P, B>> {
        protected final P updatingProduct;
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
        private Status status = Status.ACTIVE;
        private String imageURL;

        protected Builder() {
            this.updatingProduct = null;
        }

        @SuppressWarnings("unchecked")
        protected Builder(Product existingProduct) {
            this.updatingProduct = (P) existingProduct;
            this.title = existingProduct.title;
            this.category = existingProduct.category;
            this.description = existingProduct.description;
            this.height = existingProduct.height;
            this.width = existingProduct.width;
            this.length = existingProduct.length;
            this.weight = existingProduct.weight;
            this.barcode = existingProduct.barcode;
            this.originalValue = existingProduct.originalValue;
            this.currentPrice = existingProduct.currentPrice;
            this.stockQuantity = existingProduct.stockQuantity;
            this.status = existingProduct.status;
            this.imageURL = existingProduct.imageURL;
        }

        protected abstract B self();

        public abstract P build();

        public B title(@NonNull String title) {
            this.title = title;
            return self();
        }

        public B category(@NonNull String category) {
            this.category = category;
            return self();
        }

        public B description(String description) {
            this.description = description;
            return self();
        }

        public B height(@NonNull BigDecimal height) {
            this.height = height;
            return self();
        }

        public B width(@NonNull BigDecimal width) {
            this.width = width;
            return self();
        }

        public B length(@NonNull BigDecimal length) {
            this.length = length;
            return self();
        }

        public B weight(@NonNull BigDecimal weight) {
            this.weight = weight;
            return self();
        }

        public B barcode(@NonNull String barcode) {
            this.barcode = barcode;
            return self();
        }

        public B originalValue(long originalValue) {
            this.originalValue = originalValue;
            return self();
        }

        public B currentPrice(long currentPrice) {
            this.currentPrice = currentPrice;
            return self();
        }

        public B stockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
            return self();
        }

        public B status(@NonNull Status status) {
            this.status = status;
            return self();
        }

        public B status(@NonNull String status) {
            this.status = Status.valueOf(status.toUpperCase());
            return self();
        }

        public B imageURL(String imageURL) {
            this.imageURL = imageURL;
            return self();
        }
    }
}
