package com.hust.soict.ict.aims.services.productmanagement.log;

import com.hust.soict.ict.aims.dto.request.UpdateFieldRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.audit.ProductActions;
import com.hust.soict.ict.aims.models.entities.audit.ProductLog;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductLogRepository;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.security.AuthenticationFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.UUID;

@Component @Aspect
@RequiredArgsConstructor
@Slf4j
class ProductUpdateLogging {
    private final ProductRepository productRepository;
    private final AuthenticationFacade authenticationFacade;
    private final ProductLogRepository productLogRepository;

    @Around(value = "@annotation(AuditUpdate) && args(id, dto)", argNames = "pjp,id,dto")
    Object log(ProceedingJoinPoint pjp, UUID id, UpdateProductRequest dto) throws Throwable {
        var logBuilder = ProductActions.UPDATE.get();

        for (Class<?> clazz = dto.getClass(); clazz != Object.class; clazz = clazz.getSuperclass()) {
            for (Field field : clazz.getDeclaredFields()) {
                if (Modifier.isStatic(field.getModifiers()) || field.isSynthetic()) continue;

                try {
                    field.setAccessible(true);
                    String fieldName = field.getName();
                    var value = (UpdateFieldRequest<?>) field.get(dto);
                    value.ifDefined(_ -> logBuilder.requestedFieldName(fieldName));
                } catch (IllegalAccessException e) {
                    System.out.println("[UNREACHABLE] Failed to access DTO field: " + field.getName() + ": " + e.getMessage());
                }
            }
        }

        Product product = productRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("[AUDIT FAILED] Product not found for updates. ID: {}", id);
                    return new ProductNotFoundException(id);
                });

        logBuilder.extractOldValues(product);

        product = (Product) pjp.proceed();

        logBuilder.extractNewValues(product);

        ProductLog updateLog = logBuilder.build(authenticationFacade.getCurrentUser(), product);

        productLogRepository.save(updateLog);

        log.info("[AUDIT SUCCESS] Product Update Logged. Product ID: {}. Changes:", id);

        return product;
    }
}
