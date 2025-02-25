package org.test.order.infra.dependecy.JWT;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret-key}")
    private String secretKey;

    // Injetando o tempo de expiração em minutos
    @Value("${jwt.expiration-time}")
    private long expirationTimeInMinutes;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken() {
        long expirationTimeInMillis = expirationTimeInMinutes * 1000 * 60;

        return Jwts.builder()
                .setSubject("admin")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTimeInMillis))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateToken(String token) {
        try {
            getAllClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            System.out.println("Token inválido: " + e.getMessage());
            return false;
        }
    }

    private Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            throw new IllegalArgumentException("Token inválido ou com formato incorreto", e);
        }
    }

    public boolean validateAuthorizationHeader(String authorizationHeader) {
        if (!authorizationHeader.startsWith("Bearer ")) {
            throw new SecurityException("Invalid Authorization header");
        }
        String apiKey = authorizationHeader.substring(7);
        return secretKey.equals(apiKey);
    }
}
