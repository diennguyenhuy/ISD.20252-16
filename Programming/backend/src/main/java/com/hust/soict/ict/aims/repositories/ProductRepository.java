package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.product.Product;
import com.hust.soict.ict.aims.models.entities.product.ProductStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByStatus(ProductStatus status);

    List<Product> findAllByIdInAndStatus(Collection<UUID> ids, ProductStatus status);

    @Query(nativeQuery = true, value = "SELECT * FROM product WHERE status = 'ACTIVE' ORDER BY RANDOM() LIMIT 20")
    List<Product> find20RandomActiveProducts();

    Optional<Product> findByIdAndStatus(UUID id, ProductStatus status);

    boolean existsByBarcode(String barcode);
    long countByStatusIn(Collection<ProductStatus> statuses);
}
