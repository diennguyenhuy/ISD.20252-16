package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ProductValidationException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
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
        product.changePrice(dto.getCurrentPrice()); // Validate price 30% - 150%

        Product updatedProduct = productFactory.buildUpdatedProduct(product, dto);
        setProductIdWithReflection(updatedProduct, id);

        Product savedProduct = productRepo.save(updatedProduct);
        return productMapper.toProductDetail(savedProduct);
    }

    @Transactional
    public void deleteProducts(List<UUID> productIds) {
        if (productIds == null || productIds.size() > 10) {
            throw new ProductValidationException("Cannot delete more than 10 products at a time.", "productIds");
        }

        long deletedToday = productRepo.countByStatusIn(List.of(Product.Status.DEACTIVATED, Product.Status.DELETED));
        if (deletedToday + productIds.size() > 20) {
            throw new ProductValidationException("Daily deletion quota exceeded (Max 20 per day).", "status");
        }

        for (UUID id : productIds) {
            productRepo.findById(id).ifPresent(product -> {
                product.updateStatus(Product.Status.DELETED);

                Product deletedProduct = productFactory.buildDeletedProduct(product);
                setProductIdWithReflection(deletedProduct, id);

                productRepo.save(deletedProduct);
            });
        }
    }

    private void setProductIdWithReflection(Product targetProduct, UUID oldId) {
        try {
            Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(targetProduct, oldId);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Reflection failure", e);
        }
    }
}