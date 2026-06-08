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

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetail createProduct(@RequestBody @Valid CreateProductRequest request) {
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
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducts(
            @NotEmpty(message = "List of product IDs to delete cannot be empty")
            List<@NotNull(message = "Each ID must not be null") UUID> request
    ) {
        productManagementService.deleteProducts(request);
    }

    @GetMapping
    public ResponseEntity<List<ProductSummary>> getAllProducts() {
        return ResponseEntity.ok(productManagementService.getAllProductsForManager());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDetail> getProductById(@PathVariable UUID id) {
        return ResponseEntity.ok(productManagementService.getProductByIdForManager(id));
    }

    @PostMapping("/{id}/stock")
    public void adjustStock(@PathVariable UUID id, @RequestBody @Valid AdjustStockRequest request) {
        productManagementService.adjustStock(id, request.getDelta(), request.getReason());
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
