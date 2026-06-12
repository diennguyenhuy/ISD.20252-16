package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.request.AdjustStockRequest;
import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.productmanagement.ProductManagementService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Cohesion: Communicational Cohesion<br>
 * Reason: Groups all REST API endpoint routing and HTTP mapping logic related to the same business domain (Product Management).<br>
 * Coupling:
 * - Data coupling with ProductManagementService when passing primitive path variables like UUID.
 * - Stamp coupling with the service layer by passing composite request DTOs (CreateProductRequest, UpdateProductRequest) and returning ProductDetail responses.
 * Design Strength:
 * Maintains an extremely thin architecture focused exclusively on HTTP protocol translation and validation routing, delegating all business operations to the service layer.
 */

@RestController
@RequestMapping("/manager/products")
@RequiredArgsConstructor
@Validated
public class ProductManagementController {

    private final ProductManagementService productManagementService;

    @GetMapping
    public List<ProductSummary> getAllProducts() {
        return productManagementService.getAllProductsForManager();
    }

    @GetMapping("/{id}")
    public ProductDetail getProductById(@PathVariable UUID id) {
        return productManagementService.getProductByIdForManager(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetail createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productManagementService.createProduct(request);
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductDetail updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request
    ) {
        return productManagementService.updateProduct(id, request);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ProductSummary> deleteProducts(
            @Size(min = 1, max = 10, message = "List of product IDs to delete must contain at least 1 product and a maximum of 10 products at a time")
            List<@NotNull(message = "Each ID must not be null") UUID> request
    ) {
        return productManagementService.deleteProducts(request);
    }

    @PostMapping("/{id}/stock")
    public void adjustStock(@PathVariable UUID id, @Valid @RequestBody AdjustStockRequest request) {
        productManagementService.adjustStock(id, request);
    }

    @PostMapping("/{id}/activate")
    public ProductDetail activateProduct(@PathVariable UUID id) {
        return productManagementService.activateProduct(id);
    }

    @DeleteMapping("/{id}")
    public ProductDetail deleteProduct(@PathVariable UUID id) {
        return productManagementService.deleteProduct(id);
    }
}
