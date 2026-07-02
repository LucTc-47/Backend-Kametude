package com.kametude.authservice.controller;

import com.kametude.authservice.dto.AuthResponse;
import com.kametude.authservice.dto.LoginRequest;
import com.kametude.authservice.dto.RegisterRequest;
import com.kametude.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(
            @Valid @RequestBody RegisterRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<AuthResponse> refresh(
            @RequestParam String refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken));
    }

    @GetMapping("/validate")
    public ResponseEntity<Boolean> validate(
            @RequestParam String token) {
        try {
            return ResponseEntity.ok(authService.validateToken(token));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}