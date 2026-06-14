package com.hust.soict.ict.aims.security.jwt;

import com.hust.soict.ict.aims.security.services.UserDetailsImpl;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
@Slf4j
public class JwtUtils {

    /** Custom JWT claim key for the "account is blocked" flag. */
    public static final String CLAIM_BLOCKED = "is_blocked";

    /** Custom JWT claim key for the "must change password" flag. */
    public static final String CLAIM_MUST_CHANGE_PASSWORD = "must_change_password";

    @Value("${aims.app.jwtSecret}")
    private String jwtSecret;

    @Value("${aims.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    // 1. Tạo JWT khi Login thành công — now embeds account-status claims
    public String generateJwtToken(Authentication authentication) {
        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject(userPrincipal.getEmail()) // Dùng email làm định danh chính
                .claim(CLAIM_BLOCKED, userPrincipal.isBlocked())
                .claim(CLAIM_MUST_CHANGE_PASSWORD, userPrincipal.isMustChangePassword())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Lấy chuỗi Secret Key để ký mã
    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
    }

    // 2. Lấy Email từ trong JWT
    public String getEmailFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody().getSubject();
    }

    // 3. Parse all claims from a validated JWT
    public Claims getClaimsFromJwtToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key()).build()
                .parseClaimsJws(token).getBody();
    }

    // 4. Kiểm tra xem JWT có hợp lệ không
    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(authToken);
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty: {}", e.getMessage());
        }
        return false;
    }
}