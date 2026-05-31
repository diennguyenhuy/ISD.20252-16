package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findAllByStatus(Product.Status status);

    List<Product> findAllByIdInAndStatus(Collection<UUID> ids, Product.Status status);

    @Query("SELECT p FROM Product p WHERE p.status = ACTIVE ORDER BY random()")
    List<Product> find20RandomActiveProducts(Pageable pageable);

    Optional<Product> findByIdAndStatus(UUID id, Product.Status status);

    boolean existsByBarcode(String barcode);
    long countByStatusIn(Collection<Product.Status> statuses);
    long countByStatusInAndUpdatedAtAfter(List<Product.Status> statuses, java.time.Instant startOfDay);
  
    @Query("SELECT p FROM Product p WHERE " +
    "(:title IS NULL OR LOWER(p.title) LIKE :title) AND " +
    "(:category IS NULL OR LOWER(p.category) LIKE :category) AND " +
    "(:minPrice IS NULL OR p.currentPrice >= :minPrice) AND " +
    "(:maxPrice IS NULL OR p.currentPrice <= :maxPrice) AND " +
    "p.status = ACTIVE")
    List<Product> searchActiveProductsBy(
            @Param("title") String title,
            @Param("category") String category,
            @Param("minPrice") Long minPrice,
            @Param("maxPrice") Long maxPrice,
            Pageable pageable
    );
}
