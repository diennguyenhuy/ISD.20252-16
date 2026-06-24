package com.hust.soict.ict.aims.services.auth;

import com.hust.soict.ict.aims.dto.request.ChangePasswordRequest;

import java.util.UUID;

public interface ProfileService {
    void changePassword(UUID userId, ChangePasswordRequest request);
}
