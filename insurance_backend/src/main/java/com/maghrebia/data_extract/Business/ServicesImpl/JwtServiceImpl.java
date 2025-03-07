package com.maghrebia.data_extract.Business.ServicesImpl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.core.GrantedAuthority;

import com.maghrebia.data_extract.Business.Services.JwtService;

import org.springframework.security.core.Authentication;
import java.time.Instant;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {

    private final JwtEncoder encoder;

    @Value("${jwt.expiration}")
    private long jwtExpiration;

    @Value("${jwt.cookie-name}")
    private String jwtCookieName;

    public JwtServiceImpl(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    @Override
    public String generateToken(Authentication authentication) {
        try {
            Instant now = Instant.now();
            long expiry = jwtExpiration;
            String scope = authentication.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(" "));

            // Create the JWT claims
            JwtClaimsSet claims = JwtClaimsSet.builder()
                    .issuer("self")
                    .issuedAt(now)
                    .expiresAt(now.plusSeconds(expiry))
                    .subject(authentication.getName())
                    .claim("scope", scope)
                    .build();

            // Sign the claims with the RSA private key using RS256
            return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT token", e);
        }
    }

    @Override
    public ResponseCookie generateJwtCookie(String jwt) {
        try {
            return ResponseCookie.from(jwtCookieName, jwt)
                    .path("/")
                    .maxAge(24 * 60 * 60) // 24 hours
                    .httpOnly(true)
                    .secure(true)
                    .sameSite("Strict")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate JWT cookie", e);
        }
    }

    @Override
    public ResponseCookie getCleanJwtCookie() {
        try {
            return ResponseCookie.from(jwtCookieName, "")
                    .path("/")
                    .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to clean JWT cookie", e);
        }
    }
}

