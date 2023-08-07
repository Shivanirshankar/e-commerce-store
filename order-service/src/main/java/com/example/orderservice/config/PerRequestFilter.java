package com.example.orderservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class PerRequestFilter extends OncePerRequestFilter {

    private final CustomContextHolder contextHolder;

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final FilterChain filterChain) throws ServletException, IOException {

        var correlationId = request.getHeader("CorrelationId");
        var userId = parseUserId(request);
        var username = request.getHeader("username");

        var context = new CustomContext(correlationId, userId, username);

        contextHolder.set(context);

        try {
            filterChain.doFilter(request, response);
        } finally {
            contextHolder.remove();
        }
    }

    private static Long parseUserId(final HttpServletRequest request) {
        try {
            var userIdString = request.getHeader("userId");
            return Long.parseLong(userIdString);
        } catch (NumberFormatException ignored) {
            return null;
        }
    }
}
