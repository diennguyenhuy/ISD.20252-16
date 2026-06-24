package com.hust.soict.ict.aims.services.admin;

import com.hust.soict.ict.aims.dto.response.UserResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface UserQueryService {
    Page<UserResponse> getUsers(Pageable pageable);
    UserResponse getUser(UUID userId);
}
