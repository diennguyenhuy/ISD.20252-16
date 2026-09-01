package com.hust.soict.ict.aims.services.productmanagement.log;

import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;

public record ProductsDeletedEvent(List<Product> products) {
}
