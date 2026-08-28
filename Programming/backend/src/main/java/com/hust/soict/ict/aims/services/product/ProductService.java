package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ProductService {
    List<Product> get20RandomProducts();
    Product getProductById(UUID productId) throws ProductNotFoundException;
    Page<Product> getProductsBy(String title, String category, Long minPrice, Long maxPrice, Pageable pageable);
}
