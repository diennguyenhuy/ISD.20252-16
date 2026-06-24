package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;

import java.util.List;
import java.util.UUID;

public interface ProductQueryService {
    List<ProductSummary> getAllProductsForManager();
    ProductDetail getProductByIdForManager(UUID id);
}
