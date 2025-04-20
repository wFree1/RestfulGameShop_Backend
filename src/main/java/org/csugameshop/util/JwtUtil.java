package org.csugameshop.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret:secureSecretKey1234567890abcdef1234567890abcdef1234567890}")
    private String SECRET_KEY;

    @Value("${jwt.expiration:86400000}")
    private long EXPIRATION_TIME;

    private SecretKey KEY;

    @PostConstruct
    public void init() {
        KEY = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String generateToken(int userId) {
        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(KEY, SignatureAlgorithm.HS512)
                .compact();
    }

    public String extractSubject(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage());
        }
    }
    public Claims validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            return claims;
        } catch (Exception e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage());
        }
    }
    public boolean validateToken(String token, String username) {
        try {
            Claims claims = validateToken(token);
            return (claims.getSubject().equals(username) && !isTokenExpired(claims));
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage());
        }
    }
    private boolean isTokenExpired(Claims claims) {
        return claims.getExpiration().before(new Date());
    }

    // 提供获取 userId 的方法，方便控制器使用
    public int getUserIdFromToken(String token) {
        String subject = extractSubject(token);
        try {
            return Integer.parseInt(subject);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Invalid userId in JWT: " + subject);
        }
    }

    public String extractUsername(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(KEY)
                    .build()
                    .parseClaimsJws(jwt)
                    .getBody();
            return claims.getSubject();
        } catch (JwtException e) {
            throw new RuntimeException("Invalid JWT: " + e.getMessage());
        }
    }
}