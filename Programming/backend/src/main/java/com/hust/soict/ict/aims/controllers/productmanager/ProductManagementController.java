package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.models.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.dto.request.DeleteProductRequest;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.services.productmanagement.ProductManagementService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
@RequestMapping("/api/v1/manager/products")
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
}