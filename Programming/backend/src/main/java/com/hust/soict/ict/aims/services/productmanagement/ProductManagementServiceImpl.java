package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.exceptions.ExceededDeletionQuotaException;
import com.hust.soict.ict.aims.exceptions.ProductAlreadyExistedException;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.*;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
class ProductManagementServiceImpl implements ProductManagementService, ProductQueryService {

    private final ProductRepository productRepo;
    private final ProductFactory productFactory;

    @Override
    @Transactional(readOnly = true)
    public List<Product> getAllProductsForManager() {
        log.info("[FETCH] Fetching all products for manager dashboard.");
        return productRepo.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductByIdForManager(UUID id) {
        log.info("[FETCH] Fetching product details. ID: {}", id);
        return productRepo.findById(id)
                .orElseThrow(() -> {
                    log.warn("[FETCH FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });
    }
    
    @Override
    @Transactional
    public Product createProduct(CreateProductRequest dto) {
        log.info("[CREATE] Received request to create new product. Barcode: {}", dto.getBarcode());

        if (productRepo.existsByBarcode(dto.getBarcode())) {
            log.warn("[CREATE FAILED] Barcode already exists: {}", dto.getBarcode());
            throw new ProductAlreadyExistedException("Barcode already exists: " + dto.getBarcode());
        }

        Product product = productFactory.createProduct(dto);
        product = productRepo.save(product);

        log.info("[CREATE SUCCESS] Successfully saved new product. ID: {}", product.getId());
        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequest dto) {
        log.info("[UPDATE] Received request to update product. ID: {}", id);

        Product product = productRepo.findById(id).orElseThrow(() -> {
            log.error("[UPDATE FAILED] Product not found. ID: {}", id);
            return new ProductNotFoundException(id);
        });

        dto.getCurrentPrice().ifDefined(product::updatePrice);

        product = productFactory.updateProduct(product, dto);
        product = productRepo.save(product);
        log.info("[UPDATE SUCCESS] Successfully updated product. ID: {}, Current Price: {}", product.getId(), product.getCurrentPrice());

        return product;
    }

    @Override
    @Transactional
    public List<Product> deleteProducts(List<UUID> productIds) {
        log.info("[DELETE BATCH] Received request to delete {} products.", productIds.size());

        java.time.Instant startOfToday = java.time.LocalDate.now()
                .atStartOfDay(java.time.ZoneId.systemDefault())
                .toInstant();

        long deletedToday = productRepo.countByStatusInAndUpdatedAtAfter(
                List.of(Product.Status.DEACTIVATED, Product.Status.DELETED),
                startOfToday
        );

        if (deletedToday + productIds.size() > 20) {
            log.warn("[DELETE BATCH FAILED] Daily quota exceeded! Already deleted today: {}, Requested: {}", deletedToday, productIds.size());
            throw new ExceededDeletionQuotaException("Daily deletion quota exceeded (Max 20 per day). Already deleted today: " + deletedToday + ", requested: " + productIds.size());
        }

        List<Product> products = productRepo.findAllById(productIds);

        products.forEach(Product::delete);

        log.info("[DELETE BATCH SUCCESS] Successfully processed {} products.", products.size());

        return products;
    }

    @Override
    @Transactional
    public Product adjustStock(UUID id, int delta, String reason) {
        log.info("[ADJUST STOCK] Received request for Product ID: {}. Delta: {}, Reason: '{}'", id, delta, reason);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("[ADJUST STOCK FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });


        int newStock = product.getStockQuantity() + delta;
        product.updateStock(newStock);

        productRepo.save(product);
        log.info("[ADJUST STOCK SUCCESS] Successfully updated stock. ID: {}, New Stock: {}", product.getId(), product.getStockQuantity());

        return product;
    }

    @Override
    @Transactional
    public Product activateProduct(UUID id) {
        log.info("[ACTIVATE] Received request to reactivate product. ID: {}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("[ACTIVATE FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        product.activate();
        product = productRepo.save(product);

        log.info("[ACTIVATE SUCCESS] Successfully reactivated product. ID: {}", id);

        return product;
    }

    @Override
    @Transactional
    public Product deleteProduct(UUID id) {
        log.info("[DELETE] Received request to delete product. ID: {}", id);

        Product product = productRepo.findById(id)
                .orElseThrow(() -> {
                    log.error("[DELETE FAILED] Product not found. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        product.delete();
        product = productRepo.save(product);

        Product.Status actualStatus = product.getStatus();

        log.info("[DELETE SUCCESS] Successfully {} product. ID: {}", actualStatus.name().toLowerCase(), id);

        return product;
    }
}