package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Override
    @EntityGraph(value = "Order-aggregate")
    Optional<Order> findById(@NonNull UUID uuid);

    @Override
    @EntityGraph(value = "Order-summary")
    Page<Order> findAll(Pageable pageable);

    @EntityGraph(value = "Order-summary")
    Page<Order> findAllByStatus(Order.Status status, Pageable pageable);

    @EntityGraph(value = "Order-summary")
    Page<Order> findByStatusNot(Order.Status status, Pageable pageable);

    Page<Order> findAllByStatusNot(Order.Status status, Pageable pageable);
}
