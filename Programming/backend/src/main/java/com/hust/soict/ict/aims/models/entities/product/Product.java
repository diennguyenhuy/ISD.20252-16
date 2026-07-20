package com.hust.soict.ict.aims.models.entities.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.function.BiConsumer;

import com.hust.soict.ict.aims.models.entities.VersionedEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Immutable;

@Entity
@Table(name = "product")
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class Product extends VersionedEntity {
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

    @Immutable
    @Column(unique = true, nullable = false, updatable = false, length = 32)
    private String barcode;

    @Immutable
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
        if (status == Status.DELETED) {
            throw new IllegalStateException("Cannot delete already deleted product " + title);
        }
        if (stockQuantity == 0) status = Status.DELETED;
        else deactivate();
    }

    private void deactivate() {
        if (status == Status.DEACTIVATED) {
            throw new IllegalStateException("Cannot deactivate already deactivated product " + title);
        }
        status = Status.DEACTIVATED;
    }

    public void activate() throws IllegalStateException {
        if (status == Status.ACTIVE) {
            throw new IllegalStateException("Cannot activate already activated product " + title);
        }
        if (status == Status.DELETED) {
            throw new IllegalStateException("Cannot activate deleted product " + title);
        }
        status = Status.ACTIVE;
    }

    protected Product(Builder<?> builder) {
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
        this.status = Status.ACTIVE;
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
        private String imageURL;

        protected Builder() {}

        protected abstract B self();

        public abstract Product build();

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

        public B imageURL(String imageURL) {
            this.imageURL = imageURL;
            return self();
        }
    }

    public final void update(UpdateCommand<?> command) {
        updateCommands.get(command.getClass()).accept(this, command);
    }

    private static final Map<
            Class<? extends UpdateCommand<?>>,
            BiConsumer<Product, UpdateCommand<?>>
            > updateCommands = new HashMap<>();
    protected static <C extends UpdateCommand<?>, P extends Product>
    void registerUpdateCommand(
            Class<C> commandType,
            Class<P> productType,
            BiConsumer<P, C> consumer
    ) {
        updateCommands.put(commandType, (p, c) -> consumer.accept(productType.cast(p), commandType.cast(c)));
    }
    static {
        registerUpdateCommand(UpdateCommand.Title.class, Product.class, (p, c) -> p.title = c.newValue());
        registerUpdateCommand(UpdateCommand.Category.class, Product.class, (p, c) -> p.category = c.newValue());
        registerUpdateCommand(UpdateCommand.Description.class, Product.class, (p, c) -> p.description = c.newValue());
        registerUpdateCommand(UpdateCommand.Height.class, Product.class, (p, c) -> p.height = c.newValue());
        registerUpdateCommand(UpdateCommand.Width.class, Product.class, (p, c) -> p.width = c.newValue());
        registerUpdateCommand(UpdateCommand.Length.class, Product.class, (p, c) -> p.length = c.newValue());
        registerUpdateCommand(UpdateCommand.Weight.class, Product.class, (p, c) -> p.weight = c.newValue());
        registerUpdateCommand(UpdateCommand.ImageUrl.class, Product.class, (p, c) -> p.imageURL = c.newValue());
    }

    public interface UpdateCommand<T> {
        T newValue();

        record Title(String newValue) implements UpdateCommand<String> {}
        record Category(String newValue) implements UpdateCommand<String> {}
        record Description(String newValue) implements UpdateCommand<String> {}
        record Height(BigDecimal newValue) implements UpdateCommand<BigDecimal> {}
        record Width(BigDecimal newValue) implements UpdateCommand<BigDecimal> {}
        record Length(BigDecimal newValue) implements UpdateCommand<BigDecimal> {}
        record Weight(BigDecimal newValue) implements UpdateCommand<BigDecimal> {}
        record ImageUrl(String newValue) implements UpdateCommand<String> {}
    }
}
