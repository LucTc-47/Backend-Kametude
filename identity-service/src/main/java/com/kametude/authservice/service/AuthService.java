package com.kametude.authservice.service;

import com.kametude.authservice.dto.AuthResponse;
import com.kametude.authservice.dto.LoginRequest;
import com.kametude.authservice.dto.RegisterRequest;
import com.kametude.authservice.dto.ProfileResponse;
import com.kametude.authservice.entity.Profile;
import com.kametude.authservice.entity.User;
import com.kametude.authservice.repository.ProfileRepository;
import com.kametude.authservice.repository.UserRepository;
import com.kametude.authservice.security.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email deja utilise : " + request.getEmail());
        }

        // 1. Créer le user
        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        // 2. Créer le profil automatiquement
        Profile profile = Profile.builder()
                .userId(user.getId())
                .email(user.getEmail())
                .role(user.getRole().toLower())
                .verified(false)
                .createdAt(Instant.now())
                .build();

        profileRepository.save(profile);

        return AuthResponse.builder()
                .token(jwtUtils.generateToken(user))
                .refreshToken(jwtUtils.generateRefreshToken(user))
                .email(user.getEmail())
                .role(user.getRole().toLower())
                .profile(toProfileResponse(profile))
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profil non trouve"));

        return AuthResponse.builder()
                .token(jwtUtils.generateToken(user))
                .refreshToken(jwtUtils.generateRefreshToken(user))
                .email(user.getEmail())
                .role(user.getRole().toLower())
                .profile(toProfileResponse(profile))
                .build();
    }

    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtUtils.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));

        if (!jwtUtils.isTokenValid(refreshToken, user)) {
            throw new RuntimeException("Refresh token invalide");
        }

        Profile profile = profileRepository.findByUserId(user.getId())
                .orElseThrow(() -> new RuntimeException("Profil non trouve"));

        return AuthResponse.builder()
                .token(jwtUtils.generateToken(user))
                .refreshToken(jwtUtils.generateRefreshToken(user))
                .email(user.getEmail())
                .role(user.getRole().toLower())
                .profile(toProfileResponse(profile))
                .build();
    }

    public boolean validateToken(String token) {
        String email = jwtUtils.extractUsername(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouve"));
        return jwtUtils.isTokenValid(token, user);
    }

    private ProfileResponse toProfileResponse(Profile p) {
        return ProfileResponse.builder()
                .id(p.getId())
                .userId(p.getUserId())
                .firstName(p.getFirstName())
                .lastName(p.getLastName())
                .email(p.getEmail())
                .phone(p.getPhone())
                .avatarUrl(p.getAvatarUrl())
                .bio(p.getBio())
                .city(p.getCity())
                .skills(p.getSkills())
                .rating(p.getRating())
                .role(p.getRole())
                .verified(p.getVerified())
                .createdAt(p.getCreatedAt())
                .build();
    }
}