package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {

}
