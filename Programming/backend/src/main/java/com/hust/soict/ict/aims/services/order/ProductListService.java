package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductListService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public List<ProductSummary> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toProductSummary).toList();
    }

    public ProductDetail getProductById(UUID productId) throws ProductNotFoundException {
        return productRepository.findById(productId)
                .map(productMapper::toProductDetail)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
