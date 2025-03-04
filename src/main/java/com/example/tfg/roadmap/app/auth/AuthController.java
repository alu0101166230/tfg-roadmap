package com.example.tfg.roadmap.app.auth;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<String> login(
        @RequestBody LoginDto user
        ) throws NoSuchAlgorithmException {
            boolean isAuthenticated = authService.login(user.getUsername(),user.getPassword());

            return isAuthenticated? 
            ResponseEntity.ok("User logged successfully!") :
            ResponseEntity.status(404).body("User not found or invalid credentials.");

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterDto user
        ) throws NoSuchAlgorithmException {
        authService.register(user.getUsername(),user.getEmail(),user.getPassword());
        return ResponseEntity.ok("User registered successfully!");
    }

}
