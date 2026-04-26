package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.audit.StockAdjustLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface StockAdjustLogRepository extends JpaRepository<StockAdjustLog, UUID> {
}
