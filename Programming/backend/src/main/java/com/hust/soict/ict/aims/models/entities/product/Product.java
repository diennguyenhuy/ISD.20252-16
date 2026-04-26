package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter @Setter
@NoArgsConstructor
public abstract class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Setter(AccessLevel.NONE)
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
    private long originalValue;

    @Column(nullable = false)
    private long currentPrice;

    @Column(nullable = false)
    private int stockQuantity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ProductStatus status;

    @Column(name = "image_url", length = 2048)
    private String imageURL;

//    @Version
//    @Setter(AccessLevel.NONE)
//    private long version;

    @CreationTimestamp
    @Setter(AccessLevel.NONE)
    @Column(updatable = false, nullable = false)
    private Instant addedAt;

    @UpdateTimestamp
    @Setter(AccessLevel.NONE)
    @Column(nullable = false)
    private Instant updatedAt;

    public static final double MIN_PRICE_RELATIVE = 0.3;
    public static final double MAX_PRICE_RELATIVE = 1.5;

    public void setCurrentPrice(long currentPrice) throws IllegalArgumentException {
        double minPrice = MIN_PRICE_RELATIVE * originalValue;
        double maxPrice = MAX_PRICE_RELATIVE * originalValue;

        if (currentPrice < minPrice || currentPrice > maxPrice) {
            throw new IllegalArgumentException("Product price must be between " + (long)minPrice + "VND and " + (long)maxPrice + "VND");
        }

        this.currentPrice = currentPrice;
    }
}
