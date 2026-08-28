package com.hust.soict.ict.aims.services.product;

import com.hust.soict.ict.aims.dto.response.product.ProductMappers;
import com.hust.soict.ict.aims.exceptions.ProductNotFoundException;
import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Product> get20RandomProducts() {
        return productRepository.find20RandomActiveProducts(PageRequest.of(0, 20));
    }

    @Override
    @Transactional(readOnly = true)
    public Product getProductById(UUID productId) throws ProductNotFoundException {
        return productRepository.findByIdAndStatus(productId, Product.Status.ACTIVE)
                .orElseThrow(() -> new ProductNotFoundException(productId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Product> getProductsBy(String title, String category, Long minPrice, Long maxPrice, Pageable pageable) {
        String reqTitle = title == null ? null : "%" + title.toLowerCase() + "%";
        String reqCategory = category == null ? null : "%" + category.toLowerCase() + "%";

        return productRepository.searchActiveProductsBy(reqTitle, reqCategory, minPrice, maxPrice, pageable);
    }
}
