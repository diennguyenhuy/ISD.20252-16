package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.order.ProductListService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Provide API endpoints for customer homepage and customer product detail page
 */
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductListController {
    private final ProductListService productListService;

    @GetMapping
    public @ResponseBody List<ProductSummary> getAllProducts() {
        return productListService.getAllProducts();
    }

    @GetMapping("/{id}")
    public @ResponseBody ProductDetail getProductById(@PathVariable UUID id) {
        return productListService.getProductById(id);
    }
}
