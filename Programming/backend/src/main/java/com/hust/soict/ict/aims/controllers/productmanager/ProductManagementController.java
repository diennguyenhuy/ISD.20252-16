package com.hust.soict.ict.aims.controllers.productmanager;

import com.hust.soict.ict.aims.dto.request.AdjustStockRequest;
import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductMappers;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.productmanagement.ProductManagementService;
import com.hust.soict.ict.aims.services.productmanagement.ProductQueryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/manager/products")
@RequiredArgsConstructor
@Validated
public class ProductManagementController {

    private final ProductManagementService productManagementService;
    private final ProductQueryService productQueryService;
    private final ProductMappers productMapper;

    @GetMapping
    public List<ProductSummary> getAllProducts() {
        return productQueryService.getAllProductsForManager().stream()
                .map(productMapper::mapSummary)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductDetail getProductById(@PathVariable UUID id) {
        return productMapper.map(productQueryService.getProductByIdForManager(id));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductDetail createProduct(@Valid @RequestBody CreateProductRequest request) {
        return productMapper.map(productManagementService.createProduct(request));
    }

    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductDetail updateProduct(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateProductRequest request
    ) {
        return productMapper.map(productManagementService.updateProduct(id, request));
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<ProductSummary> deleteProducts(
            @Valid @RequestBody
            @Size(
                    min = 1,
                    max = 10,
                    message = "List of product IDs to delete must contain at least 1 product and a maximum of 10 products at a time"
            )
            List<@NotNull(message = "Each ID must not be null") UUID> productIds
    ) {
        return productManagementService.deleteProducts(productIds).stream()
                .map(productMapper::mapSummary)
                .toList();
    }

    @PostMapping("/{id}/stock")
    public ProductDetail adjustStock(
            @PathVariable UUID id,
            @Valid @RequestBody AdjustStockRequest request
    ) {
        return productMapper.map(productManagementService.adjustStock(id, request.delta(), request.reason()));
    }

    @PostMapping("/{id}/activate")
    public ProductDetail activateProduct(@PathVariable UUID id) {
        return productMapper.map(productManagementService.activateProduct(id));
    }

    @DeleteMapping("/{id}")
    public ProductDetail deleteProduct(@PathVariable UUID id) {
        return productMapper.map(productManagementService.deleteProduct(id));
    }
}
