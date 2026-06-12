package com.hust.soict.ict.aims.controllers.customer;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.services.customer.ProductCatalogueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    /**
     * GET /products/random
     * <br>Fetch 20 random products
     * @return the list of summaries of the products
     */
    @Operation(summary = "Get 20 random products")
    @GetMapping("/random")
    public List<ProductSummary> get20RandomProducts() {
        return productCatalogueService.get20RandomProducts();
    }

    /**
     * GET /products?page={?}&title={?}&category={?}&minPrice={?}&maxPrice={?}
     * <br>Filter products by title, category, and price range. Also support pagination with page
     * @param title title to search for
     * @param category category to search for
     * @param minPrice minimum price
     * @param maxPrice maximum price
     * @param pageable page number
     * @return the list of summaries of the products
     */
    @Operation(summary = "Filter products by title, category, min price, max price")
    @GetMapping
    public Page<ProductSummary> filterProductsBy(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Long minPrice,
            @RequestParam(required = false) Long maxPrice,
            @PageableDefault(size = 20) Pageable pageable
    ) {
        return productCatalogueService.getProductsBy(title, category, minPrice, maxPrice, pageable);
    }

    /**
     * GET products/{id}
     * <br>Return the details of the product
     * @param id the product ID to request
     * @return the details of the product
     * @throws ProductNotFoundException if product does not exist
     */
    @Operation(summary = "Get product by ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Product found"),
            @ApiResponse(responseCode = "404", description = "Product not found")
    })
    @GetMapping("/{id}")
    public ProductDetail getProductById(@PathVariable UUID id) throws ProductNotFoundException {
        return productCatalogueService.getProductById(id);
    }
}
