package com.hust.soict.ict.aims.services.auth;

import com.hust.soict.ict.aims.dto.request.LoginRequest;
import com.hust.soict.ict.aims.dto.response.JwtResponse;

public interface AuthService {
    JwtResponse login(LoginRequest request);
}
