package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.AuditableEntity;
import com.hust.soict.ict.aims.models.entities.product.*;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.util.List;
import java.util.UUID;

/**
 * Cohesion: Communicational Cohesion<br>
 * Reason: Coordinates the product management workflow (create, update, delete) where all operations execute on the same core domain data (Product).<br>
 * Coupling:
 * - Data coupling with ProductRepository and ProductFactory through simple method calls and passing primitive identifiers (UUID).
 * - Stamp coupling with CreateProductRequest, UpdateProductRequest, and ProductMapper because composite data transfer objects are passed between modules.
 * Design Strength:
 * Acts as a clean delegator, decoupling business orchestration from physical object creation (Factory) and persistence operations (Repository), ensuring high testability.
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductManagementService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;
    private final ProductFactory productFactory;

    @Transactional
    public ProductDetail createProduct(CreateProductRequest dto) {
        log.info("[CREATE] Received request to create new product. Barcode: {}", dto.getBarcode());

        if (productRepo.existsByBarcode(dto.getBarcode())) {
            log.warn("[CREATE FAILED] Barcode already exists: {}", dto.getBarcode());
            throw new ProductValidationException("Barcode already exists: " + dto.getBarcode(), "barcode");
        }

        Product product = productFactory.buildNewProduct(dto);
        Product savedProduct = productRepo.save(product);

        log.info("[CREATE SUCCESS] Successfully saved new product. ID: {}", savedProduct.getId());
        return productMapper.toProductDetail(savedProduct);
    }

    @Transactional
    public ProductDetail updateProduct(UUID id, UpdateProductRequest dto) {
        log.info("[UPDATE] Received request to update product. ID: {}", id);

        Product product = productRepo.findById(id).orElseThrow(() -> {
            log.error("[UPDATE FAILED] Product not found. ID: {}", id);
            return new ProductNotFoundException(id);
        });

        if (dto.getCurrentPrice() != null) {
            product.changePrice(dto.getCurrentPrice()); // Validate price 30% - 150%
        }

        Product updatedProduct = productFactory.buildUpdatedProduct(product, dto);
        restoreEntityIdentityWithReflection(updatedProduct, product);

        Product savedProduct = productRepo.save(updatedProduct);
        log.info("[UPDATE SUCCESS] Successfully updated product. ID: {}, Current Price: {}", savedProduct.getId(), savedProduct.getCurrentPrice());

        return productMapper.toProductDetail(savedProduct);
    }

    @Transactional
    public void deleteProducts(List<UUID> productIds) {
        log.info("[DELETE BATCH] Received request to delete {} products.", productIds != null ? productIds.size() : 0);

        if (productIds == null || productIds.size() > 10) {
            log.warn("[DELETE BATCH FAILED] Exceeded maximum allowed limit of 10 products per request.");
            throw new ProductValidationException("Cannot delete more than 10 products at a time.", "productIds");
        }

        java.time.Instant startOfToday = java.time.LocalDate.now()
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant();

        long deletedToday = productRepo.countByStatusInAndUpdatedAtAfter(
                List.of(Product.Status.DEACTIVATED, Product.Status.DELETED),
                startOfToday
        );

        if (deletedToday + productIds.size() > 20) {
            log.warn("[DELETE BATCH FAILED] Daily quota exceeded! Already deleted today: {}, Requested: {}", deletedToday, productIds.size());
            throw new ProductValidationException("Daily deletion quota exceeded (Max 20 per day).", "status");
        }

        int processedCount = 0;
        for (UUID id : productIds) {
            productRepo.findById(id).ifPresent(product -> {
                product.updateStatus(Product.Status.DELETED);

                productRepo.save(product);

                log.info("[DELETE PROCESS] Product ID: {} status updated to: {}", id, product.getStatus());
            });
            processedCount++;
        }

        log.info("[DELETE BATCH SUCCESS] Successfully processed {} products.", processedCount);
    }

    // 1. Product list for Manager
    @Transactional(readOnly = true)
    public List<ProductSummary> getAllProductsForManager() {
        log.info("[FETCH] Fetching all products for manager dashboard.");
        return productRepo.findAll().stream()
                .map(productMapper::toProductSummary)
                .toList();
    }

    // 2. Product detail for Manager
    @Transactional(readOnly = true)
    public ProductDetail getProductByIdForManager(UUID id) {
        log.info("[FETCH] Fetching product details. ID: {}", id);
        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("[FETCH FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });
        return productMapper.toProductDetail(product);
    }

    // 3. Adjust Stock
    @Transactional
    public void adjustStock(UUID id, int delta, String reason) {
        log.info("[ADJUST STOCK] Received request for Product ID: {}. Delta: {}, Reason: '{}'", id, delta, reason);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("[ADJUST STOCK FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        int newStock = product.getStockQuantity() + delta;
        if (newStock < 0) {
            log.warn("[ADJUST STOCK FAILED] Resulting stock cannot be negative. ID: {}, Current Stock: {}, Delta: {}", id, product.getStockQuantity(), delta);
            throw new ProductValidationException("Stock cannot be negative", "stockQuantity");
        }

        UpdateProductRequest updateReq = new UpdateProductRequest();
        updateReq.setStockQuantity(newStock);

        Product updatedProduct = productFactory.buildUpdatedProduct(product, updateReq);
        restoreEntityIdentityWithReflection(updatedProduct, product);

        productRepo.save(updatedProduct);
        log.info("[ADJUST STOCK SUCCESS] Successfully updated stock. ID: {}, New Stock: {}", updatedProduct.getId(), updatedProduct.getStockQuantity());

        // TODO: (Tương lai) Ghi log lý do (reason) vào bảng StockAdjustLog tại đây
    }

    private void restoreEntityIdentityWithReflection(Product targetProduct, Product existingProduct) {
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(targetProduct, existingProduct.getId());

            Field versionField = Product.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(targetProduct, existingProduct.getVersion());

            Field createdAtField = AuditableEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(targetProduct, existingProduct.getCreatedAt());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            log.error("[SYSTEM ERROR] Reflection failure while restoring entity identity.", e);
            throw new RuntimeException("Reflection failure", e);
        }
    }

    @Transactional
    public void activateProduct(UUID id) {
        log.info("[ACTIVATE] Received request to reactivate product. ID: {}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("[ACTIVATE FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        product.updateStatus(Product.Status.ACTIVE);
        productRepo.save(product);

        log.info("[ACTIVATE SUCCESS] Successfully reactivated product. ID: {}", id);
    }
}