package com.railpass.web.authentication;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Optional;

import javax.crypto.SecretKey;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

public class TokenGenerator {
    private final static SecretKey TOKEN_SECRET = Keys.hmacShaKeyFor(System.getenv("TOKEN_SECRET").getBytes(StandardCharsets.UTF_8));
    private final static int TOKEN_TTL = 2*60*60*1000; // 2 hours

    public static String generateToken(String subject) {
        Date issuedAt = new Date();
        return Jwts.builder()
            .subject(subject)
            .issuedAt(issuedAt)
            .expiration(new Date(issuedAt.getTime() + TOKEN_TTL))
            .signWith(TOKEN_SECRET)
            .compact();
    }

    public static Optional<String> getSubject(String token) {
        try {
            String subject = Jwts.parser()
                .verifyWith(TOKEN_SECRET)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();

            if (subject == null) return Optional.empty();

            return Optional.of(subject);
        } catch (JwtException e) {
            return Optional.empty();
        }
    }
}
