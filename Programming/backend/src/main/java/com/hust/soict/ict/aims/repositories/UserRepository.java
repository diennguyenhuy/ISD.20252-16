package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
}
