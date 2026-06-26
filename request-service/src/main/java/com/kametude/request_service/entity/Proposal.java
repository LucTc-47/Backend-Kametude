package com.kametude.request_service.entity;

import com.kametude.request_service.enums.ProposalStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "proposals")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Proposal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ID de l'étudiant qui propose (vient de identity-service)
    private Long studentId;

    private String coverLetter;

    private Double proposedPrice;

    // Délai proposé par l'étudiant en jours
    private Integer deliveryDays;

    @Enumerated(EnumType.STRING)
    private ProposalStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    // Lien vers l'appel d'offre
    @ManyToOne
    @JoinColumn(name = "job_request_id")
    private JobRequest jobRequest;

    @PrePersist
    public void prePersist() {
        this.status = ProposalStatus.PENDING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
