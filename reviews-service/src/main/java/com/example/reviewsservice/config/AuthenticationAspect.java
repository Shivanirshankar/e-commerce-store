package com.example.reviewsservice.config;

import com.example.reviewsservice.controllers.AuthException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Slf4j
@RequiredArgsConstructor
public class AuthenticationAspect {

    private final CustomContextHolder contextHolder;
    private final HttpServletRequest request;

    @Before("@annotation(RequiresAuthentication)")
    public void beforeRequiresAuthentication() {
        if (!this.contextHolder.isAuthenticated()) {
            log.error("{} - {} - {} - {} - {}", this.request.getMethod(), this.request.getRequestURI(), this.contextHolder.getCorrelationId(), this.contextHolder.getUsername(), null);
            throw new AuthException(HttpStatus.UNAUTHORIZED);
        }
    }
}

