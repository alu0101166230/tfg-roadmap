package com.example.tfg.roadmap.app.auth;

import java.security.NoSuchAlgorithmException;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:4200")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<Long> login(
        @RequestBody LoginDto user
        ) throws NoSuchAlgorithmException {
            Long isAuthenticated = authService.login(user.getUsername(),user.getPassword());

            return isAuthenticated != -1? 
            ResponseEntity.ok(isAuthenticated) :
            ResponseEntity.status(404).body((long)-1);

    }

    @PostMapping("/register")
    public ResponseEntity<String> register(
        @RequestBody RegisterDto user
        ) throws NoSuchAlgorithmException {
        authService.register(user.getUsername(),user.getEmail(),user.getPassword());
        return ResponseEntity.ok("User registered successfully!");
    }

}
