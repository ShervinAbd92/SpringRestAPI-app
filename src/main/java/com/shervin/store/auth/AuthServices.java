package com.shervin.store.auth;

import com.shervin.store.users.User;
import com.shervin.store.users.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServices {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtServices jwtServices;

    public User getCurrentUser() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userId  = (Long)authentication.getPrincipal();

        return userRepository.findById(userId).orElse(null);
    }

    public LoginResponse login(LoginRequestDto request){

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword())
        );

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        //find the request user id, pass it as well as name
        var accessToken = jwtServices.generateAccessToken(user);
        var refreshToken = jwtServices.generateRefreshToken(user);

        return new LoginResponse(accessToken, refreshToken);
    }


    public Jwt refreshAccessToken(String refreshToken) {

        //validate your token first
        var jwt = jwtServices.parseToken(refreshToken);
        if (jwt == null || jwt.isExpired()){
            throw new BadCredentialsException("you have provided invalid refreshtoken");
        }
        var user = userRepository.findById(jwt.getUserId()).orElseThrow();
        return jwtServices.generateAccessToken(user);
    }
}
