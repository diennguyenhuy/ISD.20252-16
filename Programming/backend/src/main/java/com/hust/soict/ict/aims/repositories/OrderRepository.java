package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

}
