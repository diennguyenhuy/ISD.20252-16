package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.dto.request.CreateProductRequest;
import com.hust.soict.ict.aims.dto.request.UpdateProductRequest;
import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;
import java.util.UUID;

public interface ProductManagementService {
    Product createProduct(CreateProductRequest dto);
    Product updateProduct(UUID id, UpdateProductRequest dto);
    List<Product> deleteProducts(List<UUID> productIds);
    Product adjustStock(UUID id, int delta, String reason);
    Product activateProduct(UUID id);
    Product deleteProduct(UUID id);
}
