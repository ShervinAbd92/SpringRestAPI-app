package com.shervin.store.auth;
import com.shervin.store.users.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.crypto.SecretKey;
import java.util.Date;

//all token related behavior

@AllArgsConstructor
@Data
public class Jwt {

    private final Claims claims;
    private final SecretKey secretKey;

    public boolean isExpired() {
        return claims.getExpiration().before(new Date());
    }
    public Role getRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public String toString(){
        return Jwts.builder().claims(claims).signWith(secretKey).compact();
    }

}
