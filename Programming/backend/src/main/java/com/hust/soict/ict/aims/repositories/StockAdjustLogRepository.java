package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StockAdjustLogRepository extends JpaRepository<StockAdjustLog, UUID> {
}
