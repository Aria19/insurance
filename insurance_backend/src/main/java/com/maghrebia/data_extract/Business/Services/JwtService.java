package com.maghrebia.data_extract.Business.Services;

import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;

public interface JwtService {

    public String generateToken(Authentication authentication);
    public ResponseCookie generateJwtCookie(String jwt);
    public ResponseCookie getCleanJwtCookie();
}
