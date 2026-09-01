package com.hust.soict.ict.aims.services.productmanagement.log;

import com.hust.soict.ict.aims.models.entities.product.Product;

public record ProductStockAdjustEvent(Product product, int oldStock, int newStock, String reason) {}
