package com.sccon.geo.pessoa.adapter.in.web.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private final Map<String, SimpleRateLimiter> limiters = new ConcurrentHashMap<>();

    private SimpleRateLimiter newLimiter() {
        return new SimpleRateLimiter(10, 10, 60_000); // 10 req/min
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String key = resolveKey(request);

        SimpleRateLimiter limiter =
                limiters.computeIfAbsent(key, k -> newLimiter());

        if (!limiter.tryConsume()) {
            response.setStatus(429);
            response.setContentType("application/json");
            response.getWriter().write("""
                {
                  "title": "Too Many Requests",
                  "status": 429,
                  "detail": "Rate limit exceeded"
                }
            """);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private String resolveKey(HttpServletRequest request) {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null && auth.isAuthenticated()) {
            return auth.getName(); // per-user
        }

        return request.getRemoteAddr(); // fallback
    }
}