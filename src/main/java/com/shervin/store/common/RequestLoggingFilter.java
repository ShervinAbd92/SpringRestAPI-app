package com.shervin.store.common;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);
    private static final String REQUEST_ID = "requestId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        String requestId = UUID.randomUUID().toString();
        try {
            // Put requestId into MDC (picked up by Logback JSON)
            MDC.put(REQUEST_ID, requestId);

            // Also expose to clients
            response.setHeader(REQUEST_ID, requestId);
            log.debug("Incoming request method={} path={}",
                    request.getMethod(),
                    request.getRequestURI());

            filterChain.doFilter(request, response); //here the security hain runs, invoking jwtAuthenticationFilter at the point your inserted it

            log.debug("Outgoing response status={}", response.getStatus());
        } finally {
            MDC.remove(REQUEST_ID); //after the request finishes control returns to here and removes MDC
        }
    }
}
