package com.smartattend.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtUtils {

    private static final Logger log = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${smartattend.jwt.secret:404E635266556A586E3272357538782F413F4428472B4B6250645367566B5970}")
    private String jwtSecret;

    @Value("${smartattend.jwt.expiration-ms:86400000}")
    private long jwtExpirationMs;

    private SecretKey key() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            if (keyBytes != null && keyBytes.length >= 32) {
                return Keys.hmacShaKeyFor(keyBytes);
            }
        } catch (Exception ignored) {
        }
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(java.nio.charset.StandardCharsets.UTF_8));
    }

    public String generateJwtToken(Authentication authentication) {
        CustomUserDetails userPrincipal = (CustomUserDetails) authentication.getPrincipal();

        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .claim("id", userPrincipal.getId())
                .claim("email", userPrincipal.getEmail())
                .claim("fullName", userPrincipal.getFullName())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String generateTokenFromUsername(String username) {
        return Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key())
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        if (token == null || token.trim().isEmpty()) {
            return null;
        }
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token.trim())
                .getPayload()
                .getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        if (authToken == null || authToken.trim().isEmpty()) {
            return false;
        }
        try {
            Jwts.parser().verifyWith(key()).build().parseSignedClaims(authToken.trim());
            return true;
        } catch (MalformedJwtException e) {
            log.error("Invalid JWT token: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            log.error("JWT token is expired: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            log.error("JWT token is unsupported: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims string is empty or invalid: {}", e.getMessage());
        } catch (JwtException e) {
            log.error("JWT validation error: {}", e.getMessage());
        }
        return false;
    }
}
