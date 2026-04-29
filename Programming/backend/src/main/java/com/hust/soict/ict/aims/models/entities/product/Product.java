package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
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

    /**
     * Unit: centimeters cm
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal height;

    /**
     * Unit: centimeters cm
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal width;

    /**
     * Unit: centimeters cm
     */
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal length;

    /**
     * Unit: kilograms kg
     */
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

    public void setCurrentPrice(Long currentPrice) throws IllegalArgumentException {
        ProductValidator.validatePriceRange(originalValue, currentPrice);
        this.currentPrice = currentPrice;
    }

    protected Product(Builder<?> builder) throws ProductValidationException {
        ProductValidator.validateTitle(builder.title);
        ProductValidator.validateCategory(builder.category);
        ProductValidator.validateDescription(builder.description);
        ProductValidator.validateHeight(builder.height);
        ProductValidator.validateWidth(builder.width);
        ProductValidator.validateLength(builder.length);
        ProductValidator.validateWeight(builder.weight);
        ProductValidator.validateBarcode(builder.barcode);
        ProductValidator.validateOriginalValue(builder.originalValue);
        ProductValidator.validatePriceRange(builder.currentPrice, builder.originalValue);
        ProductValidator.validateStockQuantity(builder.stockQuantity);
        ProductValidator.validateStatus(builder.status, builder.stockQuantity);

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
        private long originalValue;
        private long currentPrice;
        private int stockQuantity;
        private ProductStatus status = ProductStatus.ACTIVE;
        private String imageURL;

        protected abstract B self();

        public abstract Product build() throws ProductValidationException;

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
    }
}

final class ProductValidator {
    private ProductValidator() {}

    static final double MIN_PRICE_RELATIVE = 0.3;
    static final double MAX_PRICE_RELATIVE = 1.5;

    static void validateTitle(String title) throws ProductValidationException {
        if (title == null || title.isBlank()) {
            throw new ProductValidationException("Title is required", "title");
        }
    }

    static void validateCategory(String category) throws ProductValidationException {
        if (category == null || category.isBlank()) {
            throw new ProductValidationException("Category is required", "category");
        }
    }

    static void validateDescription(String description) throws ProductValidationException {
        if (description == null || description.isBlank()) {
            throw new ProductValidationException("Description is required", "description");
        }
    }

    static void validateHeight(BigDecimal height) throws ProductValidationException {
        if (height == null) {
            throw new ProductValidationException("Height is required", "height");
        }
        if (height.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductValidationException("Height must be required", "height");
        }
    }

    static void validateWidth(BigDecimal width) throws ProductValidationException {
        if (width == null) {
            throw new ProductValidationException("Width is required", "width");
        }
        if (width.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductValidationException("Width must be positive", "width");
        }
    }

    static void validateLength(BigDecimal length) throws ProductValidationException {
        if (length == null) {
            throw new ProductValidationException("Length is required", "length");
        }
        if (length.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductValidationException("Length must be positive", "length");
        }
    }

    static void validateWeight(BigDecimal weight) throws ProductValidationException {
        if (weight == null) {
            throw new ProductValidationException("Weight is required", "weight");
        }
        if (weight.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ProductValidationException("Weight must be positive", "weight");
        }
    }

    static void validateBarcode(String barcode) throws ProductValidationException {
        if (barcode == null || barcode.isBlank()) {
            throw new ProductValidationException("Barcode is required", "barcode");
        }

        if (barcode.length() > 32) {
            throw new ProductValidationException("Barcode cannot be longer than 32 characters", "barcode");
        }
    }

    static void validateOriginalValue(Long originalValue) throws ProductValidationException {
        if (originalValue == null) {
            throw new ProductValidationException("Original value is required", "originalValue");
        }

        if (originalValue < 0) {
            throw new ProductValidationException("Original value must not be negative", "originalValue");
        }
    }

    static void validatePriceRange(Long currentPrice, Long originalValue) throws ProductValidationException {
        if (currentPrice == null) {
            throw new ProductValidationException("Current price is required", "currentPrice");
        }

        if (currentPrice < 0) {
            throw new ProductValidationException("Current price must not be negative", "currentPrice");
        }

        double minPrice = MIN_PRICE_RELATIVE * originalValue;
        double maxPrice = MAX_PRICE_RELATIVE * originalValue;

        if (currentPrice < minPrice || currentPrice > maxPrice) {
            throw new ProductValidationException("Current price must be between " + (long)minPrice + "VND and " + (long)maxPrice + "VND", "currentPrice");
        }
    }

    static void validateStockQuantity(Integer stockQuantity) throws ProductValidationException {
        if (stockQuantity == null) {
            throw new ProductValidationException("Stock quantity is required", "stockQuantity");
        }

        if (stockQuantity < 0) {
            throw new ProductValidationException("Stock quantity must not be negative", "stockQuantity");
        }
    }

    static void validateStatus(ProductStatus status, Integer stockQuantity) throws ProductValidationException {
        if (status == null) {
            throw new ProductValidationException("Status is required", "status");
        }

        if (status == ProductStatus.DELETED && stockQuantity != 0) {
            throw new ProductValidationException("Status can only be DELETED when stock quantity is 0", "status");
        }
    }
}
