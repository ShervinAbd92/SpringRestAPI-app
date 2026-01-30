package com.shervin.store.auth;
import com.shervin.store.users.UserDto;
import com.shervin.store.users.UserMapper;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthServices authServices;
    private final JwtConfig jwtConfig;
    private final UserMapper userMapper;


    @PostMapping("/login")
    public JwtResponseDto login(
            @Valid  @RequestBody LoginRequestDto request,
            HttpServletResponse response){

        var loginResponse = authServices.login(request);

        var refreshToken = loginResponse.getRefreshToken();
        var cookie = new Cookie("refreshToken", refreshToken.toString());
        cookie.setHttpOnly(true);
        cookie.setPath("/auth/refresh");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);

        return new JwtResponseDto(loginResponse.getAccessToken().toString());
    }

    @PostMapping("/refresh")
    public JwtResponseDto refresh(
            @CookieValue(value = "refreshToken") String refreshToken){

        return  new JwtResponseDto(authServices.refreshAccessToken(refreshToken).toString());
    }
    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var user = authServices.getCurrentUser();
        if (user == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(userMapper.toDto(user));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> HandleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
