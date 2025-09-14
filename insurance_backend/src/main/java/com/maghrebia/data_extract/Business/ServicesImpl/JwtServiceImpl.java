package com.maghrebia.data_extract.Business.ServicesImpl;

import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;

import com.maghrebia.data_extract.Business.Services.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = "YOURSECRETMYKEYLOVEBASE643FORYOUENCOISENDLESSDEDHERE";

    // 🔹 Generate a JWT token (Updated version)
    public String generateToken(String email, String role, String username, Long id) {
        return Jwts.builder()
                .subject(email)
                .claim("role", role)  // Adding the role as a custom claim
                .claim("username", username)
                .claim("userId", id)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))  // Token expiration time
                .signWith(getSigningKey())
                .compact();
    }
    

    // 🔹 Extract username from JWT
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Long extractId(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("userId", Long.class);
    }

    // 🔹 Extract claims
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    // 🔹 Validate JWT token
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // 🔹 Check if token is expired
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // 🔹 Extract expiration date
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    // 🔹 Extract all claims
    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey()) // ✅ Updated: Uses verifyWith() instead of setSigningKey()
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 🔹 Get the signing key (Updated for better security)
    private SecretKey getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
    
}

