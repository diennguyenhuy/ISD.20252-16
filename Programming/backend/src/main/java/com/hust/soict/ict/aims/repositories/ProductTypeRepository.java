package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.product.Product;

import java.util.List;

public interface ProductTypeRepository {
    long countByType(Class<? extends Product> subclass);
    <T extends Product> List<T> getByType(Class<T> subclass);
}
