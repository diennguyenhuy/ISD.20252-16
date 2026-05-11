package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.jspecify.annotations.NonNull;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    @Override
    @EntityGraph(value = "Order-aggregate")
    Optional<Order> findById(@NonNull UUID uuid);

    @Override
    @EntityGraph(value = "Order-summary")
    List<Order> findAll();
}
