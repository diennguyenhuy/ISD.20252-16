package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.*;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.*;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
class LoggingProductManagementService implements ProductManagementService {
    private final ProductManagementService productManagementService;

    private final ProductRepository productRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ProductLogRepository productLogRepository;

    @Override
    @Transactional
    public ProductDetail createProduct(CreateProductRequest dto) {
        var result = productManagementService.createProduct(dto);

        Product product = productRepository.findByBarcode(dto.getBarcode())
                .orElseThrow(() -> {
                    log.error(
                            "[AUDIT FAILED] Cannot find created product with barcode: {}",
                            dto.getBarcode()
                    );

                    return new IllegalStateException("Cannot find created product with barcode: " + dto.getBarcode());
                });

        ProductLog productLog = ProductAction.CREATE.log(authenticationFacade.getCurrentUser(), product);
        productLogRepository.save(productLog);

        log.info(
                "[AUDIT] Product creation logged. Product ID: {}, Title: {}",
                product.getId(),
                product.getTitle()
        );

        return result;
    }

    private static Map<String, ?> extractUpdatingFields(Set<String> requestedFieldNames, Object object) {
        Map<String, Object> valueMap = new HashMap<>();

        for (Class<?> clazz = object.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    if (!requestedFieldNames.contains(fieldName)) {
                        continue;
                    }
                    Object value = field.get(object);
                    valueMap.put(fieldName, value);
                } catch (IllegalAccessException e) {
                    log.error("[AUDIT FAILED] Cannot access field: {}. Reason: {}", field.getName(), e.getMessage());
                    throw new IllegalStateException("Failed to access field: " + field.getName(), e);
                }
            }
        }

        return valueMap;
    }

    @Override
    @Transactional
    public ProductDetail updateProduct(UUID id, UpdateProductRequest dto) {
        Set<String> requestedFieldNames = new HashSet<>();

        for (Class<?> clazz = dto.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) {
                    continue;
                }

                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    // All Update DTOs have UpdateFieldRequest for each field so down-casting is safe
                    UpdateFieldRequest<?> value = (UpdateFieldRequest<?>) field.get(dto);
                    if (value.isDefined()) {
                        requestedFieldNames.add(fieldName);
                    }
                } catch (IllegalAccessException e) {
                    log.error("[AUDIT FAILED] Cannot access DTO field: {}", field.getName());
                    throw new IllegalStateException("Failed to access DTO field: " + field.getName(), e);
                }
            }
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AUDIT FAILED] Product not found for updates. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        Map<String, ?> oldValueMap = extractUpdatingFields(requestedFieldNames, product);

        var result = productManagementService.updateProduct(id, dto);

        Map<String, ?> newValueMap = extractUpdatingFields(requestedFieldNames, result);

        ProductUpdateLog productLog = new ProductUpdateLog(
                authenticationFacade.getCurrentUser(),
                product,
                requestedFieldNames,
                oldValueMap,
                newValueMap
        );
        productLogRepository.save(productLog);

        log.info("[AUDIT SUCCESS] Product Update Logged. Product ID: {}. Changes:", id);
        for (String fieldName : requestedFieldNames) {
            log.info("{}: {}->{}", fieldName, oldValueMap.get(fieldName).toString(), newValueMap.get(fieldName).toString());
        }
        return result;
    }

    @Override
    @Transactional
    public List<ProductSummary> deleteProducts(List<UUID> productIds) {
        var result = productManagementService.deleteProducts(productIds);

        List<Product> products = productRepository.findAllById(productIds);

        List<ProductLog> productLogs = new ArrayList<>();

        products.forEach(product -> {
            ProductAction actualAction;
            switch (product.getStatus()) {
                case DELETED -> actualAction = ProductAction.DELETE;
                case DEACTIVATED -> actualAction = ProductAction.DEACTIVATE;
                default -> {
                    log.warn(
                            "[AUDIT SKIPPED] Product deletion action produced unexpected status. ID: {}, Status: {}",
                            product.getId(),
                            product.getStatus()
                    );
                    return;
                }
            }

            productLogs.add(actualAction.log(authenticationFacade.getCurrentUser(), product));

            log.info(
                    "[AUDIT] Product {}d. ID: {}, Title: {}",
                    actualAction.name().toLowerCase(),
                    product.getId(),
                    product.getTitle()
            );
        });
        productLogRepository.saveAll(productLogs);

        return result;
    }

    @Override
    @Transactional
    public void adjustStock(UUID id, AdjustStockRequest adjustStockRequest) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AUDIT FAILED] Product not found for stock adjustment. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        int oldStock = product.getStockQuantity();

        productManagementService.adjustStock(id, adjustStockRequest);

        int newStock = oldStock + adjustStockRequest.delta();

        StockAdjustLog stockAdjustLog = new StockAdjustLog(
                authenticationFacade.getCurrentUser(),
                product,
                oldStock,
                newStock,
                adjustStockRequest.reason()
        );
        productLogRepository.save(stockAdjustLog);

        log.info(
                "[AUDIT SUCCESS] Stock adjustment logged. Product ID: {}, Old Stock: {}, New Stock: {}, Delta: {}",
                id,
                oldStock,
                newStock,
                adjustStockRequest.delta()
        );
    }

    @Override
    @Transactional
    public ProductDetail activateProduct(UUID id) {
        var result = productManagementService.activateProduct(id);

        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("[AUDIT FAILED] Product not found for activation. ID: {}", id);
            return new ProductNotFoundException(id);
        });

        ProductLog productLog = ProductAction.ACTIVATE.log(authenticationFacade.getCurrentUser(), product);
        productLogRepository.save(productLog);

        log.info(
                "[AUDIT] Product activated. ID: {}, Title: {}",
                product.getId(),
                product.getTitle()
        );

        return result;
    }

    @Override
    @Transactional
    public ProductDetail deleteProduct(UUID id) {
        var result = productManagementService.deleteProduct(id);
        Product product = productRepository.findById(id).orElseThrow(() -> {
            log.error("[AUDIT FAILED] Product not found for (soft-)deletion. ID: {}", id);
            return new ProductNotFoundException(id);
        });

        ProductAction actualAction;
        switch (product.getStatus()) {
            case DELETED -> actualAction = ProductAction.DELETE;
            case DEACTIVATED -> actualAction = ProductAction.DEACTIVATE;
            default -> {
                log.warn(
                        "[AUDIT SKIPPED] Product deletion action produced unexpected status. ID: {}, Status: {}",
                        id,
                        product.getStatus()
                );
                return result;
            }
        }

        ProductLog productLog = actualAction.log(authenticationFacade.getCurrentUser(), product);
        productLogRepository.save(productLog);

        log.info(
                "[AUDIT] Product {}d. ID: {}, Title: {}",
                actualAction.name().toLowerCase(),
                product.getId(),
                product.getTitle()
        );

        return result;
    }
}
