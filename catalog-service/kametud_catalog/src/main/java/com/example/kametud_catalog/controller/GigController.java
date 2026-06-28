package com.example.kametud_catalog.controller;

import com.example.kametud_catalog.dto.GigCreateRequest;
import com.example.kametud_catalog.dto.GigResponse;
import com.example.kametud_catalog.service.GigService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/gigs")
@RequiredArgsConstructor
public class GigController {

    private final GigService gigService;

    @PostMapping
    public ResponseEntity<GigResponse> createGig(@Valid @RequestBody GigCreateRequest request) {
        GigResponse response = gigService.createGig(request);
        return ResponseEntity.created(URI.create("/api/gigs/" + response.getId()))
                .body(response);
    }

    @GetMapping
    public List<GigResponse> searchGigs(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String location
    ) {
        return gigService.searchGigs(category, location);
    }

    @GetMapping("/{gigId}")
    public GigResponse getGig(@PathVariable UUID gigId) {
        return gigService.getGig(gigId);
    }

    @PatchMapping("/{gigId}/publish")
    public GigResponse publishGig(@PathVariable UUID gigId) {
        return gigService.publishGig(gigId);
    }
}
