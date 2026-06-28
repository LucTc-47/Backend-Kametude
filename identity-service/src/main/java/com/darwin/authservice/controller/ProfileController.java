    package com.darwin.authservice.controller;

import com.darwin.authservice.dto.ProfileResponse;
import com.darwin.authservice.entity.Profile;
import com.darwin.authservice.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileRepository profileRepository;

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable UUID id) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouve"));
        return ResponseEntity.ok(toResponse(profile));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfileResponse> updateProfile(
            @PathVariable UUID id,
            @RequestBody ProfileResponse request) {
        Profile profile = profileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Profil non trouve"));

        profile.setFirstName(request.getFirstName());
        profile.setLastName(request.getLastName());
        profile.setPhone(request.getPhone());
        profile.setAvatarUrl(request.getAvatarUrl());
        profile.setBio(request.getBio());
        profile.setCity(request.getCity());
        profile.setSkills(request.getSkills());

        profileRepository.save(profile);
        return ResponseEntity.ok(toResponse(profile));
    }

    private ProfileResponse toResponse(Profile p) {
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