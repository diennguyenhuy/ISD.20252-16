package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AdminLogRepository extends JpaRepository<AdminLog, UUID> {
}
