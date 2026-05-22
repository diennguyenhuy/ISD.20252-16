package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.order.ProductCatalogueService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Provide API endpoints for customer homepage and customer product detail page
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductCatalogueController {
    private final ProductCatalogueService productCatalogueService;

    @GetMapping
    public List<ProductSummary> get20RandomProducts() {
        return productCatalogueService.get20RandomProducts();
    }

    @GetMapping
    public List<ProductSummary> filterProductsBy(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) long minPrice,
            @RequestParam(required = false) long maxPrice,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return productCatalogueService.getProductsBy(title, category, minPrice, maxPrice, pageable);
    }

    @GetMapping("/{id}")
    public ProductDetail getProductById(@PathVariable UUID id) throws ProductNotFoundException {
        return productCatalogueService.getProductById(id);
    }
}
