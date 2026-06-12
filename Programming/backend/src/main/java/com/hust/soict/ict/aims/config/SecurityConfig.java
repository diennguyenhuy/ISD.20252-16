package com.hust.soict.ict.aims.config;

import com.hust.soict.ict.aims.security.jwt.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor // Tự động inject JwtAuthFilter
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // --- BỔ SUNG: Cấu hình mã hóa mật khẩu ---
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // --- BỔ SUNG: Cấu hình quản lý xác thực ---
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) {
        try {
            return authConfig.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            http.cors(Customizer.withDefaults()) // Giữ nguyên cấu hình CORS của nhóm
                    .csrf(AbstractHttpConfigurer::disable)
                    // BỔ SUNG: Chuyển quản lý Session sang dạng STATELESS (Không lưu phiên, dùng hoàn toàn Token)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(auth ->
                            auth.requestMatchers(HttpMethod.OPTIONS).permitAll()
                                    // 1. CÁC ENDPOINT CŨ CỦA NHÓM (Giữ nguyên)
                                    .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                                    .requestMatchers("/product/**", "/products/**", "/cart/**", "/order/**").permitAll()
                                    .requestMatchers("/vqr/**").permitAll()

                                    // 2. CÁC ENDPOINT MỚI BỔ SUNG
                                    .requestMatchers("/api/auth/**").permitAll() // Mở cửa cho Login/Register
                                    .requestMatchers("/manager/**").authenticated() // Manager bắt buộc phải có Token

                                    // 3. THAY ĐỔI QUAN TRỌNG: Đổi từ permitAll() sang authenticated()
                                    .anyRequest().authenticated()
                    );

            // BỔ SUNG: Thêm bộ lọc JWT đứng gác ở cửa
            http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

            return http.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}