package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.*;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.*;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

@RequiredArgsConstructor
@Slf4j
class ProductManagementServiceLogging implements ProductManagementService {
    private final ProductManagementService productManagementService;

    private final ProductRepository productRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ProductLogRepository productLogRepository;

    @Override
    @Transactional
    public Product createProduct(CreateProductRequest dto) {
        var product = productManagementService.createProduct(dto);

        ProductLog productLog = ProductActions.CREATE.log(authenticationFacade.getCurrentUser(), product);
        productLogRepository.save(productLog);

        log.info(
                "[AUDIT] Product creation logged. Product ID: {}, Title: {}",
                product.getId(),
                product.getTitle()
        );

        return product;
    }

    @Override
    @Transactional
    public Product updateProduct(UUID id, UpdateProductRequest dto) {
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

        product = productManagementService.updateProduct(id, dto);

        Map<String, ?> newValueMap = extractUpdatingFields(requestedFieldNames, product);

        ProductUpdateLog productLog = ProductActions.UPDATE.log(
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
        return product;
    }

    @Override
    @Transactional
    public List<Product> deleteProducts(List<UUID> productIds) {
        var products = productManagementService.deleteProducts(productIds);

        List<ProductLog> productLogs = new ArrayList<>();

        products.forEach(product -> {
            ProductLog productLog = switch (product.getStatus()) {
                case DELETED -> ProductActions.DELETE.log(authenticationFacade.getCurrentUser(), product);
                case DEACTIVATED -> ProductActions.DEACTIVATE.log(authenticationFacade.getCurrentUser(), product);
                default -> {
                    log.warn(
                            "[AUDIT SKIPPED] Product deletion action produced unexpected status. ID: {}, Status: {}",
                            product.getId(),
                            product.getStatus()
                    );
                    yield null;
                }
            };

            if (productLog != null) productLogs.add(productLog);
            else return;

            log.info(
                    "[AUDIT] Product {}d. ID: {}, Title: {}",
                    productLog.getAction().name().toLowerCase(),
                    product.getId(),
                    product.getTitle()
            );
        });
        productLogRepository.saveAll(productLogs);

        return products;
    }

    @Override
    @Transactional
    public Product adjustStock(UUID id, int delta, String reason) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AUDIT FAILED] Product not found for stock adjustment. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        int oldStock = product.getStockQuantity();

        product = productManagementService.adjustStock(id, delta, reason);

        int newStock = oldStock + delta;

        StockAdjustLog stockAdjustLog = ProductActions.STOCK_ADJUST.log(
                authenticationFacade.getCurrentUser(),
                product,
                oldStock,
                newStock,
                reason
        );
        productLogRepository.save(stockAdjustLog);

        log.info(
                "[AUDIT SUCCESS] Stock adjustment logged. Product ID: {}, Old Stock: {}, New Stock: {}, Delta: {}",
                id,
                oldStock,
                newStock,
                delta
        );
        return product;
    }

    @Override
    @Transactional
    public Product activateProduct(UUID id) {
        var product = productManagementService.activateProduct(id);

        ProductLog productLog = ProductActions.ACTIVATE.log(authenticationFacade.getCurrentUser(), product);
        productLogRepository.save(productLog);

        log.info(
                "[AUDIT] Product activated. ID: {}, Title: {}",
                product.getId(),
                product.getTitle()
        );

        return product;
    }

    @Override
    @Transactional
    public Product deleteProduct(UUID id) {
        var product = productManagementService.deleteProduct(id);

        ProductLog productLog = switch (product.getStatus()) {
            case DELETED -> ProductActions.DELETE.log(authenticationFacade.getCurrentUser(), product);
            case DEACTIVATED -> ProductActions.DEACTIVATE.log(authenticationFacade.getCurrentUser(), product);
            default -> {
                log.warn(
                        "[AUDIT SKIPPED] Product deletion action produced unexpected status. ID: {}, Status: {}",
                        product.getId(),
                        product.getStatus()
                );
                yield null;
            }
        };

        if (productLog != null) {
            productLogRepository.save(productLog);
            log.info(
                    "[AUDIT] Product {}d. ID: {}, Title: {}",
                    productLog.getAction().name().toLowerCase(),
                    product.getId(),
                    product.getTitle()
            );
        }

        return product;
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
}

@Configuration
class ProductManagementConfiguration {
    @Primary @Bean
    public ProductManagementService productManagementService(
            ProductManagementServiceImpl productManagementService,
            ProductRepository productRepository,
            AuthenticationFacade authenticationFacade,
            ProductLogRepository productLogRepository
    ) {
        return new ProductManagementServiceLogging(
                productManagementService,
                productRepository,
                authenticationFacade,
                productLogRepository
        );
    }
}
