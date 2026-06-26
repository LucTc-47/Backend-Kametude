package com.kametude.request_service.entity;

import com.kametude.request_service.enums.JobRequestStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "job_requests")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID du client qui poste l'appel d'offre (vient de identity-service)
    private Long clientId;

    private String title;

    private String description;

    private String category;

    private String city;

    private Double budget;

    private LocalDateTime deadline;

    @Enumerated(EnumType.STRING)
    private JobRequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "jobRequest", cascade = CascadeType.ALL)
    private List<Proposal> proposals;

    @PrePersist
    public void prePersist() {
        this.status = JobRequestStatus.OPEN;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
