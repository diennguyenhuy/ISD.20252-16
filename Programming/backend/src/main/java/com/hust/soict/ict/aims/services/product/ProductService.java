package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<ProductSummary> get20RandomProducts();
    ProductDetail getProductById(UUID productId) throws ProductNotFoundException;
    Page<ProductSummary> getProductsBy(String title, String category, Long minPrice, Long maxPrice, Pageable pageable);
}
