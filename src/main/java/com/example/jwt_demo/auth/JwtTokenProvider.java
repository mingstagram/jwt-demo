package com.example.jwt_demo.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {

    private final String SECRET_KEY = "MyJwtSecretKey12345678901234567890!";
    private final long ACCESS_EXPIRATION_MS = 1000 * 60 * 15; // 15분
    private final long REFRESH_EXPIRATION_MS = 1000 * 60 * 60 * 24 * 7; // 7일

    private final Key key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    // Access token
    public String generateAccessToken(String username, String role) {
        return Jwts.builder()
                .setSubject(username)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Refresh token
    public String generateRefreshToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_MS))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // 사용자명 추출
    public String getUsername(String token) {
        return parseClaims(token).getSubject();
    }

    // 권한 추출
    public String getRole(String token) {
        return parseClaims(token).get("role", String.class);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Claims 파싱 (내부용)
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
