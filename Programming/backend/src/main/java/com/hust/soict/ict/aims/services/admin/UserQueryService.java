package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.models.entities.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserQueryService {
    Page<User> getUsers(Pageable pageable);
    User getUser(UUID userId);
}
