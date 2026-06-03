package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.dto.request.DeleteProductRequest;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.productmanagement.ProductManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("manager/products")
@RequiredArgsConstructor
public class ProductManagementController {

    private final ProductManagementService productManagementService;

    @PostMapping
    public ResponseEntity<ProductDetail> createProduct(@RequestBody @Valid CreateProductRequest request) {
        ProductDetail createdProduct = productManagementService.createProduct(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdProduct);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDetail> updateProduct(
            @PathVariable UUID id,
            @RequestBody @Valid UpdateProductRequest request) {
        ProductDetail updatedProduct = productManagementService.updateProduct(id, request);
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProducts(@RequestBody @Valid DeleteProductRequest request) {
        productManagementService.deleteProducts(request.getProductIds());
        return ResponseEntity.noContent().build();
    }
    public static class AdjustStockRequest {
        public int delta;
        public String reason;
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
    public ResponseEntity<Void> adjustStock(@PathVariable UUID id, @RequestBody AdjustStockRequest request) {
        productManagementService.adjustStock(id, request.delta, request.reason);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activateProduct(@PathVariable UUID id) {
        productManagementService.activateProduct(id);
        return ResponseEntity.ok().build();
    }
}