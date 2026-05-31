package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.*;
import com.hust.soict.ict.aims.models.entities.AuditableEntity; // <-- IMPORT THÊM CÁI NÀY
import com.hust.soict.ict.aims.models.entities.product.*;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
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
public class ProductManagementService {

    private final ProductRepository productRepo;
    private final ProductMapper productMapper;
    private final ProductFactory productFactory;

    @Transactional
    public ProductDetail createProduct(CreateProductRequest dto) {
        if (productRepo.existsByBarcode(dto.getBarcode())) {
            throw new ProductValidationException("Barcode already exists: " + dto.getBarcode(), "barcode");
        }

        Product product = productFactory.buildNewProduct(dto);
        Product savedProduct = productRepo.save(product);

        return productMapper.toProductDetail(savedProduct);
    }

    @Transactional
    public ProductDetail updateProduct(UUID id, UpdateProductRequest dto) {
        Product product = productRepo.findById(id).orElseThrow(() -> new ProductNotFoundException(id));

        if (dto.getCurrentPrice() != null) {
            product.changePrice(dto.getCurrentPrice()); // Validate price 30% - 150%
        }

        Product updatedProduct = productFactory.buildUpdatedProduct(product, dto);

        restoreEntityIdentityWithReflection(updatedProduct, product);

        Product savedProduct = productRepo.save(updatedProduct);
        return productMapper.toProductDetail(savedProduct);
    }

    @Transactional
    public void deleteProducts(List<UUID> productIds) {
        if (productIds == null || productIds.size() > 10) {
            throw new ProductValidationException("Cannot delete more than 10 products at a time.", "productIds");
        }

//        long deletedToday = productRepo.countByStatusIn(List.of(Product.Status.DEACTIVATED, Product.Status.DELETED));
//        if (deletedToday + productIds.size() > 20) {
//            throw new ProductValidationException("Daily deletion quota exceeded (Max 20 per day).", "status");
//        }

        for (UUID id : productIds) {
            productRepo.findById(id).ifPresent(product -> {
                product.updateStatus(Product.Status.DELETED);

                Product deletedProduct = productFactory.buildDeletedProduct(product);

                restoreEntityIdentityWithReflection(deletedProduct, product);

                productRepo.save(deletedProduct);
            });
        }
    }

    // 1. Product list for Manager
    @Transactional(readOnly = true)
    public List<ProductSummary> getAllProductsForManager() {
        return productRepo.findAll().stream()
                .map(productMapper::toProductSummary)
                .toList();
    }

    // 2. Product detail for Manager
    @Transactional(readOnly = true)
    public ProductDetail getProductByIdForManager(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        return productMapper.toProductDetail(product);
    }

    // 3. Adjust Stock
    @Transactional
    public void adjustStock(UUID id, int delta, String reason) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));

        int newStock = product.getStockQuantity() + delta;
        if (newStock < 0) {
            throw new ProductValidationException("Stock cannot be negative", "stockQuantity");
        }

        UpdateProductRequest updateReq = new UpdateProductRequest();
        updateReq.setStockQuantity(newStock);

        Product updatedProduct = productFactory.buildUpdatedProduct(product, updateReq);

        restoreEntityIdentityWithReflection(updatedProduct, product);

        productRepo.save(updatedProduct);

        // TODO: (Tương lai) Ghi log lý do (reason) vào bảng StockAdjustLog tại đây
    }
    private void restoreEntityIdentityWithReflection(Product targetProduct, Product existingProduct) {
        try {
            // 1. Phục hồi ID
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(targetProduct, existingProduct.getId());

            // 2. Phục hồi Version (CHỐNG LỖI 500 INTERNAL SERVER ERROR)
            Field versionField = Product.class.getDeclaredField("version");
            versionField.setAccessible(true);
            versionField.set(targetProduct, existingProduct.getVersion());

            // 3. Phục hồi CreatedAt (Lấy từ class cha AuditableEntity)
            Field createdAtField = AuditableEntity.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(targetProduct, existingProduct.getCreatedAt());

        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Reflection failure", e);
        }
    }

    @Transactional
    public void activateProduct(UUID id) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
        product.updateStatus(Product.Status.ACTIVE);
        productRepo.save(product);
    }
}