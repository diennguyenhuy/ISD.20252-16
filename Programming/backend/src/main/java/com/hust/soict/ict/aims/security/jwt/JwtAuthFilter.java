package com.hust.soict.ict.aims.security.jwt;

import com.hust.soict.ict.aims.security.services.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * Paths that a user with must_change_password=true IS allowed to access.
     * Everything else is blocked until they change their password.
     */
    private static final Set<String> PASSWORD_CHANGE_WHITELIST = Set.of(
            "/profile/password",
            "/auth/login"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null && jwtUtils.validateJwtToken(jwt)) {
                Claims claims = jwtUtils.getClaimsFromJwtToken(jwt);
                String email = claims.getSubject();

                // --- Blocked account check (reads claim, no DB hit) ---
                Boolean isBlocked = claims.get(JwtUtils.CLAIM_BLOCKED, Boolean.class);
                if (Boolean.TRUE.equals(isBlocked)) {
                    writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                            "Your account has been blocked. Please contact an administrator.");
                    return; // Short-circuit — do NOT continue the filter chain
                }

                // --- Must-change-password check (reads claim, no DB hit) ---
                Boolean mustChangePassword = claims.get(JwtUtils.CLAIM_MUST_CHANGE_PASSWORD, Boolean.class);
                String requestPath = request.getRequestURI();
                if (Boolean.TRUE.equals(mustChangePassword) && !isWhitelistedPath(requestPath)) {
                    writeErrorResponse(response, HttpServletResponse.SC_FORBIDDEN,
                            "You must change your password before performing any other action.");
                    return; // Short-circuit
                }

                // --- Standard authentication flow ---
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Cấp thẻ đi qua cửa cho Request này
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception e) {
            log.error("Cannot set user authentication: {}", e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    private String parseJwt(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (StringUtils.hasText(headerAuth) && headerAuth.startsWith("Bearer ")) {
            return headerAuth.substring(7); // Bỏ chữ "Bearer " để lấy mỗi token
        }
        return null;
    }

    /**
     * Writes a JSON error response directly — no ObjectMapper needed for a simple message.
     */
    private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        // Simple JSON — no need for a serialization library
        String json = "{\"message\":\"" + message.replace("\"", "\\\"") + "\"}";
        response.getOutputStream().write(json.getBytes(StandardCharsets.UTF_8));
        response.getOutputStream().flush();
    }

    /**
     * Checks if the request path is allowed even when must_change_password is true.
     */
    private boolean isWhitelistedPath(String requestPath) {
        return PASSWORD_CHANGE_WHITELIST.stream().anyMatch(requestPath::startsWith);
    }
}