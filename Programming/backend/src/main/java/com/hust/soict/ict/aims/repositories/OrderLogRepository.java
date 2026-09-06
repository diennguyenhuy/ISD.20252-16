package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.audit.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderLogRepository extends JpaRepository<OrderLog, UUID> {
    List<OrderLog> findByOrderId(UUID orderId);
}
