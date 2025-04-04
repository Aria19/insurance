package com.maghrebia.data_extract.Business.Services;

import java.util.function.Function;

import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;

public interface JwtService {

    public String generateToken(String email, String role, String username);
    public String extractUsername(String token);
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver);
    public boolean validateToken(String token, UserDetails userDetails);
    public Claims extractAllClaims(String token);
}
