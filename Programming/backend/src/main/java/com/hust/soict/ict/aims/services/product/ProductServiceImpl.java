package com.hust.soict.ict.aims.services.product;

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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;

    private final ProductMapper productMapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProductSummary> get20RandomProducts() {
        return productRepository.find20RandomActiveProducts(PageRequest.of(0, 20)).stream()
                .map(productMapper::toProductSummary).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProductDetail getProductById(UUID productId) throws ProductNotFoundException {
        Product product = productRepository.findByIdAndStatus(productId, Product.Status.ACTIVE)
                .orElseThrow(() -> new ProductNotFoundException(productId));
        return productMapper.toProductDetail(product);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ProductSummary> getProductsBy(String title, String category, Long minPrice, Long maxPrice, Pageable pageable) {
        String reqTitle = title == null ? null : "%" + title.toLowerCase() + "%";
        String reqCategory = category == null ? null : "%" + category.toLowerCase() + "%";

        return productRepository.searchActiveProductsBy(reqTitle, reqCategory, minPrice, maxPrice, pageable)
                .map(productMapper::toProductSummary);
    }
}
