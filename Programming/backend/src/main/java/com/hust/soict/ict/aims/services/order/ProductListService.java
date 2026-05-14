package com.hust.soict.ict.aims.services.order;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.models.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.ProductStatus;
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

    public List<ProductSummary> get20RandomProducts() {
        return productRepository.find20RandomActiveProducts().stream().map(productMapper::toProductSummary).toList();
    }

    public ProductDetail getProductById(UUID productId) throws ProductNotFoundException {
        return productRepository.findByIdAndStatus(productId, ProductStatus.ACTIVE)
                .map(productMapper::toProductDetail)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }
}
