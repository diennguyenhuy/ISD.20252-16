package com.hust.soict.ict.aims.subsystems.vietqr.synccallback;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
class VietQRCallbackAuthService {

    @Value("${vietqr.expectedusername}")
    private String EXPECTED_USERNAME;
    @Value("${vietqr.expectedpassword}")
    private String EXPECTED_PASSWORD;

    private static final String BASIC_PREFIX  = "Basic ";
    private static final String BEARER_PREFIX = "Bearer ";

    public static final int TOKEN_TTL_SECONDS = 300;
    private final Map<String, Instant> tokenStore = new ConcurrentHashMap<>();

    public String authenticateFromHeader(String authorizationHeader) {
        String[] credentials = parseBasicAuthHeader(authorizationHeader);
        if (credentials == null) {
            log.warn("[VietQR-Auth] Missing or malformed Basic Auth header");
            return null;
        }
        return authenticate(credentials[0], credentials[1]);
    }
    public String extractBearerToken(String authorizationHeader) {
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            return null;
        }
        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();
        return token.isEmpty() ? null : token;
    }

    private String authenticate(String username, String password) {
        if (!EXPECTED_USERNAME.equals(username) || !EXPECTED_PASSWORD.equals(password)) {
            log.warn("[VietQR-Auth] Invalid credentials for username='{}'", username);
            return null;
        }
        String token = UUID.randomUUID().toString();
        Instant expiry = Instant.now().plusSeconds(TOKEN_TTL_SECONDS);
        tokenStore.put(token, expiry);
        log.info("[VietQR-Auth] Issued token={} expires={}", token, expiry);
        return token;
    }

    public boolean isValid(String token) {
        if (token == null) return false;
        Instant expiry = tokenStore.get(token);
        if (expiry == null) return false;
        if (Instant.now().isAfter(expiry)) {
            tokenStore.remove(token);
            log.debug("[VietQR-Auth] Token expired and removed: {}", token);
            return false;
        }
        return true;
    }


    private static String[] parseBasicAuthHeader(String header) {
        if (header == null || !header.startsWith(BASIC_PREFIX)) return null;
        try {
            String encoded = header.substring(BASIC_PREFIX.length()).trim();
            String decoded = new String(Base64.getDecoder().decode(encoded), StandardCharsets.UTF_8);
            int colonIndex = decoded.indexOf(':');
            if (colonIndex < 0) return null;
            return new String[]{
                    decoded.substring(0, colonIndex),
                    decoded.substring(colonIndex + 1)
            };
        } catch (IllegalArgumentException e) {
            log.warn("[VietQR-Auth] Basic Auth header contains invalid Base64");
            return null;
        }
    }
}

