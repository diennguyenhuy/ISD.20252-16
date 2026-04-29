package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.audit.AdminLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminLogRepository extends JpaRepository<AdminLog, UUID> {
}
