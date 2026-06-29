package com.example.kametud_catalog.dto;

import com.example.kametud_catalog.entity.Gig;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GigResponse {

    private UUID id;
    private UUID studentId;
    private String title;
    private String description;
    private String category;
    private String location;
    private BigDecimal rating;
    private GigTierDto tierBasique;
    private GigTierDto tierStandard;
    private GigTierDto tierPremium;
    private boolean published;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;

    public static GigResponse fromEntity(Gig gig) {
        return GigResponse.builder()
                .id(gig.getId())
                .studentId(gig.getStudentId())
                .title(gig.getTitle())
                .description(gig.getDescription())
                .category(gig.getCategory())
                .location(gig.getLocation())
                .rating(gig.getRating())
                .tierBasique(gig.getTierBasique())
                .tierStandard(gig.getTierStandard())
                .tierPremium(gig.getTierPremium())
                .published(gig.isPublished())
                .createdAt(gig.getCreatedAt())
                .updatedAt(gig.getUpdatedAt())
                .build();
    }
}
