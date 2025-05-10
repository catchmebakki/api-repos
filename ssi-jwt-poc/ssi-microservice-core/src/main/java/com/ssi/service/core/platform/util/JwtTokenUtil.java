package com.ssi.service.core.platform.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.Serializable;
import java.util.Date;

@Component
public class JwtTokenUtil implements Serializable {
    private final long jwTExpiryInMin =  20 * 60 * 1000;

    //secrectString123456789012345678901234567890
    private final
    SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64
            .decode("c2VjcmVjdFN0cmluZzEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MA=="));

    public Jws<Claims> getAllClaimsFromToken(final String token) {

        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

    public String createJWT() {

        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("Default")
                .claim("scopes", "profile")
                .signWith(key)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwTExpiryInMin))
                .compact();
    }


}