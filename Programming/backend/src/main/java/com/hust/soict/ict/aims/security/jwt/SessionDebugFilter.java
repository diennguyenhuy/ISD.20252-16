package com.hust.soict.ict.aims.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collection;

@Slf4j
//@Order(Ordered.HIGHEST_PRECEDENCE)
//@Component
public class SessionDebugFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        HttpSession before = request.getSession(false);

        log.info("BEFORE {} session={}",
                request.getRequestURI(),
                before == null ? "null" : before.getId());

        filterChain.doFilter(request, response);

        HttpSession after = request.getSession(false);

        log.info("AFTER {} session={}",
                request.getRequestURI(),
                after == null ? "null" : after.getId());

        Collection<String> cookies = response.getHeaders("Set-Cookie");
        cookies.forEach(c -> log.info("SET-COOKIE {}", c));

        log.info(
                "{} {} Cookie={}",
                request.getMethod(),
                request.getRequestURI(),
                request.getHeader("Cookie")
        );
    }
}
