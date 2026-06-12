package com.hust.soict.ict.aims.services.customer;

import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import com.hust.soict.ict.aims.dto.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCatalogueService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    public List<ProductSummary> get20RandomProducts() {
        return productRepository.find20RandomActiveProducts(PageRequest.of(0, 20)).stream()
                .map(productMapper::toProductSummary).toList();
    }

    public ProductDetail getProductById(UUID productId) throws ProductNotFoundException {
        return productRepository.findByIdAndStatus(productId, Product.Status.ACTIVE)
                .map(productMapper::toProductDetail)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    public Page<ProductSummary> getProductsBy(String title, String category, Long minPrice, Long maxPrice, Pageable pageable) {
        String reqTitle = title == null ? null : "%" + title.toLowerCase() + "%";
        String reqCategory = category == null ? null : "%" + category.toLowerCase() + "%";

        return productRepository.searchActiveProductsBy(reqTitle, reqCategory, minPrice, maxPrice, pageable)
                .map(productMapper::toProductSummary);
    }
}
