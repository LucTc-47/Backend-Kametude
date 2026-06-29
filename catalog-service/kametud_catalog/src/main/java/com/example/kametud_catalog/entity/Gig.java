package com.example.kametud_catalog.entity;

import com.example.kametud_catalog.dto.GigTierDto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "gigs",
        indexes = {
                @Index(name = "idx_gigs_category", columnList = "category"),
                @Index(name = "idx_gigs_location", columnList = "location"),
                @Index(name = "idx_gigs_student", columnList = "student_id")
        }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Gig {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(name = "student_id", nullable = false, updatable = false)
    private UUID studentId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "text")
    private String description;

    @Column(nullable = false, length = 100)
    private String category;

    @Column(nullable = false, length = 100)
    private String location;

    @Builder.Default
    @Column(nullable = false, precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tier_basique", nullable = false, columnDefinition = "jsonb")
    private GigTierDto tierBasique;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tier_standard", nullable = false, columnDefinition = "jsonb")
    private GigTierDto tierStandard;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(name = "tier_premium", nullable = false, columnDefinition = "jsonb")
    private GigTierDto tierPremium;

    @Builder.Default
    @Column(nullable = false)
    private boolean published = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false, columnDefinition = "timestamp with time zone")
    private OffsetDateTime updatedAt;
}
