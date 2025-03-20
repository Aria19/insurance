package com.maghrebia.data_extract.Web.RestControllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.maghrebia.data_extract.Business.Services.UserService;
import com.maghrebia.data_extract.DTO.LoginRequest;
import com.maghrebia.data_extract.DTO.LoginResponse;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return userService.login(request.getEmail(), request.getPassword());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(HttpServletRequest request) {
        // Invalidate the JWT on the client side (you can also blacklist the token if
        // you choose)
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            // String token = authHeader.substring(7);
            // Optionally, you can blacklist the token here if you're keeping track of
            // invalidated tokens
            // e.g., tokenBlacklistService.addToBlacklist(token);

            // For now, just inform the user to delete the token client-side.
            return ResponseEntity.ok("Successfully logged out.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No token provided.");
    }
}
