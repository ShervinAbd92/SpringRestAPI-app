package com.shervin.store.auth;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtServices jwtServices;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);
    private static final String MDC_USER = "userId";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        var autHeader = request.getHeader("Authorization");
        boolean authHeaderPresent = autHeader != null && autHeader.startsWith("Bearer ");

        log.debug("JwtFilter: method={} path={} authHeaderPresent={}",
                request.getMethod(), request.getRequestURI(), authHeaderPresent);

        if (!authHeaderPresent) {
            filterChain.doFilter(request, response);
            return; //let spring security handle it, if the target endpoint is protected we get 403 forbidden error
        }
        var token = autHeader.replace("Bearer ", "");
        try{
            var jwt = jwtServices.parseToken(token);
            if (jwt == null || jwt.isExpired()) {
                log.warn("JwtFilter: invalid/Expired token for path {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }
            //if you get here means u have a valid token, so u can tell spring this user is authenticated and allow access to authenticated resources
            //UsernamePasswordAuthenticationToken(principle, credential, authorities
            //successful validation -> set Authentication
            var authentication = new UsernamePasswordAuthenticationToken(
                    jwt.getUserId(),
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_"+ jwt.getRole()))
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request)); //u r attaching some additional metadata about the request like IP address to the authentication obejct);
            SecurityContextHolder.getContext().setAuthentication(authentication); //stores information about the correctly authenticated user

            //add userid/email to MDC so subsequent logs include it
            MDC.put(MDC_USER, jwt.getUserId().toString());
            // INFO: authenticated user and roles (no token)
            log.info("JwtFilter: authenticated userId={} roles={}", jwt.getUserId(), jwt.getRole());
            filterChain.doFilter(request, response);

        } catch (ExpiredJwtException eje) {
            // WARN: token expired
            log.warn("JwtFilter: token expired for path={} message={}", request.getRequestURI(), eje.getMessage());
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            // ERROR: unexpected exception during token parsing/validation
            log.error("JwtFilter: unexpected error for path={}", request.getRequestURI(), ex);
            SecurityContextHolder.clearContext();
            // rethrow or let the exception handler handle it - here we rethrow to be consistent with default behavior
            throw ex;
        } finally {
            // DO NOT remove MDC userId here — RequestLoggingFilter clears MDC after the request completes.
        }


    }
}
