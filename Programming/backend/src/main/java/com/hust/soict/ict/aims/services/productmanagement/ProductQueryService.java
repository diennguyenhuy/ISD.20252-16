package com.hust.soict.ict.aims.services.productmanagement;

import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;
import java.util.UUID;

public interface ProductQueryService {
    List<Product> getAllProductsForManager();
    Product getProductByIdForManager(UUID id);
}
