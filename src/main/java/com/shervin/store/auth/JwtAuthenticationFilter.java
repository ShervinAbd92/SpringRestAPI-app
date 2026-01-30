package com.shervin.store.auth;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        var autHeader = request.getHeader("Authorization");
        if (autHeader == null || !autHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return; //let spring security handle it, if the target endpoint is protected we get 403 forbidden error
        }
        var token = autHeader.replace("Bearer ", "");
        var jwt = jwtServices.parseToken(token);
        if (jwt == null || jwt.isExpired()) {
            filterChain.doFilter(request, response);
            return;
        }
        //if you get here means u have a valid token, so u can tell spring this user is authenticated and allow access to authenticated resources
        //UsernamePasswordAuthenticationToken(principle, credential, authorities
        var authentication = new UsernamePasswordAuthenticationToken(
                jwt.getUserId(),
            null,
                List.of(new SimpleGrantedAuthority("ROLE_"+ jwt.getRole()))
        );
        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request) //u r attaching some additional metadata about the request like IP address to the authentication obejct
        );

        SecurityContextHolder.getContext().setAuthentication(authentication); //stores information about the correctly authenticated user
        filterChain.doFilter(request, response);

    }
}
