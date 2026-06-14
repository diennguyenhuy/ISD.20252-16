package com.hust.soict.ict.aims.repositories;

import com.hust.soict.ict.aims.models.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Tìm User theo email để xác thực lúc Login
    Optional<User> findByEmail(String email);

    // Tìm User theo username (phòng trường hợp bạn muốn cho phép login bằng cả 2)
    Optional<User> findByUsername(String username);

    // Kiểm tra trùng lặp dữ liệu lúc Register
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}