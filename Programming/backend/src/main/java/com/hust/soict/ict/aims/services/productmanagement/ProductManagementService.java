package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.AdjustStockRequest;
import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.dto.response.product.ProductDetail;
import com.hust.soict.ict.aims.dto.response.product.ProductSummary;

import java.util.List;
import java.util.UUID;

public interface ProductManagementService {
    ProductDetail createProduct(CreateProductRequest dto);
    ProductDetail updateProduct(UUID id, UpdateProductRequest dto);
    List<ProductSummary> deleteProducts(List<UUID> productIds);
    void adjustStock(UUID id, AdjustStockRequest adjustStockRequest);
    ProductDetail activateProduct(UUID id);
    ProductDetail deleteProduct(UUID id);
}
